package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:datos.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializarBase() {
        String sql = "CREATE TABLE IF NOT EXISTS canciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "artista TEXT NOT NULL, " +
                "album TEXT, " +
                "anio DATE, " +
                "fingerstyle BOOLEAN, " +
                "imagen TEXT, " +
                "letra TEXT, " +
                "UNIQUE(nombre, artista))";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear tabla: " + e.getMessage());
        }
    }
}