package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class Game implements Runnable {
    Client client = Client.getInstance();

    private ArrayList<Text> players;
    private ArrayList<Button> letters;

    private Button wordGuessButton;

    private ImageView imageView;

    private Text revealedWord;

    private Text hangmanText;

    private boolean isLeader;

    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private ArrayList<String> guessedLetters = new ArrayList<>();

    int wordLength;
    private int stage = 0;

    // Constructor for GameMainController
    public Game(ArrayList<Text> players, ArrayList<Button> letters, Button wordGuessButton, ImageView imageView, Text revealedWord, Text hangmanText, boolean isLeader) {
        this.players = players;
        this.letters = letters;
        this.wordGuessButton = wordGuessButton;
        this.imageView = imageView;
        this.revealedWord = revealedWord;
        this.hangmanText = hangmanText;
        this.isLeader = isLeader;
    }

    // Constructor for GameLeaderController
    public Game(ArrayList<Text> players, ArrayList<Button> letters, ImageView imageView, Text revealedWord, Text hangmanText, boolean isLeader) {
        this.players = players;
        this.letters = letters;
        this.imageView = imageView;
        this.revealedWord = revealedWord;
        this.hangmanText = hangmanText;
        this.isLeader = isLeader;
    }
    @Override
    public void run() {
        awaitGameStart();
        Debugger.debug("Game Started");
        gameLoop();
    }

    public boolean awaitGameStart() {
        //client.sendData("WAITING");
        //players.get(0).setText(client.getUsername());
        while (true) {
            String message = client.awaitMessage();
            String[] messageArray = message.split(" ");
            if (messageArray[0].equals("START")) {
                wordLength = Integer.parseInt(messageArray[1]);
                StringBuilder wordLengthPlaceholder = new StringBuilder();
                wordLengthPlaceholder.append("_ ".repeat(Math.max(0, wordLength)));
                revealedWord.setText(wordLengthPlaceholder.toString());
                return true;
            } else if (messageArray[0].equals("WORD") && messageArray[1].equals("success")) {
                Debugger.debug("Word Set");
                wordLength = Integer.parseInt(messageArray[2]);
                StringBuilder wordLengthPlaceholder = new StringBuilder();
                wordLengthPlaceholder.append("_ ".repeat(Math.max(0, wordLength)));
                revealedWord.setText(wordLengthPlaceholder.toString());

            } else if (messageArray[0].equals("PLAYER")) {
                players.get(Integer.parseInt(messageArray[1])).setText(messageArray[2]);
            } else if (message.split(" ")[0].equals("ENDGAME")) {
                Debugger.debug("Game Ended");
                return false;
            } else {
                Debugger.debug("Invalid message: " + message);
            }
        }
    }

    private void gameLoop() {
        while (true) {
            String[] message = client.awaitMessage().split(" ");

            if (message[0].equals("YOURTURN")) {
                Debugger.debug("My Turn");

                for (Button letter : letters) {
                    if (!guessedLetters.contains(letter.getText())) {
                        letter.setDisable(false);
                    }
                }
                if (!isLeader) wordGuessButton.setDisable(false);
            } else if (message[0].equals("CORRECTLETTER")) {
                guessedLetters.add(message[1]);
                int index = alphabet.indexOf(message[1]);
                letters.get(index).setTextFill(Color.GREEN);
                letters.get(index).setDisable(true);
                String formattedRevealedWord = message[2].replace("", " ").trim();
                revealedWord.setText(formattedRevealedWord);

                for (Button letter : letters) {
                    letter.setDisable(true);
                }
                if (!isLeader) wordGuessButton.setDisable(true);

            } else if (message[0].equals("INCORRECTLETTER")) {
                guessedLetters.add(message[1]);
                int index = alphabet.indexOf(message[1]);
                letters.get(index).setTextFill(Color.RED);
                letters.get(index).setDisable(true);
                stage++;
                if (stage > 7) {
                    Debugger.debug("You lose this round");
                    lose();
                } else {
                    imageView.setImage(new javafx.scene.image.Image("res/" + stage + ".png"));
                }
            } else if (message[0].equals("INCORRECTWORD")) {
                if (client.getUser().getUsername().equals(message[1])) {
                    Debugger.debug("You lose this round");
                    lose();
                }

            } else if (message[0].equals("WINNER")) {
                Debugger.debug("Winner: " + message[1]);
                win();
            } else if (message[0].equals("ENDGAME")) {
                Debugger.debug("Game Ended");
                break;
            }
            else {
                Debugger.debug("Invalid message: " + message);
            }
        }
    }

    public void lose() {
        for (Button letter : letters) {
            letter.setDisable(true);
        }
        if (!isLeader) wordGuessButton.setDisable(true);

        hangmanText.setText("You Lose");
        hangmanText.setFill(Color.RED);
    }

    public void win() {
        for (Button letter : letters) {
            letter.setDisable(true);
        }
        if (!isLeader) wordGuessButton.setDisable(true);

        hangmanText.setText("You Win!");
        hangmanText.setFill(Color.GREEN);
    }

    public void reset() {
        for (Button letter : letters) {
            letter.setDisable(true);
        }
        if (!isLeader) wordGuessButton.setDisable(true);

        hangmanText.setText("Hangman");
        hangmanText.setFill(Color.BLACK);
    }
}
