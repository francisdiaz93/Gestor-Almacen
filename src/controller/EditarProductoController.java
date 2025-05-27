package controller;

import model.Productos;

import java.io.File;
import java.util.List;

import database.ProductosDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
	    @FXML private ComboBox<String> comboCategorias;
	    @FXML private ImageView imagenPreview;
	    private File archivoImagen;

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
	            producto.setCategoria(comboCategorias.getValue());

	            if (archivoImagen != null) {
	                producto.setImagen(archivoImagen.getAbsolutePath());
	            }

	            ProductosDAO.actualizarProducto(producto);

	            // Mostrar alerta de confirmación
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Éxito");
	            alert.setHeaderText(null);
	            alert.setContentText("El producto se ha editado correctamente.");
	            alert.showAndWait();

	            ((Stage) nombreField.getScene().getWindow()).close();
	        } catch (Exception e) {
	            e.printStackTrace();

	            // Alerta de error opcional
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("No se pudo guardar el producto");
	            alert.setContentText("Verifica que todos los campos estén correctos.");
	            alert.showAndWait();
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
