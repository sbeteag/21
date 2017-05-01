import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TestGUI implements EventHandler<ActionEvent> {

    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Button was clicked");
    }
}