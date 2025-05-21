package controller;

import model.Productos;

import java.util.List;

import database.ProductosDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarProductoController {

	    @FXML private TextField nombreField;
	    @FXML private TextField codigoField;
	    @FXML private TextField descripcionField;
	    @FXML private TextField cantidadField;
	    @FXML private TextField proveedorField;
	    @FXML private DatePicker fechaIngresoPicker;
	    @FXML private TextField marcaField;
	    @FXML private TextField stockMinimoField;
	    @FXML
	    private ComboBox<String> comboCategorias;

	    private Productos producto;

	    public void setProducto(Productos producto) {
	        this.producto = producto;

	        // Cargar valores actuales
	        nombreField.setText(producto.getNombre().get());
	        codigoField.setText(producto.getCodigo().get());
	        descripcionField.setText(producto.getDescripcion().get());
	        cantidadField.setText(String.valueOf(producto.getStock().get()));
	        proveedorField.setText(producto.getProveedor().get());
	        fechaIngresoPicker.setValue(producto.getFechaIngreso().get());
	        marcaField.setText(producto.getMarca().get());
	        stockMinimoField.setText(String.valueOf(producto.getStockMinimo().get()));
	        comboCategorias.setValue(producto.getCategoria().get());
	    }

	    @FXML
	    private void guardarCambios() {
	        try {
	            producto.setNombre(nombreField.getText());
	            producto.setCodigo(codigoField.getText());
	            producto.setDescripcion(descripcionField.getText());
	            producto.setStock(Integer.parseInt(cantidadField.getText()));
	            producto.setProveedor(proveedorField.getText());
	            producto.setFechaIngreso(fechaIngresoPicker.getValue());
	            producto.setMarca(marcaField.getText());
	            producto.setStockMinimo(Integer.parseInt(stockMinimoField.getText()));

	            // Aquí puedes llamar al DAO para actualizar el producto en la base de datos
	            ProductosDAO.actualizarProducto(producto);

	            // Cerrar ventana
	            ((Stage) nombreField.getScene().getWindow()).close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Mostrar alerta si hay errores
	        }
	    }
	    public void initialize() {
	        cargarCategorias();
	    }
	    
	    private void cargarCategorias() {
	        List<String> categorias = ProductosDAO.obtenerCategorias();
	        comboCategorias.getItems().clear();
	        comboCategorias.getItems().addAll(categorias);
	    }
	}
