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

public class GameSelection {

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
    void initialize() {
        Debugger.debug("Game Selection Initialized");
        logout.setOnAction(actionEvent -> logoutClicked());
        setUsername();
        setCoins();
    }

    private void logoutClicked() {
        try {
            //TODO: Add any needed functionality to logout that does not involve JavaFX scene switching back to the Main Menu
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
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

    private void setCoins() {
        //TODO: Get the user's coins from the server and set the text to the number of coins
    }

    private void setUsername() {
        //TODO: Get the user's username from the server and set the text to the username
    }
}
