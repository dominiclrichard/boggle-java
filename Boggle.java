package model;
import java.util.ArrayList;

//Dominic Richard

public class Boggle {
	
	public ArrayList<String> foundWords;
	public ArrayList<String> wrongWords;
	public ArrayList<String> missedWords;
	public int score = 0;
	private ArrayList<String> dictionary;
	private ArrayList<String> foundOnBoard;
	public int amountMissed;
	
	public Boggle(ArrayList<String> dictionary) {
		this.dictionary = dictionary;
		foundWords = new ArrayList<>();
		missedWords = new ArrayList<>();
		wrongWords = new ArrayList<>();
		foundOnBoard = new ArrayList<>();
	}
	
	public Boggle(ArrayList<String> dictionary, char[][] charList, ArrayList<String> guess) {
		this.dictionary = dictionary;
		foundWords = new ArrayList<>();
		missedWords = new ArrayList<>();
		wrongWords = new ArrayList<>();
		foundOnBoard = new ArrayList<>();
		
		DiceTray tray = new DiceTray(charList);
		
		wordsOnBoard(tray);
		
		System.out.println(guess);
		
		getFound(guess);
	}
	
	public int getScore() {
		for (int i = 0; i < foundWords.size(); i ++) {
			if (foundWords.get(i).length() == 3) {
				score += 1;
			}
			
			if (foundWords.get(i).length() == 4) {
				score += 1;
			}
			
			if (foundWords.get(i).length() == 5) {
				score += 2;
			}
			
			if (foundWords.get(i).length() == 6) {
				score += 3;
			}
			
			if (foundWords.get(i).length() == 7) {
				score += 5;
			}
			
			if (foundWords.get(i).length() >= 8) {
				score += 11;
			}
				
		}
		return score;
	}
	
	public void wordsOnBoard(DiceTray tray) {
		for (int i = 0; i < dictionary.size(); i ++) {
			if (tray.found(dictionary.get(i))) {
				foundOnBoard.add(dictionary.get(i));
			}
		}
	}
	
	public void getFound(ArrayList<String> guesses) {
		for (int i = 0; i < guesses.size(); i ++) {
			if (foundOnBoard.contains(guesses.get(i)) && !foundWords.contains(guesses.get(i))) {
				foundWords.add(guesses.get(i));
			}
			else {
				if (!foundOnBoard.contains(guesses.get(i))) {
					wrongWords.add(guesses.get(i));
				}
			}
		}
		
		for (int i = 0; i < foundOnBoard.size(); i ++) {
			if (!foundWords.contains(foundOnBoard.get(i))) {
				missedWords.add(foundOnBoard.get(i));
				
			}
		}
		amountMissed = missedWords.size();
	}
	
	public ArrayList<String> returnFound(){
		return foundWords;
	}
	
	public ArrayList<String> returnMissed(){
		return missedWords;
	}
	
	public ArrayList<String> returnWrong(){
		return wrongWords;
	}
	
	public int returnNumMissed() {
		return amountMissed;
	}

}
