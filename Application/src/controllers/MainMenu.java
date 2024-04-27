package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

public class MainMenu {

    @FXML
    private Button login;

    @FXML
    private Button createAccount;

    @FXML
    void initialize() {
        login.setOnAction(actionEvent -> loginClicked());
        createAccount.setOnAction(actionEvent -> createAccountClicked());

    }

    public void loginClicked() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/Login.fxml")));
            // Get the current window
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
    public void createAccountClicked() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/CreateAccount.fxml")));
            // Get the current window
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
}
