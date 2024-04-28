package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

import utils.Client;
import utils.Debugger;

public class GameSelection {
    private Client client = Client.getInstance();

    @FXML
    Button logout;

    @FXML
    Button joinGame;

    @FXML
    TextField gameCode;

    @FXML
    Button createGame;

    @FXML
    Text username;

    @FXML
    Text coins;

    @FXML
    public void initialize() {
        Debugger.debug("Game Selection Initialized");

        setUsername();
        setCoins();

        logout.setOnAction(actionEvent -> logoutClicked());
        joinGame.setOnAction(actionEvent -> joinGameClicked());
        createGame.setOnAction(actionEvent -> createGameClicked());

    }

    private void logoutClicked() {
        System.exit(0);
        try {
            //TODO: Add any needed functionality to logout that does not involve JavaFX scene switching back to the Main Menu
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/MainMenu.fxml")));
            // Get the current window
            Stage currentStage = (Stage) logout.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    private void joinGameClicked() {

        Debugger.debug("Join Game Clicked");
        //TODO: Check if the game code is valid from the server
        if (validateGameCode()) {
            Debugger.debug("Game Code is valid");
            client.sendData("JOIN " + gameCode.getText() + " " + client.getUsername());
            client.awaitMessage();
            String message = client.getMessage();
            if (message.contains("JOIN success")) {
                Debugger.debug("Joining game");
                joinGame();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Game Code");
                alert.setHeaderText("Game does not exist or is not active");
                alert.showAndWait();
            }
        }
    }

    private void createGameClicked() {
        Debugger.debug("Create Game Clicked");
        client.sendData("CREATEGAME");
        client.awaitMessage();
        String message = client.getMessage();
        String[] wordOptions = message.split(" ");
        if (message.contains("CREATEGAME success")) {
            createGame(message, new String[]{wordOptions[3], wordOptions[4], wordOptions[5]});
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Creating Game");
            alert.setHeaderText("Error creating game");
            alert.showAndWait();
        }
    }

    private boolean validateGameCode() {
        if (gameCode.getText().length() == 6) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Game Code");
            alert.setHeaderText("Game Code must be 6 characters long");
            alert.showAndWait();
            return false;
        }
    }


    private void joinGame() {
        try {
            //TODO: Add any needed functionality to logout that does not involve JavaFX scene switching back to the Main Menu
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/GameMain.fxml")));
            // Get the current window
            Stage currentStage = (Stage) joinGame.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    private void createGame(String message, String[] wordOptions) {
        String gameCode = client.getMessage().split(" ")[2];
        Debugger.debug("Game Code: " + gameCode);
        client.setGameCode(gameCode);
        client.setWordOptions(wordOptions);
        try {
            //TODO: Add any needed functionality to logout that does not involve JavaFX scene switching back to the Main Menu
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/GameLeader.fxml")));
            // Get the current window
            Stage currentStage = (Stage) joinGame.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    private void setCoins() {
        //TODO: Get the user's coins from the server and set the text to the number of coins
        client.sendData("COINS " + client.getUsername());
        client.awaitMessage();
        coins.setText(client.getMessage().split(" ")[1]);
    }

    private void setUsername() {
        //TODO: Get the user's username from the server and set the text to the username
        username.setText(client.getUsername());
    }
}
