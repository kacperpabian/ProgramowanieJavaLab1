package sample;

/**
* @author Kacper Pabian 226123*/


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** Main */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
        primaryStage.setTitle("Test checker");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getIcons().add(new Image("file:Resources\\icon.png"));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
