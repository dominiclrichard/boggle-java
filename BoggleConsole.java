package views;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Boggle;
import model.DiceTray;

//Dominic Richard

public class BoggleConsole {

	public static void main(String[] args) throws FileNotFoundException {
		
		ArrayList<String> boggleWords = dictWords("BoggleWords.txt");
		
		ArrayList<String> dictFormatted = new ArrayList<>();
		
		for (int i = 0; i < boggleWords.size(); i ++) {
			String upperCaseGuess = boggleWords.get(i).toUpperCase();
			dictFormatted.add(upperCaseGuess);		
		}
		
		System.out.println("New Boggle Game\n\n");		
		DiceTray tray = new DiceTray();		
		System.out.println(tray);
		ArrayList<String> guesses = getInput();
		guesses.remove("XX");
		
		ArrayList<String> guessesFormatted = new ArrayList<>();
		
		for (int i = 0; i < guesses.size(); i ++) {
			String upperCaseGuess = guesses.get(i).toUpperCase();
			guessesFormatted.add(upperCaseGuess);
			
		}
		
		displayResults(dictFormatted, tray, guessesFormatted);
		
	}
	
	public static ArrayList<String> dictWords(String fileName) throws FileNotFoundException{
		ArrayList<String> boggleWords = new ArrayList<>();
		Scanner scanner = new Scanner(new File(fileName));
		while (scanner.hasNext()) {
			boggleWords.add(scanner.nextLine());
		}
		return boggleWords;
	}
	
	/**
	 * The input stream function for the JUnit
	 * 
	 * @param input
	 * @return
	 */
	public ArrayList<String> getInput(String input) {
		ArrayList<String> retVal = new ArrayList<>();
		String[] enteredVals = input.split(" ");
		for (int i = 0; i < enteredVals.length; i ++) {
			retVal.add(enteredVals[i]);
		}
		return retVal;
	}
	
	private static ArrayList<String> getInput() {
		ArrayList<String> retVal = new ArrayList<>();
		Scanner inputScan = new Scanner(System.in);
		System.out.println("Enter Words or \"XX\" to quit: \n");
		boolean resume = true;
		while (resume) {
			String[] enteredVals = inputScan.nextLine().split(" ");
			for (int i = 0; i < enteredVals.length; i ++) {
				retVal.add(enteredVals[i]);
			}
			if (retVal.get(retVal.size() - 1).equals("XX")) {
				resume = false;
			}
		}
		inputScan.close();
		return retVal;
	}
	
	private static void displayResults(ArrayList<String> dict, DiceTray tray, ArrayList<String> guesses) {
		Boggle boggle = new Boggle(dict);
		boggle.wordsOnBoard(tray);
		boggle.getFound(guesses);
		
		ArrayList<String> foundWords = boggle.returnFound();
		ArrayList<String> missedWords = boggle.returnMissed();
		ArrayList<String> wrongWords = boggle.returnWrong();
		int points = boggle.getScore();
		int amountMissed = boggle.returnNumMissed();
		
		System.out.println("Your Score: " + Integer.toString(points));
		
		System.out.println("Words you found: ");
		System.out.println("================ ");
		String foundStr = "";
		for (int i = 0; i < foundWords.size(); i ++) {
			foundStr += (foundWords.get(i).toLowerCase()) + " ";
			if (i % 10 == 0 && i > 0) {
				foundStr += "\n";
			}
		}
		System.out.println(foundStr);
		
		System.out.println();
		System.out.println("Incorrect Words: ");
		System.out.println("================ ");
		String wrongStr = "";
		for (int i = 0; i < wrongWords.size(); i ++) {
			wrongStr += (wrongWords.get(i).toLowerCase()) + " ";
			if (i % 10 == 0 && i > 0) {
				wrongStr += "\n";
			}
		}
		System.out.println(wrongStr);
		
		System.out.println();
		System.out.println("You could have found " + amountMissed + " more words.");
		System.out.println("====================================");
		
		String missedStr = "";
		for (int i = 0; i < missedWords.size(); i ++) {
			missedStr += (missedWords.get(i).toLowerCase()) + " ";
			if (i % 10 == 0 && i > 0) {
				missedStr += "\n";
			}
		}
		System.out.println(missedStr);
	}
	
	public ArrayList<String> getDictionary(String name) {
		try {
			return dictWords(name);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

}
