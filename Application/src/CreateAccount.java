import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Objects;


public class CreateAccount {

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
    void initialize() {
        back.setOnAction(actionEvent -> backClicked());
        createAccount.setOnAction(actionEvent -> createAccountClicked());
    }

    private void backClicked() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
            // Get the current window
            Stage currentStage = (Stage) back.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }

    private void createAccountClicked() {
        //TODO: Implement create account functionality
        System.out.println("Create Account Clicked");
    }

}
