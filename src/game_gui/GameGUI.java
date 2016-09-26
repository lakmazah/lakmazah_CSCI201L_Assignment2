package game_gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game_logic.GamePlay;

//import client.Constants;

public class GameGUI extends JFrame{
	
	private GamePlay play;
	
	private JPanel game = new JPanel();
	private StartWindow start;
	private BoardWindow board;
	private JPanel finalJ = new JPanel();
	private JMenuBar menuBar;
	private JMenu menu;
	
	private Color maroon = new Color(102,0,0);
	private Color lightRed = new Color(255,150,150);
	
	public GameGUI() {
		super("Jeopardy");
		setSize(700,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game.setLayout(new CardLayout());
		//game.setBackground(Color.DARK_GRAY);
		createGUI();
		checkStartGame();
		//createBoard();
		
		//game.add(question, "Question");
		//game.add(finalJ, "Final Jeopardy");
		//createGUI();
		
		add(game);
		setVisible(true);
	}
	
	private void createGUI() {
		start = new StartWindow();
		game.add(start, "Jeopardy");
		
	}
	
	private void checkStartGame() {
		JButton startButton = start.returnStartButton();
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(start.isReady()) {
					play = new GamePlay(start.getFileName(), start.getTeams(), start.getNumTeams());
					board = new BoardWindow(play);
					menuSetup();
					game.add(board, "Game Board");
					
					CardLayout cl = (CardLayout) game.getLayout();
					//createBoard();
					cl.show(game, "Game Board");
				}
			}
			
		});
	}

	private void menuSetup() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		menuBar.add(menu);
		JMenuItem restart = new JMenuItem(new AbstractAction("Restart This Game") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				play.resetData();
				play.playGame();
				board.resetDisplay();
			}
			
		});
		
		JMenuItem newFile = new JMenuItem(new AbstractAction("Choose New Game File") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CardLayout c2 = (CardLayout) game.getLayout();
				c2.show(game, "Jeopardy");
			}
			
		});
		JMenuItem exit = new JMenuItem(new AbstractAction("Exit Game") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		menu.add(restart);
		menu.add(newFile);
		menu.add(exit);
		setJMenuBar(menuBar);
	}
	
	
	
}
