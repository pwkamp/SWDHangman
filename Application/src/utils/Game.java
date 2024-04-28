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

    private ImageView imageView;

    private Text revealedWord;

    private boolean isLeader;

    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private ArrayList<String> guessedLetters = new ArrayList<>();

    int wordLength;
    private int stage = 0;

    public Game(ArrayList<Text> players, ArrayList<Button> letters, ImageView imageView, Text revealedWord, boolean isLeader) {
        this.players = players;
        this.letters = letters;
        this.imageView = imageView;
        this.revealedWord = revealedWord;
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

            }
            else if (message.split(" ")[0].equals("ENDGAME")) {
                Debugger.debug("Game Ended");
                return false;
            } else {
                Debugger.debug("Invalid message: " + message);
            }
        }
    }

    private void gameLoop() {
        while (true) {
            String message = client.awaitMessage();
            String[] messageArray = message.split(" ");
            if (messageArray[0].equals("YOURTURN")) {
                Debugger.debug("My Turn");

                for (Button letter : letters) {
                    if (!guessedLetters.contains(letter.getText())) {
                        letter.setDisable(false);
                    }
                }
            } else if (messageArray[0].equals("CORRECTLETTER")) {
                guessedLetters.add(messageArray[1]);
                int index = alphabet.indexOf(messageArray[1]);
                letters.get(index).setTextFill(Color.GREEN);
                letters.get(index).setDisable(true);
                String formattedRevealedWord = messageArray[2].replace("", " ").trim();
                revealedWord.setText(formattedRevealedWord);

                for (Button letter : letters) {
                    letter.setDisable(true);
                }

            } else if (messageArray[0].equals("INCORRECTLETTER")) {
                guessedLetters.add(messageArray[1]);
                int index = alphabet.indexOf(messageArray[1]);
                letters.get(index).setTextFill(Color.RED);
                letters.get(index).setDisable(true);
                stage++;
                if (stage > 7) {
                    Debugger.debug("You lose this round");
                    for (Button letter : letters) {
                        letter.setDisable(true);
                    }
                } else {
                    imageView.setImage(new javafx.scene.image.Image("res/" + stage + ".png"));
                }
            } else if (messageArray[0].equals("ENDGAME")) {
                Debugger.debug("Game Ended");
                break;
            }
            else {
                Debugger.debug("Invalid message: " + message);
            }
        }
    }
}
