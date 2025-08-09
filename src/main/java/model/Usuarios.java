package model;

import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static utilidades.Alertas.mostrarError;

public class Usuarios {

    public void mostrarFingerCombo(ComboBox<String> cbFingerstyle) {
        cbFingerstyle.getItems().clear();
        cbFingerstyle.setValue("Seleccionar:");

        String sqlFingerstyle = "SELECT * FROM fingerstyle";

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlFingerstyle)) {

            while (rs.next()) {
                int idFingerstyle = rs.getInt("id");
                String nombreFingerstyle = rs.getString("fingerstyle");

                cbFingerstyle.getItems().add(nombreFingerstyle);
                cbFingerstyle.getProperties().put(nombreFingerstyle, idFingerstyle);
            }

        } catch (Exception e) {
            mostrarError("Error", "Error al mostrar datos: " + e.toString());
        }
    }
}