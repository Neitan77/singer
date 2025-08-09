package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Cancion;
import javafx.collections.ObservableList;
import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class AppController {

    @FXML private TextField tfNombre;
    @FXML private TextField tfArtista;
    @FXML private TextField tfAlbum;
    @FXML private DatePicker dpAño;
    @FXML private ComboBox<String> cbFingerstyle;
    @FXML private TextField tfNombreImagen;
    @FXML private ImageView ivImagen;
    @FXML private TextArea taLetra;
    @FXML private TableView<Cancion> tabla;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnExplorarArchivos;
    @FXML private TableColumn<Cancion, String> colNombre;
    @FXML private TableColumn<Cancion, String> colArtista;
    @FXML private TableColumn<Cancion, String> colAlbum;
    @FXML private TableColumn<Cancion, LocalDate> colAño;
    @FXML private TableColumn<Cancion, Boolean> colFingerstyle;
    @FXML private TableColumn<Cancion, Integer> colId;

    @FXML
    public void initialize() {
        cbFingerstyle.getItems().addAll("Sí", "No");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colArtista.setCellValueFactory(cellData -> cellData.getValue().artistaProperty());
        colAlbum.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        colAño.setCellValueFactory(cellData -> cellData.getValue().añoProperty());
        colFingerstyle.setCellValueFactory(cellData -> cellData.getValue().fingerstyleProperty());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabla.setItems(Cancion.listar());
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarSeleccion(newSel);
            }
        });
    }

    private void cargarDatos() {
        ObservableList<Cancion> canciones = Cancion.listar();
        tabla.setItems(canciones);
    }

    private void mostrarSeleccion(Cancion c) {
        if (c != null) {
            tfNombre.setText(c.getNombre());
            tfArtista.setText(c.getArtista());
            tfAlbum.setText(c.getAlbum());
            dpAño.setValue(c.getAño());
            cbFingerstyle.setValue(c.isFingerstyle() ? "Sí" : "No");
            tfNombreImagen.setText(c.getNombreImagen());
            taLetra.setText(c.getLetra()); // ← NUEVO
            cargarImagen(c.getNombreImagen());
        }
    }

    private void cargarImagen(String nombreArchivo) {
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            File file = new File("imagenes/" + nombreArchivo);
            if (file.exists()) {
                ivImagen.setImage(new Image(file.toURI().toString()));
            } else {
                ivImagen.setImage(null);
            }
        } else {
            ivImagen.setImage(null);
        }
    }

    @FXML
    Cancion Save() {
        String nombre = tfNombre.getText().trim();
        String artista = tfArtista.getText().trim();
        String album = tfAlbum.getText().trim();
        LocalDate año = dpAño.getValue();
        boolean fingerstyle = "Sí".equals(cbFingerstyle.getValue());
        String nombreImagen = tfNombreImagen.getText().trim();
        String letra = taLetra.getText().trim();

        if (Cancion.comprobarSiExiste(nombre, artista)) {
            mostrarAlerta("Duplicado", "Ya existe una canción con ese nombre y artista.");
            return null;
        }

        if (nombre.isEmpty() || artista.isEmpty() || año == null) {
            mostrarAlerta("Campos obligatorios", "Nombre, artista y año son requeridos.");
            return null;
        }

        Cancion nueva = new Cancion(nombre, artista, album, año, fingerstyle, nombreImagen, letra);
        Cancion.insertar(nueva);
        cargarDatos();
        limpiarCampos();
        return nueva;
    }

    @FXML
    private void Delete() {
        Cancion seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Cancion.eliminar(seleccionada);
            cargarDatos();
            limpiarCampos();
        } else {
            mostrarAlerta("Sin selección", "Selecciona una canción para eliminar.");
        }
    }

    @FXML
    private void Update() {
        Cancion seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin selección", "Selecciona una canción para modificar.");
            return;
        }

        String nombre = tfNombre.getText().trim();
        String artista = tfArtista.getText().trim();
        String album = tfAlbum.getText().trim();
        LocalDate año = dpAño.getValue();
        boolean fingerstyle = "Sí".equals(cbFingerstyle.getValue());
        String nombreImagen = tfNombreImagen.getText().trim();
        String letra = taLetra.getText().trim(); // ← NUEVO

        seleccionada.setNombre(nombre);
        seleccionada.setArtista(artista);
        seleccionada.setAlbum(album);
        seleccionada.setAño(año);
        seleccionada.setFingerstyle(fingerstyle);
        seleccionada.setNombreImagen(nombreImagen);
        seleccionada.setLetra(letra);
        Cancion.actualizar(seleccionada);
        cargarDatos();
        tabla.getSelectionModel().select(seleccionada);
    }

    @FXML
    private void Clear() {
        limpiarCampos();
    }

    private void limpiarCampos() {
        tfNombre.clear();
        tfArtista.clear();
        tfAlbum.clear();
        dpAño.setValue(null);
        cbFingerstyle.setValue(null);
        tfNombreImagen.clear();
        taLetra.clear(); // ← NUEVO
        ivImagen.setImage(null);
        tabla.getSelectionModel().clearSelection();
    }

    @FXML
    private void explorarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            String nombreDestino = archivo.getName();

            File carpetaImagenes = new File("imagenes");
            if (!carpetaImagenes.exists()) {
                carpetaImagenes.mkdirs();
            }

            File destino = new File(carpetaImagenes, nombreDestino);

            try {
                java.nio.file.Files.copy(archivo.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                tfNombreImagen.setText(nombreDestino);
                ivImagen.setImage(new Image(destino.toURI().toString()));
            } catch (Exception e) {
                mostrarAlerta("Error al copiar imagen", "No se pudo copiar la imagen:\n" + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void cargarLetra() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de letra");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            try (Scanner scanner = new Scanner(archivo)) {
                StringBuilder contenido = new StringBuilder();
                while (scanner.hasNextLine()) {
                    contenido.append(scanner.nextLine()).append("\n");
                }
                taLetra.setText(contenido.toString());
            } catch (Exception e) {
                System.out.println("Error al leer el archivo de letra: " + e.getMessage());
            }
        }
    }

}