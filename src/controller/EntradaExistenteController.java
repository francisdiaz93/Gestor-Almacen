/*package controller;

import database.EntradasDAO;
import database.ProductosDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Entradas;
import model.Productos;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EntradaExistenteController {

    @FXML private TableView<Productos> tablaProductos;
    @FXML private TableColumn<Productos, String> colNombre;
    @FXML private TableColumn<Productos, String> colCodigo;
    @FXML private TableColumn<Productos, Integer> colCantidad;

    @FXML private TextField cantidadField;
    @FXML private TextField facturaField;
    @FXML private TextField proveedorField;
    @FXML private TextField observacionesField;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
        colCodigo.setCellValueFactory(cell -> cell.getValue().codigoProperty());
        colCantidad.setCellValueFactory(cell -> cell.getValue().cantidadProperty().asObject());

        List<Productos> productos = ProductosDAO.obtenerProductos();
        tablaProductos.getItems().setAll(productos);
    }

    @FXML
    private void registrarEntrada() {
        Productos productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadField.getText());
            Date fechaActual = Date.valueOf(LocalDate.now());

            Entradas entrada = new Entradas();
            entrada.setProductoId(productoSeleccionado.getId());
            entrada.setCantidad(cantidad);
            entrada.setFechaIngreso(fechaActual);
            entrada.setProveedor(proveedorField.getText());
            entrada.setUsuarioId(Sesion.getUsuario());
            entrada.setNumeroFactura(facturaField.getText());
            entrada.setObservaciones(observacionesField.getText());

            boolean exito = EntradasDAO.insertarEntrada(entrada);
            if (exito) {
                // Actualizar cantidad del producto
                ProductosDAO.actualizarCantidadProducto(productoSeleccionado.getId(), cantidad);
                mostrarAlerta("Entrada registrada exitosamente.");
                ((Stage) cantidadField.getScene().getWindow()).close();
            } else {
                mostrarAlerta("Error al registrar la entrada.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Cantidad o costo inválido.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}*/