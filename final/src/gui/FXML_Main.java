package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Startet das Programm und indem es die scene erstellt und die FXML-Datei
 * einbindet
 *
 * @author Dimitri Solodownik, inf102736
 */
public class FXML_Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setMinHeight(600);
        stage.setMinWidth(600);

        stage.show();
        stage.setTitle("Pentago");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
