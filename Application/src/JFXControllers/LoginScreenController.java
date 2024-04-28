package JFXControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import utils.Debugger;

public class LoginScreenController {

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

        //For Testing Purposes
        loginSuccess();
    }

    private void loginSuccess() {
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
