package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Entradas;
import model.Productos;
import database.EntradasDAO;
import java.time.LocalDate;


public class AgregarEntradaExistenteController {

    @FXML private Label nombreProductoLabel;
    @FXML private TextField cantidadField;
    @FXML private DatePicker fechaPicker;
    @FXML private TextArea observacionesField;
    @FXML private Label proveedorField;

    private Productos producto;

    public void setProducto(Productos producto) {
        this.producto = producto;
        nombreProductoLabel.setText(producto.getNombre().get());


        // Mostrar proveedor y hacer campo no editable
        proveedorField.setText(producto.getProveedor().get());
    }

    @FXML
    private void registrarEntrada() {
        try {
            if (cantidadField.getText().isEmpty()) {
                mostrarAlerta("Error", "Completa todos los campos.");
                return;
            }

            int cantidad = Integer.parseInt(cantidadField.getText());
            LocalDate fecha = fechaPicker.getValue();

            if (fecha == null || cantidad <= 0) {
                mostrarAlerta("Error", "Por favor completa todos los campos correctamente.");
                return;
            }

            Entradas entrada = new Entradas();
            entrada.setProductoId(producto.getId());
            entrada.setCantidad(cantidad);
            entrada.setFechaIngreso(java.sql.Date.valueOf(fecha));
            entrada.setUsuarioId(Sesion.getUsuarioId());
            entrada.setNumeroFactura("FACT-" + System.currentTimeMillis());
            entrada.setObservaciones(observacionesField.getText());
            entrada.setProveedor(producto.getProveedor().get());

            EntradasDAO.insertarEntrada(entrada);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText(null);
            alert.setContentText("La entrada se registró correctamente.");
            alert.showAndWait();

            Stage stage = (Stage) cantidadField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Costo unitario y cantidad deben ser valores numéricos.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al registrar la entrada.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}