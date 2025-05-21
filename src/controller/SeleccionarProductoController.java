package controller;

import database.ProductosDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Productos;

import java.util.List;

public class SeleccionarProductoController {

    @FXML
    private TableView<Productos> tablaProductos;

    @FXML
    private TableColumn<Productos, String> colCodigo;

    @FXML
    private TableColumn<Productos, String> colNombre;

    @FXML
    private TableColumn<Productos, Integer> colStock;

    private Productos productoSeleccionado;

    @FXML
    private void initialize() {
        // Configurar las columnas
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colStock.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());

        // Cargar productos con stock > 0
        List<Productos> lista = ProductosDAO.listarProductosEnStock();
        ObservableList<Productos> productosEnStock = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(productosEnStock);
    }

    @FXML
    private void seleccionarProducto() {
        productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            cerrarVentana();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Sin selección");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, seleccione un producto.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        productoSeleccionado = null;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
        stage.close();
    }

    public Productos getProductoSeleccionado() {
        return productoSeleccionado;
    }
}
