package utilidades;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Alertas {

    public static void mostrarAlerta(String title, String contenido) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        VBox content = new VBox(new Label(contenido));
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait();
    }

    public static void mostrarError(String title, String contenido) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("error" + title);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        VBox content = new VBox(new Label(contenido));
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait();
    }
}