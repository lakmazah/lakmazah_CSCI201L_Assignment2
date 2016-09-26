package game_logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameData {

	protected BufferedReader br;
	private FileReader fr;
	//contains questions with their answer and boolean flag as to whether they have been asked
	public QuestionAnswer[][] questions;
	
	//maps from the point value/category to their index in the appropriate array
	protected Map<Integer, Integer> pointValuesMapToIndex;
	protected Map<String, Category> categoriesMap;
	
	protected String finalJeopardyQuestion;
	protected String finalJeopardyAnswer;
	
	public GameData(String fileName) {
		
		pointValuesMapToIndex = new HashMap<>();
		categoriesMap = new HashMap<>();
		questions = new QuestionAnswer[5][5];
		
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			
			parseCategoriesAndPoints();
			parseQuestions();
		}
		catch (FileNotFoundException e) {
			exit("Invalid file! not found");
		}
		catch (IOException e) {
			exit("Invalid file!");
		}
		finally{
			
			try{
				close();	
			}
			catch(IOException ioe){
				exit("Something went wrong!");
			}
		}
		
	}
	
	public void close() throws IOException{
		
		if (fr != null) fr.close();
		if (br != null) br.close();
	}
	
	public static void exit(String errorMessage){
		
		System.out.println(errorMessage);
		System.exit(0);
	}
	
	public void parseCategoriesAndPoints() throws IOException{
		
		String categories = br.readLine();
		String[] parsedCategories = categories.split("::");
		
		if (parsedCategories.length != 5){
			exit("Too many or too few categories provided.");
		}
		
		for (String str : parsedCategories){
			
			if (str.trim().equals("")){
				exit("One of the categories is whitespace.");
			}
		}
		
		String pointValues = br.readLine();
		String[] parsedPointValues = pointValues.split("::");
		
		if (parsedPointValues.length != 5){
			exit( "Too many or too few dollar values provided.");
		}
		
		for (int i = 0; i<5; i++){
			categoriesMap.put(parsedCategories[i].toLowerCase().trim(), new Category(parsedCategories[i].trim(), i));
			
			try{
				pointValuesMapToIndex.put(Integer.parseInt(parsedPointValues[i].trim()), i);
			}
			catch (NumberFormatException nfe){
				exit("One of the point values is a string.");
			}
		}
	}
	
	public void parseQuestions() throws IOException{
		
		String templine = "";
		String fullData = "";
		int questionCount = 0;
		boolean haveFinalJeopardy = false;

		while(questionCount != 26){
			
			templine = br.readLine();
			if (templine == null){
				exit("Not enough questions in the file");
			}
			
			if (!templine.startsWith("::")){
				fullData += templine;
			}
			else{
				
				//parsePrevious question
				if (questionCount != 0){
					haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);
				}
				
				fullData = templine.substring(2);
				questionCount++;
			}
			
		}
		
		haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);
		
		if (br.readLine() != null){
			exit("Two many questions provided.");
		}
		
		if (!haveFinalJeopardy){
			exit("This game file does not have a final jeopardy question.");
		}
	}
	
	private Boolean parseQuestions(String line, Boolean haveFinalJeopardy){
		
		Boolean finalJeopardy = haveFinalJeopardy;
		
		if(line.toLowerCase().startsWith("fj")){
			
			if (haveFinalJeopardy){
				exit("Cannot have more than one final jeopardy question.");
			}
			else{
				
				parseFinalJeopardy(line);
				finalJeopardy = true;
			}
			
		}
		else{
			parseQuestionString(line);
		}
		return finalJeopardy;
	}
	
	private void parseFinalJeopardy(String finalJeopardyString){
		
		String [] questionData = finalJeopardyString.split("::");
		
		if (questionData.length != 3) exit("Too much or not enough data provided for the final jeopardy question.");
		
		if (questionData[1].trim().equals("")) exit("The Final Jeopardy question cannot be whitespace");
		
		if (questionData[2].trim().equals("")) exit("The Final Jeopardy answer cannot be whitespace");
		
		finalJeopardyQuestion = questionData[1].trim();
		finalJeopardyAnswer = questionData[2].trim();

	}
	
	//does not check whether there is a duplicate category/point value question
	private void parseQuestionString(String question){
		
		String [] questionData = question.split("::");
		
		if (questionData.length != 4){
			exit("Too much or not enough data provided for this question");
		}
		else{
			
			String category = questionData[0].trim();
			
			if (!categoriesMap.containsKey(category.toLowerCase())) exit("This category does not exist: "+category);
			
			Integer pointValue = -1;
			
			try{
				pointValue = Integer.parseInt(questionData[1].trim());	
			}
			catch (NumberFormatException nfe){
				exit("The point value cannot be a String.");
			}
			
			if (!pointValuesMapToIndex.containsKey(pointValue)) exit("This point value does not exist: "+pointValue);
			
			int indexX = categoriesMap.get(category.toLowerCase().trim()).getIndex();
			int indexY = pointValuesMapToIndex.get(pointValue);
			
			if (questionData[2].trim().equals("")) exit("The question cannot be whitespace.");
			
			if (questionData[3].trim().equals("")) exit("The answer cannot be whitespace.");
			
			questions[indexX][indexY] = new QuestionAnswer(questionData[2].trim(), questionData[3].trim());
		}
	}
	
	public String[] getCategories() {
		String[] catReturn = new String[5];
		for(String cat : categoriesMap.keySet())
			catReturn[categoriesMap.get(cat).getIndex()]  = cat;
		return catReturn;
	}
	
	public int[] getValues() {
		int[] valReturn = new int[5];
		for(int val : pointValuesMapToIndex.keySet())
			valReturn[pointValuesMapToIndex.get(val)] = val;
		return valReturn;
	}

	public int getValIndex(int points) {
		return pointValuesMapToIndex.get(points);
	}

	
	protected class Category{
		
		private String category;
		private int index;
		
		public Category(String category, int index){
			this.category = category;
			this.index = index;
		}
		
		public String getCategory(){
			return category;
		}
		
		public int getIndex(){
			return index;
		}
	}
}
