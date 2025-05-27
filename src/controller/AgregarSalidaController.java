package controller;

import database.ProductosDAO;
import database.SalidasDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Productos;
import model.Salidas;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AgregarSalidaController {

    @FXML
    private ComboBox<String> departamentoComboBox;

    @FXML
    private TextField codigoProductoField;

    @FXML
    private TextField cantidadField;

    @FXML
    private TextArea observacionesArea;

    @FXML
    private TextField motivoField;
    
    @FXML
    private DatePicker fechaSalidaPicker;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelar;
    
    @FXML
    private Button btnBuscarProducto;

    @FXML
    private void initialize() {
        departamentoComboBox.getItems().addAll("Producción", "Mantenimiento", "Oficinas", "Logística");
        departamentoComboBox.setValue("Producción");
    }

    @FXML
    private void registrarSalida() throws SQLException {
        String departamento = departamentoComboBox.getValue();
        String codigo = codigoProductoField.getText().trim();
        String cantidadStr = cantidadField.getText().trim();
        String motivo = motivoField.getText().trim();
        
        LocalDate fechaSeleccionada = fechaSalidaPicker.getValue();
        if (fechaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Fecha inválida", "Debe seleccionar una fecha de salida.");
            return;
        }

        if (codigo.isEmpty() || cantidadStr.isEmpty() || motivo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de validación", "Debe completar todos los campos obligatorios: código, cantidad y motivo.");
            return;
        }

        String numeroFactura = generarNumeroFactura();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Cantidad inválida", "La cantidad debe ser un número entero positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Cantidad inválida", "La cantidad debe ser un número entero.");
            return;
        }

        Productos producto = ProductosDAO.buscarProductoPorCodigo(codigo);
        if (producto == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Producto no encontrado", "No existe ningún producto con ese código.");
            return;
        }

        if (producto.getStock().get() < cantidad) {
            mostrarAlerta(Alert.AlertType.ERROR, "Stock insuficiente", "No hay suficiente stock para realizar esta salida.");
            return;
        }

        Salidas salida = new Salidas();
        salida.setProductoId(producto.getId());
        salida.setDepartamento(departamento);
        salida.setCantidad(cantidad);
        salida.setMotivo(motivo);
        salida.setUsuarioId(Sesion.getUsuarioId());
        salida.setFechaSalida(java.sql.Date.valueOf(fechaSeleccionada));
        salida.setNumeroFactura(numeroFactura);
        
        System.out.println("Número de factura generado: " + numeroFactura);

        boolean exito = SalidasDAO.agregarSalida(salida);

        if (exito) {
            int nuevoStock = producto.getStock().get() - cantidad;
            producto.setStock(nuevoStock);
            ProductosDAO.actualizarProducto(producto);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Salida registrada", "La salida se registró correctamente.");

            // <-- Aquí agrego la verificación y advertencia si el stock queda bajo
            if (nuevoStock < producto.getStockMinimo().get()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia de Stock Bajo");
                alert.setHeaderText("Stock bajo después de la salida");
                alert.setContentText("El producto '" + producto.getNombre().get() + "' ahora tiene stock bajo (" + nuevoStock + ").");
                alert.showAndWait();
            }
            // <-- fin de la adición

            cerrarVentana();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar la salida.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void abrirSelectorProductos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeleccionarProductoView.fxml"));
            Parent root = loader.load();

            // Obtener controlador de la ventana de selección
            SeleccionarProductoController selectorController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Seleccionar producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Obtener producto seleccionado
            Productos productoSeleccionado = selectorController.getProductoSeleccionado();

            if (productoSeleccionado != null) {
                codigoProductoField.setText(productoSeleccionado.getCodigo().get());
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de selección de productos.");
        }
    }

    private String generarNumeroFactura() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "FAC-" + now.format(formatter);
    }
}

