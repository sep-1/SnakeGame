package snakeGame;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
/**
 * Name: Sep
 * Dae: June 18, 2022
 * Title: GameFrame
 * Description: This program runs a speed snake game which is based on snake.
 */
public class GameFrame extends JPanel implements ActionListener {
	static final int screenWidth = 600;
	static final int screenHeight = 600;
	static final int startX = screenWidth/4;
	static final int startY = screenHeight/2;
	final int unitSize;
	final int gameDelay;
	final int gameUnits; 
	final int x[];
	final int y[];
	int snakeParts = 6;
	int applesConsumed;
	int appleCordX;
	int appleCordY;
	boolean done;
	long inputDelay = System.currentTimeMillis();
	char direction = 'R';
	Timer timer;
	Random randint; 
	JFrame frame;
	
	/*********************
	* Constructor
	* Pre: none
	* Post: creates a game frame object
	***********************/
	public GameFrame(int units, int delay) {
		//fields setup
		unitSize = units;
		gameDelay = delay;
		randint = new Random();
		gameUnits = (screenWidth*screenHeight)/unitSize;
		x = new int[gameUnits];
		y = new int[gameUnits];
		//frame setup
		frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//panel setup
		this.setFocusable(true);
		this.setBackground(new Color(0,153,0));
		this.addKeyListener(new KeyAdapters());
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		
		// set final package of images
		frame.setContentPane(this);	
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		startGame();
		
	}
	
	/**
	 * @pre none
	 * @param e - ActionEvent
	 * @post a repaint and if the game is running several functions related to 
	 * game continuity.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!done) {
			move();
			checkApple();
			checkCollide();
		}
		repaint();
	}
	/**
	 * @pre none
	 * @post sets the starting position of the 
	 * snake, adds an apple, and begins a timer. 
	 */
	public void startGame() {
		for (int i = 0; i < snakeParts; i++) {
			x[i] = startX - i*unitSize;
			y[i] = startY;
		}
		addApple();
		timer = new Timer(gameDelay, this);
		timer.start();		
	}
	
	/**
	 * @pre none
	 * @param g - Graphics
	 * @post creates drawings on the parent panel 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	/**
	 * @pre a component to paint on and valid timer
	 * @param g - Graphics
	 * @post draws all game elements (grid, apple, snake, etc)
	 */
	public void draw(Graphics g) {
		if (!done) {
			//border grid
			boolean toggle = false;
			g.setColor(new Color(0,102,0));
			for (int r = 0; r < screenHeight; r += unitSize) {
				for (int c = 0; c < screenWidth; c += unitSize) {
					if (toggle) {
						g.fillRect(c, r,unitSize,unitSize);
						toggle = false;
					} else toggle = true;
					
				}
				toggle = !toggle;
			}
			
			//apple 
			g.setColor(Color.green);
			g.drawLine(appleCordX, appleCordY, appleCordX + 5,appleCordY + 8);
			g.setColor(Color.red);
			g.fillOval(appleCordX, appleCordY, unitSize, unitSize);
			
			//snake
			for (int i = 0; i < snakeParts; i++) {
				g.setColor(Color.cyan);
				if (i == 0) {
					//snake head
					if (direction == 'U')
						g.fillRect(x[i], y[i] + unitSize/2, unitSize,unitSize);
					else if (direction == 'D')
						g.fillRect(x[i], y[i] - unitSize/2, unitSize,unitSize);
					else if (direction == 'R') 
						g.fillRect(x[i] - unitSize/2, y[i], unitSize,unitSize);
					else if (direction == 'L') 
						g.fillRect(x[i] + unitSize/2, y[i], unitSize,unitSize);				
					g.fillRoundRect(x[i], y[i], unitSize, unitSize,unitSize,unitSize);
					
					//snake eyes 
					g.setColor(Color.orange);
					int eyesToBody = (int) (unitSize * .32);
					int topEye = (int) (unitSize * .16);
					int bottomEye = (int) (unitSize * .64);
					if (direction == 'L' || direction == 'R') {
						g.fillOval(x[i] + eyesToBody, y[i] + topEye, unitSize/5, unitSize/5);
						g.fillOval(x[i] + eyesToBody, y[i] + bottomEye, unitSize/5, unitSize/5);
					} else if (direction == 'U' || direction == 'D') {
						g.fillOval(x[i] + topEye, y[i] + eyesToBody, unitSize/5, unitSize/5);
						g.fillOval(x[i] + bottomEye, y[i] + eyesToBody, unitSize/5, unitSize/5);
					}

				} else
					//snake body
					g.fillRect(x[i], y[i], unitSize,unitSize);
				
				g.setColor(Color.black);
				g.setFont(new Font("Times New Roman", Font.BOLD, 40));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize());
			} 
		} else {
			if (applesConsumed >= ((screenWidth/unitSize) * (screenHeight/unitSize))) {
				winner(g);
			} else gameOver(g);
		}
		
	}
	
	/**
	 * @pre valid timer 
	 * @post moves snake in given direction based on direction field. 
	 */
	public void move() {
		for (int i = snakeParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - unitSize; // move up
			break;
		case 'D':
			y[0] = y[0] + unitSize; // move down
			break;
		case 'L':
			x[0] = x[0] - unitSize; // move left
			break;
		case 'R':
			x[0] = x[0] + unitSize; //move right
			break;
		}
	}
	
	/**
	 * @pre none 
	 * @post checks whether snake head has landed on a tile with an apple and consumes it
	 * by increasing score and body length by one, new apple added after.
	 */
	public void checkApple() {
		if (x[0] == appleCordX && y[0] == appleCordY) {
			snakeParts++;
			applesConsumed++;
			addApple();
		}
	}
	
	/**
	 * @pre none
	 * @post adds a new apple at a randomly determined tile. 
	 */
	public void addApple() {
		if (applesConsumed >= ((screenWidth/unitSize) * (screenHeight/unitSize)))
			done = true;
		else {	
			appleCordX = randint.nextInt(screenWidth/unitSize) * unitSize;
			appleCordY = randint.nextInt(screenHeight/unitSize) * unitSize;
			for (int i = 0; i < snakeParts; i++) {
				if (appleCordX == x[i] && appleCordY == y[i]) {
					addApple();
				}
			}
		}
	}
	
	/**
	 * @pre valid snakeparts and position table
	 * @post cbecks whether the snake has collided with itself or the border. 
	 */
	public void checkCollide() {
		for (int i = snakeParts; i > 0; i--) {
			//checks body collisions
			if ((x[0] == x[i] && y[0] == y[i]) && !(x[0] == 0 && y[0] == 0 && x[i] == 0 && y[i] == 0))
				done = true; 
		}
		//checks left and right border collisions
		if (x[0] >= screenWidth || x[0] <= -unitSize) 
			done = true;
		//checks bottom and top border collisions
		if (y[0] >= screenHeight || y[0] <= -unitSize) 
			done = true;
		if (done) timer.stop();
	}
	
	/**
	 * @pre valid timer and graphics.
	 * @param g - Graphics
	 * @post draws text on screen indicating a winner.
	 */
	public void winner(Graphics g) {
		//score 
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize());
		//game over
		g.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("You Win!",(screenWidth - metrics2.stringWidth("You Win!"))/2, screenHeight/2);
	}
	
	/**
	 * @pre valid timer and graphics
	 * @param g - Graphics
	 * @post draws text on screen indicating game over.
	 */
	public void gameOver(Graphics g) {
		//score 
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize());
		//game over
		g.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over",(screenWidth - metrics2.stringWidth("Game Over"))/2, screenHeight/2);	
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press Space to Continue",(screenWidth - metrics3.stringWidth("Press Space to Continue"))/2, (int)(screenHeight*.75));	
		
	}
	
	
	public class KeyAdapters extends KeyAdapter {
		/**
		 * @pre parent of class is keyadapter 
		 * @param e - KeyEvent
		 * @post deals with user input and how to handle it with different aspects of the game
		 * such as movement and returning to menu. 
		 */
		public void keyPressed(KeyEvent e) {
			if (System.currentTimeMillis() - inputDelay > 55) { //input delay of 55 ms to prevent snake head from going into its own body
				switch(e.getKeyCode()) {
				case KeyEvent.VK_A:
					if (direction == 'U' || direction == 'D')
						direction = 'L';
					break;
				case KeyEvent.VK_D:
					if (direction == 'U' || direction == 'D')
						direction = 'R';
					break;
				case KeyEvent.VK_W:
					if (direction == 'L' || direction == 'R')
						direction = 'U';
					break;
				case KeyEvent.VK_S:
					if (direction == 'L' || direction == 'R')
						direction = 'D';
					break;
				case KeyEvent.VK_SPACE:
					if (done) {
						new MenuScreen();
						frame.setVisible(false);
						frame = null;
					}
					break;
				}
				inputDelay = System.currentTimeMillis(); 
			}
			
		}
	}
}

/*
Class GameFrame
Class Fields:
	static final int screenWidth = 600
	static final int screenHeight = 600
	static final int startX = screenWidth/4
	static final int startY = screenHeight/2
	final int unitSize
	final int gameDelay
	final int gameUnits 
	final int x[]
	final int y[]
	int snakeParts = 6
	int applesConsumed
	int appleCordX
	int appleCordY
	boolean done
	long inputDelay = System.currentTimeMillis()
	char direction = 'R'
	Timer timer
	Random randint 
	JFrame frame
	
	Class Constructors:
	
	GameFrame(int units, int delay) {
		unitSize = units
		gameDelay = delay
		randint = new Random()
		gameUnits = (screenWidth*screenHeight)/unitSize
		x = new int[gameUnits]
		y = new int[gameUnits]
		frame = new JFrame("Snake Game")
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		frame.setResizable(false)
		
		this.setFocusable(true)
		this.setBackground(new Color(0,153,0))
		this.addKeyListener(new KeyAdapters())
		this.setPreferredSize(new Dimension(screenWidth,screenHeight))
		
		frame.setContentPane(this)	
		frame.pack()
		frame.setVisible(true)
		frame.setLocationRelativeTo(null)
		startGame()
		
	}
	
	Class Methods: 
	
	actionPerformed(ActionEvent e) {
		if (!done) {
			move()
			checkApple()
			checkCollide()
		}
		repaint()
	} end actionPerformed
	
	startGame() {
		for (int i = 0 i < snakeParts i++) {
			x[i] = startX - i*unitSize
			y[i] = startY
		}
		addApple()
		timer = new Timer(gameDelay, this)
		timer.start()		
	} end startGame
	
	paintComponent(Graphics g) {
		super.paintComponent(g)
		draw(g)
	} end paintComponent
	
	draw(Graphics g) {
		if (!done) {
			boolean toggle = false
			g.setColor(new Color(0,102,0))
			for (int r = 0 r < screenHeight r += unitSize) {
				for (int c = 0 c < screenWidth c += unitSize) {
					if (toggle) {
						g.fillRect(c, r,unitSize,unitSize)
						toggle = false
					} else toggle = true
					
				}
				toggle = !toggle
			}
			
			g.setColor(Color.green)
			g.drawLine(appleCordX, appleCordY, appleCordX + 5,appleCordY + 8)
			g.setColor(Color.red)
			g.fillOval(appleCordX, appleCordY, unitSize, unitSize)
			
			for (int i = 0 i < snakeParts i++) {
				g.setColor(Color.cyan)
				if (i == 0) {

					if (direction == 'U')
						g.fillRect(x[i], y[i] + unitSize/2, unitSize,unitSize)
					else if (direction == 'D')
						g.fillRect(x[i], y[i] - unitSize/2, unitSize,unitSize)
					else if (direction == 'R') 
						g.fillRect(x[i] - unitSize/2, y[i], unitSize,unitSize)
					else if (direction == 'L') 
						g.fillRect(x[i] + unitSize/2, y[i], unitSize,unitSize)				
					g.fillRoundRect(x[i], y[i], unitSize, unitSize,unitSize,unitSize)
					
					g.setColor(Color.orange)
					int eyesToBody = (int) (unitSize * .32)
					int topEye = (int) (unitSize * .16)
					int bottomEye = (int) (unitSize * .64)
					if (direction == 'L' || direction == 'R') {
						g.fillOval(x[i] + eyesToBody, y[i] + topEye, unitSize/5, unitSize/5)
						g.fillOval(x[i] + eyesToBody, y[i] + bottomEye, unitSize/5, unitSize/5)
					} else if (direction == 'U' || direction == 'D') {
						g.fillOval(x[i] + topEye, y[i] + eyesToBody, unitSize/5, unitSize/5)
						g.fillOval(x[i] + bottomEye, y[i] + eyesToBody, unitSize/5, unitSize/5)
					}

				} else
					g.fillRect(x[i], y[i], unitSize,unitSize)
				
				g.setColor(Color.black)
				g.setFont(new Font("Times New Roman", Font.BOLD, 40))
				FontMetrics metrics = getFontMetrics(g.getFont())
				g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize())
			} 
		} else {
			if (applesConsumed >= ((screenWidth/unitSize) * (screenHeight/unitSize))) {
				winner(g)
			} else gameOver(g)
		}
		
	} end draw

	move() {
		for (int i = snakeParts i>0 i--) {
			x[i] = x[i-1]
			y[i] = y[i-1]
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - unitSize 
			break
		case 'D':
			y[0] = y[0] + unitSize
			break
		case 'L':
			x[0] = x[0] - unitSize 
			break
		case 'R':
			x[0] = x[0] + unitSize
			break
		}
	}
	
	public void checkApple() {
		if (x[0] == appleCordX && y[0] == appleCordY) {
			snakeParts++
			applesConsumed++
			addApple()
		}
	} end move
	
	addApple() {
		if (applesConsumed >= ((screenWidth/unitSize) * (screenHeight/unitSize)))
			done = true
		else {	
			appleCordX = randint.nextInt(screenWidth/unitSize) * unitSize
			appleCordY = randint.nextInt(screenHeight/unitSize) * unitSize
			for (int i = 0 i < snakeParts i++) {
				if (appleCordX == x[i] && appleCordY == y[i]) {
					addApple()
				}
			}
		}
	} end addApple
	
	checkCollide() {
		for (int i = snakeParts i > 0 i--) {
			if ((x[0] == x[i] && y[0] == y[i]) && !(x[0] == 0 && y[0] == 0 && x[i] == 0 && y[i] == 0))
				done = true 
		}
		//checks left and right border collisions
		if (x[0] >= screenWidth || x[0] <= -unitSize) 
			done = true
		//checks bottom and top border collisions
		if (y[0] >= screenHeight || y[0] <= -unitSize) 
			done = true
		if (done) timer.stop()
	} end checkCollide
	
	winner(Graphics g) {
		//score 
		g.setColor(Color.red)
		g.setFont(new Font("Times New Roman", Font.BOLD, 40))
		FontMetrics metrics = getFontMetrics(g.getFont())
		g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize())
		//game over
		g.setFont(new Font("Times New Roman", Font.ITALIC, 40))
		FontMetrics metrics2 = getFontMetrics(g.getFont())
		g.drawString("You Win!",(screenWidth - metrics2.stringWidth("You Win!"))/2, screenHeight/2)
	} end winner
	
	gameOver(Graphics g) {
		g.setColor(Color.red)
		g.setFont(new Font("Times New Roman", Font.BOLD, 40))
		FontMetrics metrics = getFontMetrics(g.getFont())
		g.drawString("Score: " +applesConsumed,(screenWidth - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize())
		g.setFont(new Font("Times New Roman", Font.ITALIC, 40))
		FontMetrics metrics2 = getFontMetrics(g.getFont())
		g.drawString("Game Over",(screenWidth - metrics2.stringWidth("Game Over"))/2, screenHeight/2)	
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20))
		FontMetrics metrics3 = getFontMetrics(g.getFont())
		g.drawString("Press Space to Continue",(screenWidth - metrics3.stringWidth("Press Space to Continue"))/2, (int)(screenHeight*.75))	
	} end gameOver
	
	
	Class KeyAdapters 
	Class Methods: 
		keyPressed(KeyEvent e) {
			if (System.currentTimeMillis() - inputDelay > 55) { //input delay of 55 ms to prevent snake head from going into its own body
				switch(e.getKeyCode()) {
				case KeyEvent.VK_A:
					if (direction == 'U' || direction == 'D')
						direction = 'L'
					break
				case KeyEvent.VK_D:
					if (direction == 'U' || direction == 'D')
						direction = 'R'
					break
				case KeyEvent.VK_W:
					if (direction == 'L' || direction == 'R')
						direction = 'U'
					break
				case KeyEvent.VK_S:
					if (direction == 'L' || direction == 'R')
						direction = 'D'
					break
				case KeyEvent.VK_SPACE:
					if (done) {
						new MenuScreen()
						frame.setVisible(false)
						frame = null
					}
					break
				}
				inputDelay = System.currentTimeMillis() 
			}
			
		} end keyPressed
	}
}
*/
