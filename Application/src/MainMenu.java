import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class MainMenu {

    @FXML
    private Button login;

    @FXML
    private Button createAccount;

    @FXML
    void initialize() {
        login.setOnAction(actionEvent -> loginClicked());

    }

    public void loginClicked() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            Scene scene = new Scene(root); // attach scene graph to scene
            stage.setTitle("Hangman"); // displayed in window's title bar
            stage.setScene(scene); // attach scene to stage
            stage.show(); // display the stage
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
}
