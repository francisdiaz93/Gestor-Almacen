package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import database.ProductosDAO;

public class AddProductController {

    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField cantidadField;
    @FXML private TextField proveedorField;
    @FXML private TextField precioField;
    @FXML private TextField marcaField;
    @FXML private ComboBox<String> categoriaComboBox;
    @FXML private DatePicker fechaIngresoPicker;
    @FXML private TextField observacionesField;
    @FXML private TextField stockMinimoField;
    @FXML private ImageView imagenPreview;
    private File archivoImagen;

    private final String DB_URL = "jdbc:mysql://localhost:3307/almacen_db";
    private final String USER = "root";
    private final String PASS = "";

    int usuarioId = Sesion.getUsuarioId();

    @FXML
    public void handleAddProduct(ActionEvent event) {
        if (nombreField.getText().isEmpty() || categoriaComboBox.getEditor().getText().isEmpty()
                || descripcionField.getText().isEmpty() || cantidadField.getText().isEmpty()
                || proveedorField.getText().isEmpty() || marcaField.getText().isEmpty() || stockMinimoField.getText().isEmpty() || fechaIngresoPicker.getValue() == null)  {
            mostrarAlerta("Todos los campos deben estar completos.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.setAutoCommit(false);

            int productoId;

            // Generar el código basado en el último producto_id
            productoId = obtenerSiguienteProductoId(conn);
            String codigoGenerado = String.format("P%03d", productoId);
            
         // Copiar imagen a carpeta local y obtener ruta
            String rutaImagen = null;
            if (archivoImagen != null) {
                Path destino = Paths.get("imagenes", archivoImagen.getName());
                Files.copy(archivoImagen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                rutaImagen = destino.toAbsolutePath().toString();
            }

            // Insertar nuevo producto
            String sqlInsert = "INSERT INTO productos (nombre, codigo, categoria, descripcion, cantidad, proveedor, fecha_ingreso, marca, stock_minimo, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, nombreField.getText());
                insertStmt.setString(2, codigoGenerado);
                insertStmt.setString(3, categoriaComboBox.getEditor().getText());
                insertStmt.setString(4, descripcionField.getText());
                insertStmt.setInt(5, Integer.parseInt(cantidadField.getText()));
                insertStmt.setString(6, proveedorField.getText());
                insertStmt.setDate(7, Date.valueOf(fechaIngresoPicker.getValue()));
                insertStmt.setString(8, marcaField.getText());
                insertStmt.setInt(9, Integer.parseInt(stockMinimoField.getText()));
                insertStmt.setString(10, rutaImagen);

                insertStmt.executeUpdate();

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productoId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID del producto insertado.");
                    }
                }
            }

            // Insertar la entrada **sin número de factura**, obteniendo el id generado para la entrada
            int entradaId;
            String sqlEntrada = "INSERT INTO entradas (producto_id, cantidad, fecha_ingreso, proveedor, usuario_id, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
            int cantidad = Integer.parseInt(cantidadField.getText());

            try (PreparedStatement stmtEntrada = conn.prepareStatement(sqlEntrada, Statement.RETURN_GENERATED_KEYS)) {
                stmtEntrada.setInt(1, productoId);
                stmtEntrada.setInt(2, cantidad);
                stmtEntrada.setDate(3, Date.valueOf(fechaIngresoPicker.getValue()));
                stmtEntrada.setString(4, proveedorField.getText());
                stmtEntrada.setInt(5, usuarioId);
                stmtEntrada.setString(6, observacionesField.getText());

                stmtEntrada.executeUpdate();

                try (ResultSet generatedKeys = stmtEntrada.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entradaId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la entrada insertada.");
                    }
                }
            }

            // Generar número de factura basado en el ID de la entrada
            String numeroFactura = String.format("FAC%06d", entradaId);

            // Actualizar la entrada con el número de factura generado
            String sqlUpdateFactura = "UPDATE entradas SET numero_factura = ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateFactura)) {
                updateStmt.setString(1, numeroFactura);
                updateStmt.setInt(2, entradaId);
                updateStmt.executeUpdate();
            }

            conn.commit();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro Exitoso");
            alert.setHeaderText(null);
            alert.setContentText("El producto se ha registrado correctamente.\nCódigo generado: " + codigoGenerado + "\nNúmero factura: " + numeroFactura);
            alert.showAndWait();

            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();

            if (callbackProductoRegistrado != null) {
                callbackProductoRegistrado.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al guardar el producto y la entrada.");
        }
    }

    private int obtenerSiguienteProductoId(Connection conn) throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM productos";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        }
        return 1;
    }

    public void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    public void initialize() {
        List<String> categorias = ProductosDAO.obtenerCategorias();
        categoriaComboBox.getItems().addAll(categorias);

        fechaIngresoPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        fechaIngresoPicker.setValue(LocalDate.now());
    }

    private Runnable callbackProductoRegistrado;

    public void setCallbackProductoRegistrado(Runnable callback) {
        this.callbackProductoRegistrado = callback;
    }
    
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            archivoImagen = file;
            Image imagen = new Image(file.toURI().toString());
            imagenPreview.setImage(imagen);
        }
    }
}
