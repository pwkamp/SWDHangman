package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import utils.Client;
import utils.Debugger;

public class Login {

    private Client client = Client.getInstance();

    @FXML
    Button back;

    @FXML
    Button login;

    @FXML
    PasswordField password;

    @FXML
    TextField username;

    @FXML
    public void initialize() {
        back.setOnAction(actionEvent -> backClicked());
        login.setOnAction(actionEvent -> loginClicked());
    }

    private void backClicked() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/MainMenu.fxml")));
            // Get the current window
            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    private void loginClicked() {
        //TODO: Implement login functionality and checks to make sure UN and PW are valid and match Database
        Debugger.debug("Login Clicked");

        client.sendData("LOGIN " + username.getText() + " " + password.getText());
        client.awaitMessage();
        String message = client.getMessage();
        if (message.equals("LOGIN success")) {
            Debugger.debug("Login success");
            loginSuccess();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }

    private void loginSuccess() {
        client.setUsername(username.getText());
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/GameSelection.fxml")));
            // Get the current window
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

}
