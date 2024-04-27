package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import utils.LetterEvent;

import java.util.ArrayList;
import java.util.Objects;

//TODO: Everything
public class GameMain {

    @FXML
    private Text roundText;

    @FXML
    private Text usernameText;

    @FXML
    private Text coinsText;

    @FXML
    private Text playersText;

    @FXML
    private Text player1Text;

    @FXML
    private Text player2Text;

    @FXML
    private Text player3Text;

    @FXML
    private Text player4Text;

    @FXML
    private Text player5Text;

    @FXML
    private Text player6Text;

    @FXML
    private Text player7Text;

    @FXML
    private Text player8Text;

    @FXML
    private Text player9Text;

    @FXML
    private Text player10Text;

    @FXML
    private ImageView hangmanImageView;

    @FXML
    private Button aButton;

    @FXML
    private Button bButton;

    @FXML
    private Button cButton;

    @FXML
    private Button dButton;

    @FXML
    private Button eButton;

    @FXML
    private Button fButton;

    @FXML
    private Button gButton;

    @FXML
    private Button hButton;

    @FXML
    private Button iButton;

    @FXML
    private Button jButton;

    @FXML
    private Button kButton;

    @FXML
    private Button lButton;

    @FXML
    private Button mButton;

    @FXML
    private Button nButton;

    @FXML
    private Button oButton;

    @FXML
    private Button pButton;

    @FXML
    private Button qButton;

    @FXML
    private Button rButton;

    @FXML
    private Button sButton;

    @FXML
    private Button tButton;

    @FXML
    private Button uButton;

    @FXML
    private Button vButton;

    @FXML
    private Button wButton;

    @FXML
    private Button xButton;

    @FXML
    private Button yButton;

    @FXML
    private Button zButton;

    @FXML
    private Button leaveGameButton;

    private ArrayList<Button> letterButtons;

    private ArrayList<Text> players;

    @FXML
    public void initialize() {
        setLetterButtons();
        setLetterButtonActions();
        //setPlayers();
        //setPlayersText();
        leaveGameButton.setOnAction(actionEvent -> leaveGame());
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/GameSelection.fxml")));
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
        players.add(player5Text);
        players.add(player6Text);
        players.add(player7Text);
        players.add(player8Text);
        players.add(player9Text);
        players.add(player10Text);
    }


    //TODO: Implement setPlayersText server / client functionality
    private void setPlayersText() {
        for (Text player : players) {
            player.setText("");
        }
    }

}
