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

public class GameSelectionController {
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
    public void initialize() {
        Debugger.debug("Game Selection Initialized");

        setUsername();

        logout.setOnAction(actionEvent -> logoutClicked());
        joinGame.setOnAction(actionEvent -> joinGameClicked());
        createGame.setOnAction(actionEvent -> createGameClicked());

    }

    private void logoutClicked() {
        try {
            client.closeConnection();
            //TODO: Add any needed functionality to logout that does not involve JavaFX scene switching back to the Main Menu
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/ServerSelect.fxml")));
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
            client.sendData("JOIN " + gameCode.getText() + " " + client.getUser().getUsername());
            String message = client.awaitMessage();
            if (message.contains("success")) {
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
        String message = client.awaitMessage();
        if (message.equals("success")) {
            createGame();
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
            //TODO: Add any needed functionality to that does not involve JavaFX scene switching back to the Main Menu
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

    private void createGame() {
        String[] message = client.awaitMessage().split(" ");
        String gameCode = message[0];
        String[] wordOptions = {message[1], message[2], message[3]};

        Debugger.debug("Game Code: " + gameCode);
        client.setGameCode(gameCode);
        client.setWordOptions(wordOptions);
        try {
            //TODO: Add any needed functionality that does not involve JavaFX scene switching back to the Main Menu
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

    private void setUsername() {
        //TODO: Get the user's username from the server and set the text to the username
        username.setText(client.getUser().getUsername());
    }
}
