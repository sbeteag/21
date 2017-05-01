/**
 * Created by Steven on 4/3/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TwentyOne extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        File savePath = new File("Saves/");
        if(!savePath.exists()){
            savePath.mkdir();
        }
        Parent root = FXMLLoader.load(getClass().getResource("title.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Twenty-One");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
