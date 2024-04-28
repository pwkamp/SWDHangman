package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Client;
import utils.Debugger;

import java.io.IOException;
import java.util.Objects;

public class ServerSelectController {

    Client client = Client.getInstance();

    @FXML
    TextField ipBox;

    @FXML
    Button connectButton;

    public void initialize() {
        connectButton.setOnAction(event -> connectClicked());
    }

    public void connectClicked() {
        Debugger.debug("Connect clicked");


        try {
            client.connect(ipBox.getText(), 23535);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/MainMenu.fxml")));
            // Get the current window
            Stage currentStage = (Stage) connectButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
}

