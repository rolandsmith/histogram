package histogram;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes input.txt and pulls/formats the amount of occurrences
 * of words and pushes the results to output.txt
 */
public class histogram {
	//creating a HashMap to store keys and values
	private static Map<String, Integer> histogramMap = new HashMap<>();
	private static String inputFileLocation = "src\\histogram\\input.txt";
	private static String outputFileLocation = "src\\histogram\\output.txt";
	private static ArrayList<String> list = new ArrayList<String>();
	
	public static void main(String args[]){
		try {
		//pulls the text from input.txt into a HashMap
		int maxWordLength = pullInput();
		
		//parses the HashMap and sorts/formats it
		parseMap(maxWordLength);
		
		//prints the Output, scrubbing out X used for sorting during parseMap
		pushOutput();
		}
		catch(Error e){
			System.out.println("Error: IOException");
		}
	}

	/**
	 * Returns maxWordLength after pulling words from input.txt and inserting 
	 * them into the HashMap
	 * 
	 * @return	the largest word's length
	 */
	private static int pullInput() {
			try {
				//Making a file and scanner
				File inputFile = new File(inputFileLocation);
				Scanner input = new Scanner(inputFile);
				
				//setting punctuation to ignore
				String punctuation = ".,/\\!@#$%^&*(;)`~+'=-_|?{}[]><\"";  
				
				//used to add proper formatting
				int maxWordLength = 0;
				
				//running through file
				while(input.hasNext()){
					String word = input.next();
					
					//if the word has a ", remove it before checking the end of the word e.x. "what?"
					if(word.charAt(word.length()-1) == '"') {
						if(punctuation.contains("" + word.charAt(word.length()-1))){
							word = word.substring(0,word.length()-1);
						}
					}
					
					//if the word has punctuation at the end, substring the punctuation off
					if(punctuation.contains("" + word.charAt(word.length()-1))){
						word = word.substring(0,word.length()-1);
					}
					
					//if the word has punctuation at the beginning, substring the front off
					if(punctuation.contains("" + word.charAt(0))){
						word = word.substring(1,word.length());
					}
					
					word = word.toLowerCase();
					
					if(word.length() > maxWordLength){
						maxWordLength = word.length();
					}
					
					//adding words to HashMap, incrementing if already existing in Map
					Integer checker = histogramMap.putIfAbsent(word, 1);
					if(checker != null){
						int amount = histogramMap.get(word);
						histogramMap.put(word, amount+1);
					}
					
				}
				
				//Closing Input
				input.close();
				return maxWordLength;
			}
			catch(IOException e){
				System.out.println("Error: File not Found");
				return -1;
			}
	}
	
	/**
	 * Lambda iterates through HashMap and adds correct formating to results, storing results into
	 * ArrayList list
	 * 
	 * @param finalMaxWordLength	the HashMap's largest word's length
	 */
	//Lambda iterating through Map to add correct formatting to results and store in ArrayList list
	private static void parseMap(final int finalMaxWordLength){
		histogramMap.forEach((word, amount) -> {
			
			//deals with equal sign amounts
			String equalSigns = "";
			for(int i = 0; i < amount; i++){
				equalSigns += "=";
			}
			
			//adds spaces for to account for longer words
			String formatSpaces = "";
			int loopNum = finalMaxWordLength - word.length();
			for(int i = 0; i < loopNum; i++){
				formatSpaces += " ";
			}
			
			list.add(amount + "X" + formatSpaces + word + " | " + equalSigns + " (" + amount + ")");
			});
		
		//sorts ArrayList in reverse order so the most word occurrences are at the top, in Java 6 O(NLog(N)), Java 7 O(N)
		Collections.sort(list, Collections.reverseOrder());
	}
	
	/**
	 * Writes contents of ArrayList list to a file after taking out the sorting number and X
	 * 
	 */
	private static void pushOutput(){
		try{
			//opening output file
			File outputFile = new File(outputFileLocation);
			
			FileWriter outputWriter = new FileWriter(outputFile);
			
			BufferedWriter output = new BufferedWriter(outputWriter);
			
			//removing the amount and 'X' in the front of the contents of list and writing to output.txt
			for(int i = 0; i < list.size()-1; i++){
				for(int j = 0; j < list.get(i).length()-1; j++){
					if(list.get(i).charAt(j) == 'X'){
						output.write(list.get(i).substring(j+1,list.get(i).length()));
						output.newLine();
					}
				}
			}
			
			//closing Writers
			output.close();
			outputWriter.close();
		}
		catch(IOException e){
			System.out.println("Error: IOException");
		}
	}
	
}
