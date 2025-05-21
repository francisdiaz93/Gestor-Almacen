package controller;

import database.SalidasDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Salidas;

import java.util.Date;
import java.util.List;

public class SalidasController {

    @FXML private TableView<Salidas> tablaSalidas;
    @FXML private TableColumn<Salidas, Integer> colId;
    @FXML private TableColumn<Salidas, Integer> colProductoId;
    @FXML private TableColumn<Salidas, String> colDepartamento;
    @FXML private TableColumn<Salidas, Integer> colCantidad;
    @FXML private TableColumn<Salidas, Date> colFechaSalida;
    @FXML private TableColumn<Salidas, String> colMotivo;

    @FXML private TextField productoIdField;
    @FXML private TextField departamentoField;
    @FXML private TextField cantidadField;
    @FXML private DatePicker fechaSalidaPicker;
    @FXML private TextField motivoField;
    @FXML private TextField facturaField;
    
   // String numeroFacturaGenerado = SalidasDAO.generarNumeroFactura();

    private int usuarioId = Sesion.getUsuarioId();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colProductoId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getProductoId()).asObject());
        colDepartamento.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDepartamento()));
        colCantidad.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());
        colFechaSalida.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaSalida()));
        colMotivo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMotivo()));

        fechaSalidaPicker.setValue(java.time.LocalDate.now());

        cargarSalidas();
    }

    private void cargarSalidas() {
        List<Salidas> salidas = SalidasDAO.obtenerSalidas();
        ObservableList<Salidas> data = FXCollections.observableArrayList(salidas);
        tablaSalidas.setItems(data);
    }

    @FXML
    public void handleAgregarSalida() {
        try {
            if (productoIdField.getText().isEmpty() || departamentoField.getText().isEmpty()
                    || cantidadField.getText().isEmpty() || fechaSalidaPicker.getValue() == null) {
                mostrarAlerta("Completa todos los campos obligatorios.");
                return;
            }

            int productoId = Integer.parseInt(productoIdField.getText());
            String departamento = departamentoField.getText();
            int cantidad = Integer.parseInt(cantidadField.getText());
            Date fechaSalida = java.sql.Date.valueOf(fechaSalidaPicker.getValue());
            String motivo = motivoField.getText();

            if (cantidad <= 0) {
                mostrarAlerta("La cantidad debe ser mayor que cero.");
                return;
            }

            Salidas salida = new Salidas();
            salida.setProductoId(productoId);
            salida.setDepartamento(departamento);
            salida.setCantidad(cantidad);
            salida.setFechaSalida(fechaSalida);
            salida.setMotivo(motivo);
            salida.setUsuarioId(usuarioId);

            // Generar número de factura dinámicamente
            String numeroFactura = SalidasDAO.generarNumeroFactura();
            salida.setNumeroFactura(numeroFactura);

            boolean exito = SalidasDAO.agregarSalida(salida);
            if (exito) {
            	
                mostrarInfo("Salida registrada correctamente. N° Factura: " + numeroFactura);
                limpiarCampos();
                cargarSalidas();
            } else {
                mostrarAlerta("No hay suficiente stock para esta salida.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Datos numéricos inválidos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al registrar la salida.");
        }
    }

    private void limpiarCampos() {
        productoIdField.clear();
        departamentoField.clear();
        cantidadField.clear();
        fechaSalidaPicker.setValue(java.time.LocalDate.now());
        motivoField.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
