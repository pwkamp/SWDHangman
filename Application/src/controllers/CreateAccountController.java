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
import utils.User;


public class CreateAccountController {

    Client client = Client.getInstance();

    @FXML
    Button back;

    @FXML
    Button createAccount;

    @FXML
    PasswordField password1;

    @FXML
    PasswordField password2;

    @FXML
    TextField username;

    @FXML
    public void initialize() {
        back.setOnAction(actionEvent -> backClicked());
        createAccount.setOnAction(actionEvent -> createAccountClicked());
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

    private void createAccountClicked() {
        if (validatePassword() && validateUsername()) {
            //TODO: Implement create account server functionality including checking if the username is taken already
            Debugger.debug("Creating account");
            client.sendData("CREATE " + username.getText() + " " + password1.getText());
//            while (!client.getMessage().equals("CREATE success") || !client.getMessage().equals("CREATE fail")) {
//            }
            String message = client.awaitMessage();
            if (message.equals("success")) {
                Debugger.debug("Account created");
                client.setUser(User.deserialize(client.awaitMessage()));
                loginSuccess();
            } else {
                Debugger.debug("Account not created");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(message);
                alert.showAndWait();
            }
        }
    }

    private void loginSuccess() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/GameSelection.fxml")));
            // Get the current window
            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    public boolean validatePassword() {
        if (password1.getText().equals(password2.getText())) {
            Debugger.debug("Passwords match");
            if (password1.getText().length() >= 4) {
                return true;
            } else {
                Debugger.debug("Password must be at least 4 characters long");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password must be at least 4 characters long");
                alert.showAndWait();
                return false;
            }
        } else {
            Debugger.debug("Passwords do not match");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.showAndWait();
            return false;
        }
    }

    private boolean validateUsername() {
        if (username.getText().length() >= 4) {
            return true;
        } else {
            Debugger.debug("Username must be at least 4 characters long");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username must be at least 4 characters long");
            alert.showAndWait();
            return false;
        }
    }
}
