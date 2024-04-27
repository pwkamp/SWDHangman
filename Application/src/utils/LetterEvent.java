package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LetterEvent extends ActionEvent implements EventHandler<ActionEvent> {
    private final char letter;

    public LetterEvent(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }


    //TODO: Properly implement the handle method
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Letter: " + getLetter() + " clicked");
    }
}
