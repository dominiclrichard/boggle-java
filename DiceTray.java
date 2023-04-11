package model;

import java.util.ArrayList;
import java.util.Random;

//rich mercer, Dominic Richard

public class DiceTray {

	private char[][] path;
	private char[][] theBoard;
	public static final char TRIED = '@';
	public static final char PART_OF_WORD = '!';
	private String attempt;
	private int index;
	public static final int SIZE = 4;
	public static final int NUMBER_SIDES = 6;
	private ArrayList<Dice> dieList = new ArrayList<>();

	/**
	 * Construct a tray of dice using a hard coded 2D array of chars. Use this for
	 * testing
	 * 
	 * @param newBoard The 2D array of characters used in testing
	 */
	public DiceTray(char[][] newBoard) {
		theBoard = newBoard;
	}

	// TODO
	public DiceTray() {
		theBoard = new char[4][4];
		dieList.add(new Dice('L', 'R', 'Y', 'T', 'T', 'E'));
		dieList.add(new Dice('A', 'N', 'A', 'E', 'E', 'G'));
		dieList.add(new Dice('A', 'F', 'P', 'K', 'F', 'S'));
		dieList.add(new Dice('Y', 'L', 'D', 'E', 'V', 'R'));
		dieList.add(new Dice('V', 'T', 'H', 'R', 'W', 'E'));
		dieList.add(new Dice('I', 'D', 'S', 'Y', 'T', 'T'));
		dieList.add(new Dice('X', 'L', 'D', 'E', 'R', 'I'));
		dieList.add(new Dice('Z', 'N', 'R', 'N', 'H', 'L'));
		dieList.add(new Dice('E', 'G', 'H', 'W', 'N', 'E'));
		dieList.add(new Dice('O', 'A', 'T', 'T', 'O', 'W'));
		dieList.add(new Dice('H', 'C', 'P', 'O', 'A', 'S'));
		dieList.add(new Dice('N', 'M', 'I', 'H', 'U', 'Q'));
		dieList.add(new Dice('S', 'E', 'O', 'T', 'I', 'S'));
		dieList.add(new Dice('M', 'T', 'O', 'I', 'C', 'U'));
		dieList.add(new Dice('E', 'N', 'S', 'I', 'E', 'U'));
		dieList.add(new Dice('O', 'B', 'B', 'A', 'O', 'J'));

		Random rand = new Random();

		int i = 0;
		int j = 0;

		while (!dieList.isEmpty()) {
			int randInt = rand.nextInt(dieList.size());
			theBoard[i][j] = dieList.remove(randInt).getRandSide();
			j += 1;
			if (j == 4) {
				j = 0;
				i += 1;
			}
		}

	}

	/**
	 * Return true if search is word that can found on the board following the rules
	 * of Boggle
	 * 
	 * @param str A word that may be in the board by connecting consecutive letters
	 * @return True if search is found
	 */
	public boolean found(String str) {
		if (str.length() < 3)
			return false;
		attempt = str.toUpperCase().trim();
		boolean found = false;
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++)
				if (theBoard[r][c] == attempt.charAt(0)) {
					init();
					found = recursiveSearch(r, c);
					if (found) {
						return true;
					}
				}
		}
		return found;
	}

	// Keep a 2nd 2D array to remember the characters that have been tried
	private void init() {
		path = new char[SIZE][SIZE];
		for (int r = 0; r < SIZE; r++)
			for (int c = 0; c < SIZE; c++)
				path[r][c] = '.';
		index = 0;
	}

	// This is like the Obstacle course escape algorithm.
	// Now we are checking 8 possible directions (no wraparound)
	private boolean recursiveSearch(int r, int c) {
		boolean found = false;

		if (valid(r, c)) { // valid returns true if in range AND one letter was found
			path[r][c] = TRIED;
			if (theBoard[r][c] == 'Q')
				index += 2;
			else
				index++;

			// Look in 8 directions for the next character
			if (index >= attempt.length())
				found = true;
			else {
				found = recursiveSearch(r - 1, c - 1);
				if (!found)
					found = recursiveSearch(r - 1, c);
				if (!found)
					found = recursiveSearch(r - 1, c + 1);
				if (!found)
					found = recursiveSearch(r, c - 1);
				if (!found)
					found = recursiveSearch(r, c + 1);
				if (!found)
					found = recursiveSearch(r + 1, c - 1);
				if (!found)
					found = recursiveSearch(r + 1, c);
				if (!found)
					found = recursiveSearch(r + 1, c + 1);
				// If still not found, allow backtracking to use the same letter in a
				// different location later as in looking for "BATTLING" in this board
				//
				// L T T X // Mark leftmost T as untried after it finds a 2nd T but not the L.
				// I X A X
				// N X X B
				// G X X X
				//
				if (!found) {
					path[r][c] = '.'; // Rick used . to mark the 2nd 2D array as TRIED
					index--; // 1 less letter was found. Let algorithm find the right first (col 2)
				}
			} // End recursive case

			if (found) {
				// Mark where the letter was found. Not required, but could be used to
				// show the actual path of the word that was found.
				path[r][c] = theBoard[r][c];
			}
		}
		return found;
	}

	// Determine if a current value of row and columns can or should be tried
	private boolean valid(int r, int c) {
		return r >= 0 && r < SIZE && c >= 0 && c < SIZE && path[r][c] != TRIED
				&& theBoard[r][c] == attempt.charAt(index);
	}

	@Override
	public String toString() {
		String retStr = "  ";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (theBoard[i][j] == 'Q') {
					retStr += "Qu ";
				} else {
					retStr += theBoard[i][j] + "  ";
				}
				if (j == 3) {
					retStr += "\n  ";
				}
			}
		}
		return retStr;
	}

	private class Dice {

		char[] sides;

		private Dice(char sOne, char sTwo, char sThree, char sFour, char sFive, char sSix) {
			sides = new char[6];
			sides[0] = sOne;
			sides[1] = sTwo;
			sides[2] = sThree;
			sides[3] = sFour;
			sides[4] = sFive;
			sides[5] = sSix;
		}

		private char getRandSide() {
			Random rand = new Random();
			return sides[rand.nextInt(6)];
		}

	}

}