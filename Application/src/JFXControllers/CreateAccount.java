package JFXControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import utils.Client;
import utils.Debugger;


public class CreateAccount {

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
            client.awaitMessage();
            if (client.getMessage().equals("CREATE success")) {
                Debugger.debug("Account created");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Account created");
                alert.showAndWait();
                backClicked();
            } else {
                Debugger.debug("Account not created");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Account not created");
                alert.showAndWait();
            }
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
