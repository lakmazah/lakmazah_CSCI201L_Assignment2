package game_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game_logic.GamePlay;

public class StartWindow extends JPanel{
	private JPanel teamGrid;
	private JPanel team1;
	private JPanel team2;
	private JPanel team3;
	private JPanel team4;
	
	private JLabel team1Label;
	private JLabel team2Label;
	private JLabel team3Label;
	private JLabel team4Label;
	
	private static JTextField team1Field;
	private static JTextField team2Field;
	private static JTextField team3Field;
	private static JTextField team4Field;
	
	private JLabel fileName;
	private String[] names = new String[4];
	
	private JSlider slider;
	private JButton startButton;
	private JButton clearButton;
	private JButton exitButton;
	
	private boolean complete = false;
	
	private static String filePath;
	
	private Color maroon = new Color(102,0,0);
	private Color lightRed = new Color(255,150,150);
	
	public StartWindow() {
		setSize(100,600);
		setLayout(new BorderLayout());
		setBackground(maroon);
		JLabel banner = new JLabel();
		banner.setText("<html><font size=18><center>Welcome to Jeopardy!</center></font><br/><font size=4><center>Choose the game file, number of teams, and team names before starting the game.</center></font></html>");
		banner.setFont(new Font("TimesRoman", Font.BOLD, 10));
		banner.setHorizontalAlignment(SwingConstants.CENTER);
		banner.setBackground(lightRed);
		banner.setOpaque(true);
		add(banner, BorderLayout.NORTH);
		
		JPanel inputs = new JPanel();
		inputs.setOpaque(false);
		inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
		
		JPanel fileSelection = new JPanel();
		JLabel file = new JLabel("Please choose a game file.");
		file.setForeground(Color.WHITE);
		//String fileName;
		JButton fileButton = new JButton("Choose File");
		fileName = new JLabel();
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(fileButton);
				String name = fc.getSelectedFile().getName();
				if(name.substring(name.length()-4, name.length()).equals(".txt")){
					filePath = fc.getSelectedFile().getAbsolutePath();
					System.out.println("path: " + filePath);
					fileName.setText(fc.getSelectedFile().getName());
					fileName.setForeground(Color.WHITE);
				}
			}
		});
		setVisible(true);
		fileSelection.add(file);
		fileSelection.add(fileButton);
		fileSelection.add(fileName);
		fileSelection.setOpaque(false);
		
		JPanel sliderSection = new JPanel();
		sliderSection.setOpaque(false);
		sliderSection.setLayout(new GridLayout(2,1));
		JLabel sliderLabel = new JLabel("Please choose the number of teams that will be playing on the slider below.", SwingConstants.CENTER);
		sliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sliderLabel.setForeground(Color.WHITE);
		sliderLabel.setBackground(maroon);
		
		slider = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);
		//slider.addChangeListener(manager);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setForeground(Color.WHITE);
		slider.setBackground(Color.DARK_GRAY);
		slider.setValue(1);
		slider.setSnapToTicks(true);
		slider.setOpaque(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateTeamGrid();
			}
		});
		inputs.add(fileSelection);
		sliderSection.add(sliderLabel);
		sliderSection.add(slider);
		inputs.add(sliderSection);
		
		createTeamGrid();
		
		inputs.add(teamGrid);
		this.add(inputs, BorderLayout.CENTER);
		JPanel buttonBox = new JPanel();
		startButton = new JButton("Start Jeopardy");
		clearButton = new JButton("Clear Choices");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				clearTextFields();
				slider.setValue(1);
				updateTeamGrid();
				fileName.setText("");
			}
		});
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		
		buttonBox.add(startButton);
		buttonBox.add(clearButton);
		buttonBox.add(exitButton);
		buttonBox.setOpaque(false);
		
		add(buttonBox, BorderLayout.SOUTH);
		
	}
	
	private void createTeamGrid() {
		teamGrid = new JPanel();
		teamGrid.setLayout(new GridLayout(2,2));
		teamGrid.setOpaque(false);
		
		team1 = new JPanel();
		team2 = new JPanel();
		team3 = new JPanel();
		team4 = new JPanel();
		
		team1.setOpaque(false);
		team2.setOpaque(false);
		team3.setOpaque(false);
		team4.setOpaque(false);
		
		team1.setBorder(new EmptyBorder(20,60,20,60));
		team2.setBorder(new EmptyBorder(20,60,20,60));
		team3.setBorder(new EmptyBorder(20,60,20,60));
		team4.setBorder(new EmptyBorder(20,60,20,60));
		
		team1.setLayout(new GridLayout(2,1));
		team2.setLayout(new GridLayout(2,1));
		team3.setLayout(new GridLayout(2,1));
		team4.setLayout(new GridLayout(2,1));
		
		team1Label = new JLabel("Please name Team1");
		team2Label = new JLabel("Please name Team2");
		team3Label = new JLabel("Please name Team3");
		team4Label = new JLabel("Please name Team4");
		
		team1Label.setForeground(Color.WHITE);
		team1Label.setBackground(Color.DARK_GRAY);
		team2Label.setForeground(Color.WHITE);
		team2Label.setBackground(Color.DARK_GRAY);
		team3Label.setForeground(Color.WHITE);
		team3Label.setBackground(Color.DARK_GRAY);
		team4Label.setForeground(Color.WHITE);
		team4Label.setBackground(Color.DARK_GRAY);
		
		team1Label.setOpaque(true);
		team2Label.setOpaque(true);
		team3Label.setOpaque(true);
		team4Label.setOpaque(true);
		
		team1Field = new JTextField();
		team2Field = new JTextField();
		team3Field = new JTextField();
		team4Field = new JTextField();
		
		team1Field.setBackground(lightRed);
		team2Field.setBackground(lightRed);
		team3Field.setBackground(lightRed);
		team4Field.setBackground(lightRed);
		
		team1.add(team1Label);
		team1.add(team1Field);
		team2.add(team2Label);
		team2.add(team2Field);
		team3.add(team3Label);
		team3.add(team3Field);
		team4.add(team4Label);
		team4.add(team4Field);
		
		teamGrid.add(team1);
		teamGrid.add(team2);
		teamGrid.add(team3);
		teamGrid.add(team4);
		
		team2.setVisible(false);
		team3.setVisible(false);
		team4.setVisible(false);
	}
	
	private void updateTeamGrid() {
		switch(slider.getValue()) {
		case 1:
			team1.setVisible(true);
			team2.setVisible(false);
			team3.setVisible(false);
			team4.setVisible(false);
			team2Field.setText("");
			team3Field.setText("");
			team4Field.setText("");
			break;
		case 2:
			team1.setVisible(true);
			team2.setVisible(true);
			team3.setVisible(false);
			team4.setVisible(false);
			team3Field.setText("");
			team4Field.setText("");
			break;
		case 3:
			team1.setVisible(true);
			team2.setVisible(true);
			team3.setVisible(true);
			team4.setVisible(false);
			team4Field.setText("");
			break;
		case 4:
			team1.setVisible(true);
			team2.setVisible(true);
			team3.setVisible(true);
			team4.setVisible(true);
			break;
		}
	}
	
	public boolean isReady() {
		if(team1.isVisible() && team1Field.getText().equals("")) {
			return false;
		}
		if(team2.isVisible() && team2Field.getText().equals(""))
			return false;
		if(team3.isVisible() && team3Field.getText().equals(""))
			return false;
		if(team4.isVisible() && team4Field.getText().equals(""))
			return false;
		if(fileName.getText().equals(""))
			return false;
		if(team1Field.getText().equals(team2Field.getText()) && team1.isVisible() && team2.isVisible())
			return false;
		if(team1Field.getText().equals(team3Field.getText()) && team1.isVisible() && team3.isVisible())
			return false;
		if(team1Field.getText().equals(team4Field.getText()) && team1.isVisible() && team4.isVisible())
			return false;
		if(team2Field.getText().equals(team3Field.getText()) && team2.isVisible() && team3.isVisible())
			return false;
		if(team2Field.getText().equals(team4Field.getText()) && team2.isVisible() && team4.isVisible())
			return false;
		if(team3Field.getText().equals(team4Field.getText()) && team3.isVisible() && team4.isVisible())
			return false;
		
		names[0] = team1Field.getText();
		names[1] = team2Field.getText();
		names[2] = team3Field.getText();
		names[3] = team4Field.getText();
		
		return true;
	}
	
	private void clearTextFields() {
		team1Field.setText("");
		team2Field.setText("");
		team3Field.setText("");
		team4Field.setText("");
	}
	
	public int getNumTeams() {
		return slider.getValue();
	}
	
	public String getFileName() {
		System.out.println("in get filename: " + filePath);
		return filePath;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public String[] getTeams() {
		return names;
	}
	
	public JButton returnStartButton() {
		return startButton;
	}
}
