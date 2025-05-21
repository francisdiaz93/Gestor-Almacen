package controller;

import model.Productos;
import database.ProductosDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteProductController {

    @FXML
    private TextField productCode;  // Campo para el código del producto
    @FXML
    private TextField productName;  // Campo para el nombre del producto

    // Método para manejar la eliminación del producto
    @FXML
    public void handleDeleteProduct(ActionEvent event) {
        String codigo = productCode.getText().trim();
        String nombre = productName.getText().trim();

        if (!codigo.isEmpty() && !nombre.isEmpty()) {
            // Buscar el producto en la base de datos
            Productos productToDelete = ProductosDAO.buscarProductoPorCodigo(codigo);

            if (productToDelete != null) {
                // Eliminar el producto
            	ProductosDAO.eliminarProducto(productToDelete.getId()); // Asegúrate de que este método esté disponible

                // Mostrar un mensaje de confirmación
                showAlert("Producto eliminado", "El producto ha sido eliminado correctamente.");

                // Cerrar la ventana
                closeWindow(event);
            } else {
                showAlert("Error", "No se encontró un producto con ese código y nombre.");
            }
        } else {
            showAlert("Error", "Por favor, ingrese tanto el código como el nombre del producto.");
        }
    }

    // Método para manejar la cancelación (cerrar la ventana sin eliminar)
    @FXML
    public void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    // Método para cerrar la ventana
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}