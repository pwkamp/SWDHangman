import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * ApplicationDriver.java
 * Main class that loads and displays the Client Application.
 * This class extends the Application class from the JavaFX library.
 * Based on the TipCalculator.java class
 * @author Peter Kamp
 */
public class Application extends javafx.application.Application {

    /**
     * The start method is the main entry point for all JavaFX applications.
     * It is called after the init() method has returned, and after the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set.
     * @throws Exception if the fxml file is not found.
     * @author Peter Kamp
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scenes/ServerSelect.fxml")));

        Scene scene = new Scene(root); // attach scene graph to scene
        stage.setTitle("Hangman"); // displayed in window's title bar
        stage.setScene(scene); // attach scene to stage
        stage.show(); // display the stage
    }

    public static void main(String[] args) {
        // create a QuizGUI object and call its start method
        launch(args);
    }
}