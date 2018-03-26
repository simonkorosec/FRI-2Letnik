package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static double stageWidth;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        stageWidth = primaryStage.getWidth();

        primaryStage.widthProperty().addListener(((observable, oldValue, newValue) -> stageWidth = primaryStage.getWidth()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
