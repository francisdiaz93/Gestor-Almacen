package controller;

import database.EntradasDAO;
import database.SalidasDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Entradas;
import model.Productos;
import model.Salidas;

import java.util.List;

public class DetalleProductoController {

    @FXML
    private Label lblNombre, lblCodigo, lblCategoria, lblDescripcion, lblCantidad;

    @FXML
    private TableView<Entradas> tablaEntradas;

    @FXML
    private TableColumn<Entradas, String> colProveedor;
    @FXML
    private TableColumn<Entradas, Integer> colCantidadEntrada;
    @FXML
    private TableColumn<Entradas, String> colFacturaEntrada;
    @FXML
    private TableColumn<Entradas, java.util.Date> colFechaEntrada;

    @FXML
    private TableView<Salidas> tablaSalidas;

    @FXML
    private TableColumn<Salidas, String> colDepartamento;
    @FXML
    private TableColumn<Salidas, Integer> colCantidadSalida;
    @FXML
    private TableColumn<Salidas, String> colFacturaSalida;
    @FXML
    private TableColumn<Salidas, java.util.Date> colFechaSalida;

    private Productos producto;

    public void setProducto(Productos producto) {
        this.producto = producto;
        mostrarDatosProducto();
        cargarEntradas();
        cargarSalidas();
    }

    private void mostrarDatosProducto() {
        lblNombre.setText(producto.getNombre().get());
        lblCodigo.setText(producto.getCodigo().get());
        lblCategoria.setText(producto.getCategoria().get());
        lblDescripcion.setText(producto.getDescripcion().get());
        lblCantidad.setText(String.valueOf(producto.getStock().get()));
    }

    private void cargarEntradas() {
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colCantidadEntrada.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFacturaEntrada.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colFechaEntrada.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        List<Entradas> entradas = EntradasDAO.obtenerPorProducto(producto.getId());
        tablaEntradas.getItems().setAll(entradas);
    }

    private void cargarSalidas() {
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colCantidadSalida.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFacturaSalida.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colFechaSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));

        List<Salidas> salidas = SalidasDAO.obtenerPorProducto(producto.getId());
        tablaSalidas.getItems().setAll(salidas);
    }
}
