package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Cancion {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty artista = new SimpleStringProperty();
    private StringProperty album = new SimpleStringProperty();
    private StringProperty nombreImagen = new SimpleStringProperty();
    private BooleanProperty fingerstyle = new SimpleBooleanProperty();
    private ObjectProperty<LocalDate> año = new SimpleObjectProperty<>();

    public Cancion(int id, String nombre, String artista, String album, LocalDate año, boolean fingerstyle, String nombreImagen, String letra) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.artista.set(artista);
        this.album.set(album);
        this.año.set(año);
        this.fingerstyle.set(fingerstyle);
        this.nombreImagen.set(nombreImagen);
        this.letra.set(letra);
    }
    public Cancion(String nombre, String artista, String album,
                   LocalDate año, boolean fingerstyle, String nombreImagen, String letra) {
        this.nombre.set(nombre);
        this.artista.set(artista);
        this.album.set(album);
        this.año.set(año);
        this.fingerstyle.set(fingerstyle);
        this.nombreImagen.set(nombreImagen);
        this.letra.set(letra);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getArtista() { return artista.get(); }
    public void setArtista(String artista) { this.artista.set(artista); }
    public StringProperty artistaProperty() { return artista; }

    public String getAlbum() { return album.get(); }
    public void setAlbum(String album) { this.album.set(album); }
    public StringProperty albumProperty() { return album; }

    public String getNombreImagen() {
        return nombreImagen.get(); }
    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen.set(nombreImagen); }
    public StringProperty nombreImagenProperty() {
        return nombreImagen; }

    public boolean isFingerstyle() {
        return fingerstyle.get(); }
    public void setFingerstyle(boolean fingerstyle) {
        this.fingerstyle.set(fingerstyle); }
    public BooleanProperty fingerstyleProperty() {
        return fingerstyle; }

    public LocalDate getAño() {
        return año.get(); }
    public void setAño(LocalDate año) {
        this.año.set(año); }
    public ObjectProperty<LocalDate> añoProperty() {
        return año; }
    private StringProperty letra = new SimpleStringProperty();
    public String getLetra() {
        return letra.get(); }
    public void setLetra(String letra) {
        this.letra.set(letra); }
    public StringProperty letraProperty() {
        return letra; }

    public static ObservableList<Cancion> listar() {
        ObservableList<Cancion> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM canciones";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cancion c = new Cancion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getObject("anio", LocalDate.class),
                        rs.getBoolean("fingerstyle"),
                        rs.getString("imagen"),
                        rs.getString("letra")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar canciones: " + e.getMessage());
        }
        return lista;
    }

    public static void insertar(Cancion c) {
        String sql = "INSERT INTO canciones (nombre, artista, album, anio, fingerstyle, imagen, letra) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getArtista());
            ps.setString(3, c.getAlbum());
            ps.setObject(4, c.getAño());
            ps.setBoolean(5, c.isFingerstyle());
            ps.setString(6, c.getNombreImagen());
            ps.setString(7, c.getLetra());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar canción: " + e.getMessage(), e);
        }
    }
    public static boolean comprobarSiExiste(String nombre, String artista) {
        String sql = "SELECT COUNT(*) FROM canciones WHERE nombre = ? AND artista = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, artista);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error al verificar duplicado: " + e.getMessage());
            return false;
        }
    }

    public static void actualizar(Cancion c) {
        String sql = "UPDATE canciones SET nombre=?, artista=?, album=?, anio=?, fingerstyle=?, imagen=?, letra=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getArtista());
            ps.setString(3, c.getAlbum());
            ps.setObject(4, c.getAño());
            ps.setBoolean(5, c.isFingerstyle());
            ps.setString(6, c.getNombreImagen());
            ps.setString(7, c.getLetra());
            ps.setInt(8, c.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar canción: " + e.getMessage());
        }
    }


    public static void eliminar(Cancion c) {
        String sql = "DELETE FROM canciones WHERE id=?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar canción: " + e.getMessage());
        }
    }
}