package controller;

import database.ProductosDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Productos;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsController {

    @FXML private TextField campoBusqueda;
    @FXML private FlowPane contenedorProductos;

    private ObservableList<Productos> productList;

    @FXML
    public void initialize() {
        List<Productos> productosBD = ProductosDAO.obtenerProductos();
        productList = FXCollections.observableArrayList(productosBD);
        mostrarProductos(productList);

        campoBusqueda.textProperty().addListener((obs, oldText, newText) -> {
            filtrarProductos(newText);
        });
    }

    private void filtrarProductos(String texto) {
        String lower = texto.toLowerCase().trim();
        List<Productos> filtrados = productList.stream()
            .filter(p -> p.getNombre().get().toLowerCase().contains(lower) ||
                         p.getCategoria().get().toLowerCase().contains(lower) ||
            			 p.getCodigo().get().toLowerCase().contains(lower))
            .collect(Collectors.toList());
        mostrarProductos(FXCollections.observableArrayList(filtrados));
    }

    private void mostrarProductos(ObservableList<Productos> productos) {
        contenedorProductos.getChildren().clear();
        for (Productos producto : productos) {
            VBox tarjeta = crearTarjetaProducto(producto);
            contenedorProductos.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaProducto(Productos producto) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPrefWidth(250);
        tarjeta.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 10; -fx-background-radius: 8; -fx-background-color: #f9f9f9;");

        Label nombreLabel = new Label(producto.getNombre().get());
        nombreLabel.setFont(new Font("Arial", 16));
        nombreLabel.setTextFill(Color.DARKBLUE);

        Label codigoLabel = new Label("Código: " + producto.getCodigo().get());
        Label categoriaLabel = new Label("Categoría: " + producto.getCategoria().get());
        Label descripcionLabel = new Label("Descripción: " + producto.getDescripcion().get());
        Label stockLabel = new Label("Stock: " + producto.getStock().get());

        HBox acciones = new HBox(10);
        acciones.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnAgregarEntrada = new Button("➕");
        btnAgregarEntrada.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");
        btnAgregarEntrada.setOnAction(e -> abrirVentanaAgregarEntradaExistente(producto));
        acciones.getChildren().add(0, btnAgregarEntrada); // Lo añadimos al principio del HBox

        Button btnEditar = new Button("✏️");
        btnEditar.setStyle("-fx-background-color: transparent;");
        btnEditar.setOnAction(e -> handleEditarProducto(producto));

        Button btnEliminar = new Button("❌");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: red;");
        btnEliminar.setOnAction(e -> handleEliminarProducto(producto));

        acciones.getChildren().addAll(btnEditar, btnEliminar);

        tarjeta.getChildren().addAll(nombreLabel, codigoLabel, categoriaLabel, descripcionLabel, stockLabel, acciones);
        return tarjeta;
    }

    private void handleEditarProducto(Productos producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditarProductoView.fxml"));
            Parent root = loader.load();

            EditarProductoController controller = loader.getController();
            controller.setProducto(producto);

            Stage stage = new Stage();
            stage.setTitle("Editar producto");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEliminarProducto(Productos producto) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que quieres eliminar el producto: " + producto.getNombre() + "?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                boolean exito = ProductosDAO.eliminarProducto(producto.getId());
                if (exito) {
                    productList.remove(producto);
                    mostrarProductos(productList);
                } else {
                    showAlert("Error", "No se pudo eliminar el producto.");
                }
            }
        });
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    private void abrirVentanaAgregarEntradaExistente(Productos producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AgregarEntradaExistente.fxml"));
            Parent root = loader.load();

            AgregarEntradaExistenteController controller = loader.getController();
            controller.setProducto(producto); // Le pasas el producto correspondiente

            Stage stage = new Stage();
            stage.setTitle("Agregar Entrada - Producto Existente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refrescar la lista después de agregar entrada
            List<Productos> productosBD = ProductosDAO.obtenerProductos();
            productList.setAll(productosBD);
            mostrarProductos(productList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirVentanaAgregarProductoNuevo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refrescar productos después de registrar uno nuevo
            List<Productos> productosBD = ProductosDAO.obtenerProductos();
            productList.setAll(productosBD);
            mostrarProductos(productList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
