package game_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import game_logic.GamePlay;
import game_logic.QuestionAnswer;
import game_logic.TeamData;

public class BoardWindow extends JPanel{
	
	private GamePlay game;

	private JLabel banner;
	private JPanel board;
	private JPanel teams;
	private JPanel progress;
	
	private JTextArea updates;
	
	private JPanel askPanel;
	private JPanel gamePanel;
	
	private JLabel reminderLabel;
	
	private JPanel left;
	private JPanel right;

	private String[] categories;
	private int[] values;
	private String[] teamList;
	private int[] scores;
	private List<TeamData> data;

	private int submitClick = 0;
	
	private int numBets = 0;
	private int numAns = 0;
	
	private JLabel finalJQ;
	
	private JPanel bottomFinalPanel;
	
	private String[] answers;
	
	
	//private QuestionAnswer[][] questions;
	
	private JLabel[] nameArray;
	private JLabel[] scoreArray;
	
	private JPanel sliderPanel;
	
	//private JPanel[][] panelArray;
	
	private Color maroon = new Color(102,0,0);
	private Color lightRed = new Color(255,150,150);
	
	public BoardWindow(GamePlay play) {
		//super();
		game = play;
		data = play.getData();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setSize(1500,1000);
		setVisible(true);
		
		parseData();
		makeLeftBox();
		makeRightBox();
		add(left);
		add(right);
	}
	//game board and banner
	private void makeLeftBox() {
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBackground(Color.GRAY);
		gamePanel.setOpaque(true);
		left = new JPanel();
		left.setLayout(new GridLayout(1,1));
		left.setBackground(Color.GRAY);
		banner = new JLabel("Jeopardy", SwingConstants.CENTER);
		banner.setBorder(new EmptyBorder(10,10,10,10));
		banner.setHorizontalAlignment(SwingConstants.CENTER);
		banner.setForeground(Color.GRAY);
		banner.setBackground(lightRed);
		banner.setFont(new Font("TimesRoman", Font.BOLD, 30));
		banner.setOpaque(true);
		makeGameBoard();
		
		gamePanel.add(banner, BorderLayout.NORTH);
		gamePanel.add(board, BorderLayout.CENTER);
		left.setOpaque(false);
		JPanel space = new JPanel();
		space.setOpaque(false);
		gamePanel.add(space, BorderLayout.SOUTH);
		left.add(gamePanel);
		
	}
	//score and progress
	private void makeRightBox() {
		right = new JPanel();
		right.setLayout(new GridLayout(2,1));
		teams = new JPanel();
		teams.setLayout(new GridLayout(teamList.length,2));
		scores = new int[teamList.length];
		nameArray = new JLabel[teamList.length];
		scoreArray = new JLabel[teamList.length];
		for(int i = 0; i < teamList.length; i++) {
			scores[i] = 0;
			nameArray[i] = new JLabel(teamList[i]);
			nameArray[i].setForeground(Color.WHITE);
			nameArray[i].setBackground(Color.DARK_GRAY);
			nameArray[i].setOpaque(true);
			scoreArray[i] = new JLabel("$" + Long.toString(data.get(i).getPoints()));
			scoreArray[i].setForeground(Color.WHITE);
			scoreArray[i].setBackground(Color.DARK_GRAY);
			scoreArray[i].setOpaque(true);
			teams.add(nameArray[i]);
			teams.add(scoreArray[i]);
		}
		right.add(teams);
		
		progress = new JPanel();
		progress.setLayout(new BorderLayout());
		progress.setBackground(lightRed);
		JLabel title = new JLabel("Game Progress", SwingConstants.CENTER);
		title.setBackground(lightRed);
		title.setForeground(Color.BLACK);
		title.setFont(new Font("TimesRoman", Font.BOLD, 30));
		title.setOpaque(false);
		
		updates = new JTextArea();
		updates.setLineWrap(true);
		updates.setText("Welcome to Jeopardy!");
		updates.append("\n" + "The team to go first is " + teamList[game.getWhoseTurn()]);
		updates.setBackground(lightRed);
		updates.setForeground(Color.BLACK);
		updates.setFont(new Font("TimesRoman", Font.BOLD, 12));
		updates.setOpaque(false);
		
		progress.add(title, BorderLayout.NORTH);
		progress.add(updates, BorderLayout.CENTER);
		
		right.add(progress);
		
	}

	
	private void updateScores() {
		for(int i = 0; i < teamList.length; i++)
			scoreArray[i].setText("$" + Long.toString(data.get(i).getPoints()));
		right.revalidate();
	}
	
	private void makeGameBoard() {
		board = new JPanel();
		board.setLayout(new GridLayout(6,5));
		board.setBorder(new EmptyBorder(20,20,20,20));
		Border border = BorderFactory.createLineBorder(maroon);
		for(int i = 0; i < categories.length; i++) {
			JLabel newCat = new JLabel(categories[i], SwingConstants.CENTER);
			newCat.setForeground(Color.WHITE);
			newCat.setBackground(Color.DARK_GRAY);
			newCat.setOpaque(true);
			//newCat.setBorder(new EmptyBorder(5,5,5,5));
			newCat.setBorder(border);
			board.add(newCat);
		}
		//panelArray = new JPanel[5][5];
		for(int i = 0; i < values.length; i++) {
			for(int j = 0; j < values.length; j++) {
				JPanel questionPanel = new JPanel();
				JLabel newVal = new JLabel(Integer.toString(values[i]), SwingConstants.CENTER);
				newVal.setForeground(Color.WHITE);
				newVal.setOpaque(false);
				newVal.setBorder(new EmptyBorder(15,15,15,15));
				questionPanel.setBorder(border);
				questionPanel.add(newVal);
				questionPanel.setOpaque(true);
				questionPanel.setBackground(Color.DARK_GRAY);
				//panelArray[i][j] = questionPanel;
				QuestionAnswer question = game.questions[i][j];
				int row = j;
				int col = i;
				questionPanel.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						//questionPanel
						//System.out.println("Category: " + categories[row] + " Value: " + values[col]);
						//System.out.println("Category is: " + locatePanelCat(questionPanel));
						if(!game.getQuestions()[row][col].hasBeenAsked()) {
							updates.append("\n" + teamList[game.getWhoseTurn()] + " chose the question in " + categories[row] + " worth $" + values[col] + ".");
							questionView(col, row);
							questionPanel.setBackground(Color.GRAY);
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
				});
				board.add(questionPanel);
			}
		}
		board.setOpaque(false);
	}
	
	private void parseData() {
		teamList = new String[data.size()];
		for(int i = 0; i < data.size(); i++) {
			teamList[i] = data.get(i).getTeamName();
		}
		categories = game.getCategories();
		values = game.getValues();
	}
	
	private void questionView(int row, int col) {
		JPanel qPanel = new JPanel();
		askPanel = new JPanel();
		askPanel.setLayout(new BorderLayout());
		askPanel.setOpaque(true);
		askPanel.setBackground(Color.DARK_GRAY);
		
		JLabel teamLabel = new JLabel(teamList[game.getWhoseTurn()] + "     " + categories[col] + "     $" + values[row], SwingConstants.CENTER);
		teamLabel.setBackground(maroon);
		teamLabel.setForeground(Color.GRAY);
		teamLabel.setOpaque(true);
		teamLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
		
		reminderLabel = new JLabel("Remember to pose your answer as a question.", SwingConstants.CENTER);
		reminderLabel.setBackground(Color.DARK_GRAY);
		reminderLabel.setForeground(Color.GRAY);
		reminderLabel.setOpaque(true);
		reminderLabel.setVisible(false);
		qPanel.setLayout(new BorderLayout());
		qPanel.add(reminderLabel, BorderLayout.NORTH);
		
		JTextArea questionArea = new JTextArea();
		questionArea.setFont(new Font("TimesRoman", Font.BOLD, 20));
		questionArea.setText(game.getQuestionStatement(row, col));
		questionArea.setLineWrap(true);
		game.askQuestion(categories[col], values[row]);
		//JLabel question = new JLabel(game.getQuestionStatement(category, value));
		//JLabel teamLabel = new JLabel();
		
		questionArea.setBackground(lightRed);
		questionArea.setOpaque(true);
		qPanel.add(questionArea, BorderLayout.CENTER);
		//qPanel.setBorder(new EmptyBorder(40,40,40,40));
		qPanel.setOpaque(true);
		qPanel.setBackground(Color.DARK_GRAY);
		JPanel enterAnswer = new JPanel();
		enterAnswer.setLayout(new BorderLayout());
		
		JTextField answerSlot = new JTextField();
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				answerQ(col, row, values[row], answerSlot.getText(), submitClick);
				submitClick++;
				updateScores();
			}
			
		});
		enterAnswer.add(answerSlot, BorderLayout.CENTER);
		enterAnswer.add(submitButton, BorderLayout.EAST);
		//enterAnswer.setBorder(new EmptyBorder(20,0,20,0));
		enterAnswer.setOpaque(false);
		qPanel.setBorder(new EmptyBorder(80,60,60,60));
		qPanel.add(enterAnswer, BorderLayout.SOUTH);
		qPanel.setOpaque(false);

		askPanel.add(qPanel, BorderLayout.CENTER);
		askPanel.add(teamLabel, BorderLayout.NORTH);
		left.remove(gamePanel);
		left.validate();
		left.add(askPanel);
		left.setBackground(Color.DARK_GRAY);
		left.revalidate();
		left.setOpaque(true);
		questionArea.setVisible(true);
		qPanel.setVisible(true);
		askPanel.setVisible(true);
		
	}
	
	private void answerQ(int catIndex, int valIndex, int pointVal, String ans, int att) {
		int answerVal = game.answerQuestion(catIndex, valIndex, pointVal, ans, submitClick);
		if(answerVal > 0) {
			if(answerVal == 1)
				updates.append("\n" + "Incorrect; $" + pointVal + " will be deducted from your total.");
			else if(answerVal == 2)
				updates.append("\n" + "Correct; $" + pointVal + " will be added to your total!");
			submitClick = -1;
			left.removeAll();
			left.add(gamePanel);
			reminderLabel.setVisible(false);
			repaint();
			if(game.returnNumChosenQ() != 25)
				updates.append("\n" + "It is now " + teamList[game.getWhoseTurn()] + "'s turn.");
			else {
				updates.append("\n" + "It's time for Final Jeopardy!");
				playFinalJeopardy();
			}
		}
		else {
			reminderLabel.setVisible(true);
			repaint();
		}
	}
	
	public void resetDisplay() {
		for(Component c : board.getComponents()) {
			c.setBackground(Color.DARK_GRAY);
		}
		updateScores();
		left.removeAll();
		left.add(gamePanel);
		updates.setText("Welcome to Jeopardy!");
		updates.append("\n" + "The team to go first is " + teamList[game.getWhoseTurn()] + ".");
	}
	
	private void playFinalJeopardy(){
		JLabel titleLabel = new JLabel("Final Jeopardy Round");
		titleLabel.setBackground(maroon);
		titleLabel.setForeground(Color.GRAY);
		titleLabel.setOpaque(true);
		titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
		
		askPanel = new JPanel();
		askPanel.setLayout(new BorderLayout());
		askPanel.setOpaque(true);
		askPanel.setBackground(Color.DARK_GRAY);
		askPanel.add(titleLabel, BorderLayout.NORTH);
		
		sliderSetup();
		finalAnswerSetup();
		
		left.remove(gamePanel);
		left.validate();
		left.setLayout(new BorderLayout());
		left.add(askPanel, BorderLayout.CENTER);
		left.add(bottomFinalPanel, BorderLayout.SOUTH);
		left.setBackground(Color.DARK_GRAY);
		left.revalidate();
		left.setOpaque(true);
		
		askPanel.setVisible(true);
	}
	
	private void sliderSetup() {
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(teamList.length,1));
		
		for(int i = 0; i < teamList.length; i++) {
			JPanel teamPanel = new JPanel();
			teamPanel.setLayout(new BorderLayout());
			
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, data.get(i).getPoints().intValue(), 0);
		//slider.addChangeListener(manager);
			slider.setMajorTickSpacing(50);
			slider.setPaintLabels(true);
			slider.setPaintTicks(true);
			slider.setForeground(Color.GRAY);
			slider.setBackground(Color.DARK_GRAY);
			slider.setValue(0);
			slider.setOpaque(true);
			int team = i;
			JButton setBet = new JButton("Set Bet");
			setBet.setBackground(Color.WHITE);
			setBet.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					game.getData().get(team).setBet(slider.getValue());
					setBet.setEnabled(false);
					numBets++;
					testForFinalBet();
				}
				
			});
			
			JLabel nameLabel = new JLabel(teamList[i]);
			nameLabel.setBackground(Color.DARK_GRAY);
			nameLabel.setForeground(Color.GRAY);
			nameLabel.setOpaque(true);
			
			
			teamPanel.add(slider, BorderLayout.CENTER);
			teamPanel.add(setBet, BorderLayout.EAST);
			teamPanel.add(nameLabel, BorderLayout.WEST);
			
			sliderPanel.add(teamPanel);
		}
		askPanel.add(sliderPanel, BorderLayout.CENTER);
	}
	
	private void finalAnswerSetup() {
		bottomFinalPanel = new JPanel();
		bottomFinalPanel.setLayout(new BorderLayout());
		bottomFinalPanel.setBackground(Color.DARK_GRAY);
		
		JPanel holder = new JPanel();
		holder.setLayout(new GridLayout(2,2));
		
		answers = new String[teamList.length];
		
		for(int i = 0; i < teamList.length; i++) {
			JPanel teamPanel = new JPanel();
			teamPanel.setLayout(new GridLayout(1,2));
			
			JTextField input = new JTextField(teamList[i] + ", enter your answer");
			input.setForeground(Color.GRAY);
			
			JButton setAns = new JButton("Submit Answer");
			setAns.setBackground(Color.WHITE);
			
			int index = i;
			
			setAns.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//game.getData().get(team)
					setAns.setEnabled(false);
					input.setForeground(Color.BLACK);
					answers[index] = input.getText();
					numAns++;
					testForFinalAns();
				}
				
			});
			
			teamPanel.add(input);
			teamPanel.add(setAns);
			holder.add(teamPanel);
		}
		
		
		finalJQ = new JLabel("And the question is...", SwingConstants.CENTER);
		finalJQ.setBackground(lightRed);
		finalJQ.setForeground(Color.GRAY);
		finalJQ.setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		bottomFinalPanel.add(finalJQ, BorderLayout.NORTH);
		bottomFinalPanel.add(holder, BorderLayout.CENTER);
		
	}
	
	private void testForFinalBet() {
		if(numBets == teamList.length)
			finalJQ.setText(game.getFinalQ());
	}
	
	private void testForFinalAns() {
		if(numAns == teamList.length) {
			finalJQ.setText("Answer: " + game.getFinalA());
			game.checkFinalJeopardy(answers);
			updateScores();
			ArrayList<Integer> array = game.getWinners();
			String finalOut = "";
			for(int i = 0; i < array.size(); i++) 
				finalOut +=  " " + teamList[array.get(i)] + ", ";
			JOptionPane.showMessageDialog(null, "The Winner(s) is " + finalOut);
		}
	}

}
