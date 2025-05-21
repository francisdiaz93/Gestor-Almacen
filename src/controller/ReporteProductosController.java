package controller;

import database.ProductosDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Productos;

import java.util.List;

public class ReporteProductosController {

    @FXML
    private TableView<Productos> tablaProductos;

    @FXML private TableColumn<Productos, String> colNombre;
    @FXML private TableColumn<Productos, String> colCodigo;
    @FXML private TableColumn<Productos, String> colCategoria;
    @FXML private TableColumn<Productos, Integer> colStock;
    @FXML private TableColumn<Productos, String> colProveedor;
    @FXML private TableColumn<Productos, String> colMarca;

    private ObservableList<Productos> listaProductos;


@FXML
private void initialize() {
    colNombre.setCellValueFactory(cellData -> cellData.getValue().getNombre());
    colCodigo.setCellValueFactory(cellData -> cellData.getValue().getCodigo());
    colCategoria.setCellValueFactory(cellData -> cellData.getValue().getCategoria());
    colStock.setCellValueFactory(cellData -> cellData.getValue().getStock().asObject());
    colProveedor.setCellValueFactory(cellData -> cellData.getValue().getProveedor());
    colMarca.setCellValueFactory(cellData -> cellData.getValue().getMarca());

    cargarDatos();
}

    private void cargarDatos() {
        List<Productos> productos = ProductosDAO.obtenerProductosConStock();  // Método DAO que filtra stock > 0
        listaProductos = FXCollections.observableArrayList(productos);
        tablaProductos.setItems(listaProductos);
    }

    @FXML
    private void exportarPDF() {
        // Aquí implementamos la exportación a PDF (usando iText o similar)
    }

    @FXML
    private void exportarExcel() {
        // Aquí implementamos la exportación a Excel (usando Apache POI o similar)
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
        stage.close();
    }
}
