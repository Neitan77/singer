package controller;

import javafx.collections.ObservableList;
import model.Cancion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppControllerTest {

    @Test
    public void testInsertarCancion() {
        Cancion c = new Cancion(
                "Imagine",
                "John Lennon",
                "Imagine",
                LocalDate.of(1971, 10, 11),
                true,
                "imagine.jpg",
                "esta es la super letra de la cancion asi bien genial de jhon lennon si lees esto hiciste una prueba y salio bien , felicidades ojala saques 10"
        );

        Cancion.insertar(c);

        ObservableList<Cancion> canciones = Cancion.listar();
        boolean existe = canciones.stream()
                .anyMatch(x -> x.getNombre().equals("Imagine")
                        && x.getArtista().equals("John Lennon")
                        && x.getLetra().contains("Imagine all the people"));

        assertTrue(existe, "La canción debería haberse insertado correctamente con su letra.");
    }
}