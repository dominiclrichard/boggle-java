package views;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Boggle;
import model.DiceTray;

//DOMINIC RICHARD

public class BoggleGUI extends Application {

	public static void main(String[] args) throws FileNotFoundException {
		launch(args);
	}

	private Button startButton = new Button("New Game");
	private Button endButton = new Button("End Game");
	private Label enterAttempts = new Label("Enter Attempts:");
	private Label results = new Label("Results:");
	private DiceTray tray;
	private TextArea attemptsField = new TextArea();
	private TextArea boardField = new TextArea();
	private TextArea resultsField = new TextArea();
	GridPane pane;

	private void layoutGUI() {
		pane = new GridPane();

		GridPane buttonPane = new GridPane();

		pane.setHgap(10);
		pane.setVgap(10);

		buttonPane.add(startButton, 1, 1);
		buttonPane.add(endButton, 2, 1);

		pane.add(buttonPane, 1, 1);
		buttonPane.setHgap(20);
		buttonPane.setMaxHeight(20);

		pane.add(enterAttempts, 2, 1);
		pane.add(attemptsField, 2, 2);
		pane.add(boardField, 1, 2);
		pane.add(results, 3, 1);
		pane.add(resultsField, 3, 2);

		boardField.setMaxWidth(320);
		attemptsField.setMaxWidth(320);
		resultsField.setMaxWidth(520);
		resultsField.setMinWidth(520);

		boardField.setMinHeight(320);
		attemptsField.setMinHeight(320);
		resultsField.setMinHeight(320);

		boardField.setMaxHeight(320);
		attemptsField.setMaxHeight(320);
		resultsField.setMaxHeight(320);
		resultsField.setEditable(false);

		attemptsField.setStyle("-fx-border-color: red; -fx-border-width: 2");
		// boardField.setStyle("-fx-border-color: green; -fx-border-width: 2");
		// resultsField.setStyle("-fx-border-color: blue; -fx-border-width: 2");

		Font font = new Font("American Typewriter", 20);

		Font font2 = new Font("Courier New", 30);

		Font font3 = new Font("American Typewriter", 15);

		boardField.setFont(font2);

		enterAttempts.setFont(font);
		results.setFont(font);

		attemptsField.setWrapText(true);
		attemptsField.setFont(font3);

	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Boggle");

		layoutGUI();
		registerHandlers();

		var scene = new Scene(pane, 1200, 380);
		stage.setScene(scene);
		stage.show();

	}

	private void registerHandlers() {
		startButton.setOnAction(new StartHandler());
		endButton.setOnAction(new EndHandler());

	}

	private class StartHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			tray = new DiceTray();

			boardField = new TextArea("\n\n" + tray.toString());
			attemptsField = new TextArea();
			attemptsField.setPromptText("Type found words, press \"End Game\" for score...");
			pane.add(attemptsField, 2, 2);
			pane.add(boardField, 1, 2);

			attemptsField.setMaxWidth(320);
			attemptsField.setMaxHeight(320);
			attemptsField.setMinHeight(320);
			attemptsField.setMinWidth(320);
			attemptsField.setStyle("-fx-border-color: red; -fx-border-width: 2");
			attemptsField.setWrapText(true);

			boardField.setMinHeight(320);
			boardField.setMaxWidth(320);
			boardField.setMaxHeight(320);
			boardField.setMinWidth(320);
			boardField.setEditable(false);
			
			resultsField = new TextArea();
			pane.add(resultsField, 3, 2);
			resultsField.setMinHeight(320);
			resultsField.setMaxWidth(520);
			resultsField.setMinWidth(520);
			resultsField.setMaxHeight(320);
			resultsField.setEditable(false);

			Font font3 = new Font("American Typewriter", 18);
			attemptsField.setFont(font3);

			Font font2 = new Font("Courier New", 30);
			boardField.setFont(font2);
		}

	}

	private class EndHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			attemptsField.setEditable(false);

			ArrayList<String> boggleWords;
			try {
				boggleWords = dictWords("BoggleWords.txt");
			} catch (FileNotFoundException e) {
				return;
			}

			ArrayList<String> dict = new ArrayList<>();

			for (int i = 0; i < boggleWords.size(); i++) {
				String upperCaseGuess = boggleWords.get(i).toUpperCase();
				dict.add(upperCaseGuess);
			}

			ArrayList<String> guesses = getGuesses();

			String results = getStr(dict, guesses);

			resultsField = new TextArea(results);
			pane.add(resultsField, 3, 2);
			resultsField.setMinHeight(320);
			resultsField.setMaxWidth(520);
			resultsField.setMinWidth(520);
			resultsField.setMaxHeight(320);
			resultsField.setEditable(false);
			resultsField.setWrapText(true);
			resultsField.setStyle("-fx-border-color: red; -fx-border-width: 2");

		}

		private ArrayList<String> getGuesses() {
			ArrayList<String> guesses = new ArrayList<String>();

			String guessStr = attemptsField.getText();
			String[] attempts = guessStr.split(" ");

			for (int i = 0; i < attempts.length; i++) {
				guesses.add(attempts[i].replace("\n", "").toUpperCase());
			}

			return guesses;
		}

		private ArrayList<String> dictWords(String fileName) throws FileNotFoundException {
			ArrayList<String> boggleWords = new ArrayList<>();
			Scanner scanner = new Scanner(new File(fileName));
			while (scanner.hasNext()) {
				boggleWords.add(scanner.nextLine());
			}
			return boggleWords;
		}

		private String getStr(ArrayList<String> dict, ArrayList<String> guesses) {
			Boggle boggle = new Boggle(dict);

			boggle.wordsOnBoard(tray);
			boggle.getFound(guesses);

			ArrayList<String> foundWords = boggle.returnFound();
			ArrayList<String> missedWords = boggle.returnMissed();
			ArrayList<String> wrongWords = boggle.returnWrong();
			int points = boggle.getScore();
			int amountMissed = boggle.returnNumMissed();
			
			Collections.sort(foundWords);
			Collections.sort(missedWords);
			Collections.sort(wrongWords);

			String results = "Your Score: " + points + "\n\nWords you found:\n";

			String foundStr = "";
			for (int i = 0; i < foundWords.size(); i++) {
				foundStr += (foundWords.get(i).toLowerCase()) + " ";
			}

			results += foundStr + "\n\nIncorrect Words:\n";

			String wrongStr = "";
			for (int i = 0; i < wrongWords.size(); i++) {
				wrongStr += (wrongWords.get(i).toLowerCase()) + " ";
			}

			results += wrongStr + "\n\nYou could have found " + amountMissed + " more words:\n";

			String missedStr = "";
			for (int i = 0; i < missedWords.size(); i++) {
				missedStr += (missedWords.get(i).toLowerCase()) + " ";
			}

			results += missedStr;
			return results;
		}

	}

}
