package snakeGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Name: Sepehr Rajabian
 * Dae: June 18, 2022
 * Title: MenuScreen
 * Description: This program creates a menu screen gui to eventually create the gameframe object 
 */
public class MenuScreen implements ActionListener {
	JFrame frame;
	JPanel panel;
	JLabel title;
	JButton easy;
	JButton regular;
	JButton instructions;
	boolean instructionsEnabled;
	
	/*********************
	* Constructor
	* Pre: none
	* Post: creates a menu screen object
	***********************/
	public MenuScreen() {	
		
		//object setup
		frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//panel setup
		panel = new JPanel();
		panel.setFocusable(true);
		panel.setBackground(new Color(0,153,0));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setLayout(new GridLayout(4, 4));
		
		//title
		title = new JLabel("  SPEED Snake!");
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setFont(new Font("Times New Roman", Font.BOLD, 40));
		panel.add(title);
		
		//buttons setup
		easy = new JButton("Easy Mode");
		easy.setActionCommand("easy");
		easy.addActionListener(this);
		easy.setAlignmentX(JButton.CENTER_ALIGNMENT);
		easy.setAlignmentY(JButton.BOTTOM_ALIGNMENT);
		panel.add(easy);
		
		regular = new JButton("Default Mode");
		regular.setActionCommand("regular");
		regular.addActionListener(this);
		regular.setAlignmentX(JButton.CENTER_ALIGNMENT);
		regular.setAlignmentY(JButton.BOTTOM_ALIGNMENT);
		panel.add(regular);
		
		instructions = new JButton("Instructions");
		instructions.setActionCommand("instructions");
		instructions.addActionListener(this);
		instructions.setAlignmentX(JButton.CENTER_ALIGNMENT);
		instructions.setAlignmentY(JButton.BOTTOM_ALIGNMENT);
		panel.add(instructions);
		
		// set final package of images
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	/*********************
	* Pre: none
	* Post: creates a new menuscreen object and initializes the gui elements.
	***********************/
	public static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new MenuScreen();
	}
	
	/*********************
	* Pre: a valid argument for the ActionEvent
	* Post: deals with button presses and creates gameframe object.
	***********************/
	public void actionPerformed(ActionEvent event) {
		String eventName = event.getActionCommand();
		if (eventName.equals("easy")) {
			new GameFrame(50, 70);
			frame.setVisible(false);
			frame = null;
		} else if (eventName.equals("regular")) {
			new GameFrame(25, 70);
			frame.setVisible(false);
			frame = null;
		} else if (eventName.equals("instructions")) {
			if (!instructionsEnabled) {
				JTextArea instructionsExplanation = new JTextArea("Use W to move up, A to move left, S to move down, and D to move right. The objective of the game is to eat the apples to grow the snakes body to be as long as possible. Avoid hitting the borders of the screen as well as your own tail.");
				instructionsExplanation.setAlignmentX(JLabel.TOP_ALIGNMENT);
				instructionsExplanation.setLineWrap(true);
				instructionsExplanation.setFont(new Font("Times New Roman", Font.BOLD, 12));
				panel.remove(title);
				panel.add(instructionsExplanation);
				frame.pack();		
				instructionsEnabled = true;
			}
		}
			
	}
	/** Main Program
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}

}

/*
Class fields: 
JFrame frame
JPanel panel
JLabel title
JButton easy
JButton regular
JButton instructions
boolean instructionsEnabled

	
Class Constructors:

public MenuScreen() {	
	
	frame = new JFrame()
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	frame.setResizable(false)
	
	panel = new JPanel()
	panel.setFocusable(true)
	panel.setBackground(new Color(0,153,0))
	panel.setPreferredSize(new Dimension(300,300))
	panel.setLayout(new GridLayout(4, 4))
	
	JLabel title = new JLabel(" SPEED Snake!")
	title.setAlignmentX(JLabel.CENTER_ALIGNMENT)
	title.setFont(new Font("Arial", Font.BOLD, 40))
	panel.add(title)
	
	easy = new JButton("Easy Mode")
	easy.setActionCommand("easy")
	easy.addActionListener(this)
	easy.setAlignmentX(JButton.CENTER_ALIGNMENT)
	easy.setAlignmentY(JButton.BOTTOM_ALIGNMENT)
	panel.add(easy)
	
	regular = new JButton("Default Mode")
	regular.setActionCommand("regular")
	regular.addActionListener(this)
	regular.setAlignmentX(JButton.CENTER_ALIGNMENT)
	regular.setAlignmentY(JButton.BOTTOM_ALIGNMENT)
	panel.add(regular)
	
	// set final package of images
	frame.setContentPane(panel)
	frame.pack()
	frame.setVisible(true)
	frame.setLocationRelativeTo(null)
} end MenuScreen
	
Class Methods: 
runGUI() {
	JFrame.setDefaultLookAndFeelDecorated(true)
	new MenuScreen()
} end runGUI

actionPerformed(ActionEvent event) {
	String eventName = event.getActionCommand()
	if (eventName.equals("easy")) {
		new GameFrame(50, 70)
		frame.setVisible(false)
		frame = null
	} else if (eventName.equals("regular")) {
		new GameFrame(25, 70)
		frame.setVisible(false)
		frame = null
	}  else if (eventName.equals("instructions")) {
		(if (!instructionsEnabled) {
			JTextArea instructionsExplanation = new JTextArea("Use W to move up, A to move left, S to move down, and D to move right. The objective of the game is to eat the apples to grow the snakes body to be as long as possible. Avoid hitting the borders of the screen as well as your own tail.")
			instructionsExplanation.setAlignmentX(JLabel.TOP_ALIGNMENT)
			instructionsExplanation.setLineWrap(true)
			instructionsExplanation.setFont(new Font("Times New Roman", Font.BOLD, 12))
			panel.remove(title)
			panel.add(instructionsExplanation)
			frame.pack()		
			instructionsEnabled = true
		}
	}
} end actionPerformed
Main Program: 
	
main(String[] args) {
	
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			runGUI()
		}
	})
}

*/
