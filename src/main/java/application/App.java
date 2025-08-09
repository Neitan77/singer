package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.ConexionBD;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        ConexionBD.inicializarBase();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/app.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("base de datos de canciones");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Error al cargar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}