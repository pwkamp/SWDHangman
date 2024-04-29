package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import utils.Client;
import utils.Game;
import utils.LetterEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameMainController {
    Client client = Client.getInstance();

    @FXML
    private Text usernameText;

    @FXML
    private Text hangmanText;

    @FXML
    private Text playersText;

    @FXML
    private Text player1Text = new Text();

    @FXML
    private Text player2Text = new Text();

    @FXML
    private Text player3Text= new Text();

    @FXML
    private Text player4Text = new Text();

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private Button aButton, bButton, cButton, dButton, eButton, fButton, gButton, hButton, iButton, jButton, kButton, lButton, mButton, nButton, oButton, pButton, qButton, rButton, sButton, tButton, uButton, vButton, wButton, xButton, yButton, zButton;

    @FXML
    private Button leaveGameButton;

    @FXML
    private TextField wordGuessField;

    @FXML
    private Button wordGuessButton;

    @FXML
    private Text revealedWordText;

    private ArrayList<Button> letterButtons;

    private ArrayList<Text> players = new ArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Game game;

    @FXML
    public void initialize() {
        setLetterButtons();
        setLetterButtonActions();

        for (Button button : letterButtons) {
            button.setDisable(true);
        }

        setPlayers();
        setPlayersText();

        usernameText.setText(client.getUser().getUsername());

        leaveGameButton.setOnAction(actionEvent -> leaveGame());
        wordGuessButton.setOnAction(actionEvent -> wordGuessClicked());
        wordGuessButton.setDisable(true);

        game = new Game(players, letterButtons, wordGuessButton, hangmanImageView, revealedWordText, hangmanText, false);
        executorService.execute(game);

    }

    private void wordGuessClicked() {
        String wordGuess = wordGuessField.getText().toLowerCase();
        if (wordGuess.length() < 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Word must be at least 5 characters long");
            alert.showAndWait();
        } else {
            client.sendData("WORD " + wordGuess);
        }
    }

    private void setLetterButtons() {
        letterButtons = new ArrayList<>();
        letterButtons.add(aButton);
        letterButtons.add(bButton);
        letterButtons.add(cButton);
        letterButtons.add(dButton);
        letterButtons.add(eButton);
        letterButtons.add(fButton);
        letterButtons.add(gButton);
        letterButtons.add(hButton);
        letterButtons.add(iButton);
        letterButtons.add(jButton);
        letterButtons.add(kButton);
        letterButtons.add(lButton);
        letterButtons.add(mButton);
        letterButtons.add(nButton);
        letterButtons.add(oButton);
        letterButtons.add(pButton);
        letterButtons.add(qButton);
        letterButtons.add(rButton);
        letterButtons.add(sButton);
        letterButtons.add(tButton);
        letterButtons.add(uButton);
        letterButtons.add(vButton);
        letterButtons.add(wButton);
        letterButtons.add(xButton);
        letterButtons.add(yButton);
        letterButtons.add(zButton);
    }

    private void setLetterButtonActions() {
        for (Button button : letterButtons) {
            button.setOnAction(actionEvent -> new LetterEvent(button.getText().charAt(0)).handle(actionEvent));
        }
    }

    //TODO: Implement leaveGame server / client functionality
    private void leaveGame() {
        try {
            client.sendData("LEAVEGAME " + client.getUser().getUsername());
            client.closeConnection();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/ServerSelect.fxml")));
            // Get the current window
            Stage currentStage = (Stage) leaveGameButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    //TODO: Implement setUsername server / client functionality
    private void setUsername() {
        usernameText.setText(client.getUser().getUsername());
    }

    //TODO: Implement setCoins server / client functionality

    private void setPlayers() {
        players.add(player1Text);
        players.add(player2Text);
        players.add(player3Text);
        players.add(player4Text);
    }


    //TODO: Implement setPlayersText server / client functionality
    private void setPlayersText() {
        for (Text player : players) {
            player.setText("");
        }
    }

}
