package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import model.Salidas;
import database.ProductosDAO;
import database.SalidasDAO;
import database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SalidasViewController {

    @FXML private TableView<Salidas> salidasTable;

    @FXML private TableColumn<Salidas, String> colNombreProducto;
    @FXML private TableColumn<Salidas, String> colCodigoProducto;
    @FXML private TableColumn<Salidas, String> colDepartamento;
    @FXML private TableColumn<Salidas, String> colCantidad;
    @FXML private TableColumn<Salidas, String> colFecha;
    @FXML private TableColumn<Salidas, String> colMotivo;
    @FXML private TableColumn<Salidas, String> colNumeroFactura;

    @FXML private TextField buscarField;

    @FXML private Button btnAgregarSalida;

    @FXML private TextField codigoProductoField;
    @FXML private TextField departamentoField;
    @FXML private TextField cantidadField;
    @FXML private DatePicker fechaSalidaPicker;
    @FXML private TextField motivoField;

    private ObservableList<Salidas> listaSalidas;

    @FXML
    public void initialize() {
        listaSalidas = FXCollections.observableArrayList();
        cargarSalidas();

        colCodigoProducto.setCellValueFactory(data ->
            new SimpleStringProperty(obtenerCodigoProducto(data.getValue().getProductoId()))
        );
        colNombreProducto.setCellValueFactory(data ->
            new SimpleStringProperty(obtenerNombreProducto(data.getValue().getProductoId()))
        );
        colDepartamento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartamento()));
        colCantidad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCantidad())));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaSalida().toString()));
        colMotivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotivo()));
        colNumeroFactura.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroFactura()));

        salidasTable.setItems(listaSalidas);

        // Listener para el campo buscarField para filtrar la tabla en tiempo real
        buscarField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarSalidas(newValue);
        });
    }

    private void cargarSalidas() {
        listaSalidas.setAll(SalidasDAO.obtenerSalidas());
    }

    private void filtrarSalidas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            salidasTable.setItems(listaSalidas);
            return;
        }

        ObservableList<Salidas> listaFiltrada = FXCollections.observableArrayList();
        String filtroLower = filtro.toLowerCase();

        for (Salidas s : listaSalidas) {
            String nombreProducto = obtenerNombreProducto(s.getProductoId()).toLowerCase();
            String numeroFactura = s.getNumeroFactura().toLowerCase();

            if (nombreProducto.contains(filtroLower) || numeroFactura.contains(filtroLower)) {
                listaFiltrada.add(s);
            }
        }

        salidasTable.setItems(listaFiltrada);
    }

    private String obtenerCodigoProducto(int productoId) {
        String codigo = "";
        String sql = "SELECT codigo FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                codigo = rs.getString("codigo");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el código del producto: " + e.getMessage());
        }
        return codigo;
    }

    private String obtenerNombreProducto(int productoId) {
        String nombre = "";
        String sql = "SELECT nombre FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre del producto: " + e.getMessage());
        }
        return nombre;
    }

    private int obtenerIdPorCodigo(String codigo) {
        int id = -1;
        String sql = "SELECT id FROM productos WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el ID del producto: " + e.getMessage());
        }
        return id;
    }

    @FXML
    private void handleAgregarSalida() throws SQLException {
        try {
            String codigoProducto = codigoProductoField.getText().trim();
            if (codigoProducto.isEmpty()) {
                mostrarAlerta("Campo vacío", "Ingrese el código del producto.");
                return;
            }

            int productoId = obtenerIdPorCodigo(codigoProducto);
            if (productoId == -1) {
                mostrarAlerta("Producto no encontrado", "No existe un producto con ese código.");
                return;
            }

            String departamento = departamentoField.getText().trim();
            int cantidad = Integer.parseInt(cantidadField.getText().trim());
            LocalDate fechaLocal = fechaSalidaPicker.getValue();
            String motivo = motivoField.getText().trim();

            if (departamento.isEmpty() || motivo.isEmpty() || fechaLocal == null) {
                mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos.");
                return;
            }

            // Obtener stock actual y mínimo
            int stockActualAntes = ProductosDAO.obtenerStockActual(productoId);
            int stockMinimo = ProductosDAO.obtenerStockMinimo(productoId);

            System.out.println("Stock actual antes: " + stockActualAntes);
            System.out.println("Stock mínimo: " + stockMinimo);
            System.out.println("Cantidad a restar: " + cantidad);

            if (cantidad > stockActualAntes) {
                mostrarAlerta("Stock insuficiente", "No hay suficiente stock para esta salida.");
                return;
            }

            // Registrar salida
            String numeroFactura = "S" + System.currentTimeMillis();

            Salidas salida = new Salidas();
            salida.setProductoId(productoId);
            salida.setDepartamento(departamento);
            salida.setCantidad(cantidad);
            salida.setFechaSalida(java.sql.Date.valueOf(fechaLocal));
            salida.setMotivo(motivo);
            salida.setUsuarioId(Sesion.getUsuarioId());
            salida.setNumeroFactura(numeroFactura);

            boolean exito = SalidasDAO.agregarSalida(salida);

            if (exito) {
                boolean stockActualizado = ProductosDAO.restarStock(productoId, cantidad);

                if (stockActualizado) {
                    int stockActualDespues = ProductosDAO.obtenerStockActual(productoId);
                    System.out.println("Stock actual después: " + stockActualDespues);

                    if (stockActualDespues < stockMinimo) {
                        mostrarAlertaStockBajo(productoId, stockActualDespues, stockMinimo);
                    }

                    listaSalidas.add(salida);
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error al actualizar stock", "No se pudo actualizar el stock.");
                }
            } else {
                mostrarAlerta("Error al registrar", "No se pudo guardar la salida en la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Datos inválidos", "Verifique que la cantidad sea un número válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado", "Ocurrió un error inesperado.");
        }
    }


    private void mostrarAlertaStockBajo(int productoId, int stockActual, int stockMinimo) {
        String nombre = ProductosDAO.obtenerNombreProducto(productoId);

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Stock bajo mínimo");
        alerta.setHeaderText("Producto: " + nombre);
        alerta.setContentText("Stock actual: " + stockActual + "\nStock mínimo: " + stockMinimo);
        alerta.showAndWait();
    }


    private void limpiarCampos() {
        codigoProductoField.clear();
        departamentoField.clear();
        cantidadField.clear();
        fechaSalidaPicker.setValue(null);
        motivoField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void abrirFormularioAgregarSalida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AgregarSalidaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Salida");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarSalidas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirAgregarSalida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AgregarSalidaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nueva Salida");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarSalidas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

