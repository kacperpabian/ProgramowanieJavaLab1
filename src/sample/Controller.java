package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Controller {
/** Funkcja wywoływana po naciśnięciu przycisku. Otwiera nowe okno menu */
    public void generate (ActionEvent event) {

        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Statistics.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("Statistics");
            stage.setScene(new Scene(root1));
            stage.getIcons().add(new Image("file:Resources/icon.png"));
            stage.show();
        }
        catch (Exception e)
        {
            System.out.println("Błąd przy załadowywaniu drugiego okna.");
        }


    }
}
