package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import utils.Client;
import utils.Debugger;
import utils.Game;
import utils.LetterEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: Everything
public class GameLeaderController {
    private Client client = Client.getInstance();


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
    private Button startGameButton;

    @FXML
    private Button word1Button;

    @FXML
    private Button word2Button;

    @FXML
    private Button word3Button;

    @FXML
    private Text revealedWordText;

    private ArrayList<Button> letterButtons;

    private ArrayList<Text> players = new ArrayList<>();

    ExecutorService executorService = Executors.newCachedThreadPool();
    Game game;

    @FXML
    public void initialize() {
        setLetterButtons();
        setLetterButtonActions();

        for (Button button : letterButtons) {
            button.setDisable(true);
        }
        setupLeaderFunctions();
        setPlayers();
        setPlayersText();

        usernameText.setText(client.getUser().getUsername());

        leaveGameButton.setOnAction(actionEvent -> leaveGame());
        game = new Game(players, letterButtons, hangmanImageView, revealedWordText, hangmanText, true);
        executorService.execute(game);
    }

    private void setupLeaderFunctions() {
        startGameButton.setText("Start Game: " + client.getGameCode());
        startGameButton.setOnAction(actionEvent -> startGameClicked());
        startGameButton.setDisable(true);

        word1Button.setOnAction(actionEvent -> word1ButtonClicked());
        word2Button.setOnAction(actionEvent -> word2ButtonClicked());
        word3Button.setOnAction(actionEvent -> word3ButtonClicked());

        word1Button.setText(client.getWordOptions()[0]);
        word2Button.setText(client.getWordOptions()[1]);
        word3Button.setText(client.getWordOptions()[2]);
    }

    private void startGameClicked() {
        Debugger.debug("Start Game Clicked");
        client.sendData("START");
        startGameButton.setDisable(true);
    }

    private void word1ButtonClicked() {
        Debugger.debug("Word 1 Clicked");
        client.sendData("WORD 1");
        disableWordButtons();
        startGameButton.setDisable(false);
    }

    private void word2ButtonClicked() {
        Debugger.debug("Word 2 Clicked");
        client.sendData("WORD 2");
        disableWordButtons();
        startGameButton.setDisable(false);
    }

    private void word3ButtonClicked() {
        Debugger.debug("Word 3 Clicked");
        client.sendData("WORD 3");
        disableWordButtons();
        startGameButton.setDisable(false);
    }

    private void disableWordButtons() {
        word1Button.setDisable(true);
        word2Button.setDisable(true);
        word3Button.setDisable(true);
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
        client.sendData("ENDGAME " + client.getUser().getUsername());
        client.closeConnection();
        try {
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

    }

    //TODO: Implement setCoins server / client functionality
    private void setCoins() {

    }

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
