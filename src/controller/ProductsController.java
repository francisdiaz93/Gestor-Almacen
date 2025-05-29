package controller;

import database.ProductosDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Productos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsController {

    @FXML private TextField campoBusqueda;
    @FXML private FlowPane contenedorProductos;
    @FXML private Button btnAgregarEntrada;

    private ObservableList<Productos> productList;
    private ObservableList<Productos> productosMostrados; // Lista visible (filtrados)

    @FXML
    public void initialize() {
        List<Productos> productosBD = ProductosDAO.obtenerProductos();
        productList = FXCollections.observableArrayList(productosBD);
        productosMostrados = FXCollections.observableArrayList(productList);
        mostrarProductos(productosMostrados);

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
        productosMostrados.setAll(filtrados);
        mostrarProductos(productosMostrados);
    }

    private void mostrarProductos(ObservableList<Productos> productos) {
        contenedorProductos.getChildren().clear();
        for (Productos producto : productos) {
            VBox tarjeta = crearTarjetaProducto(producto);
            contenedorProductos.getChildren().add(tarjeta);
        }
    }
    
    private void recargarProductosDesdeBD() {
        List<Productos> productosBD = ProductosDAO.obtenerProductos();
        productList.setAll(productosBD);
        productosMostrados.setAll(productList);
        mostrarProductos(productosMostrados);
    }

    private VBox crearTarjetaProducto(Productos producto) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPrefWidth(260);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);"
        );
        
     // Cambiar el estilo según stock mínimo
        if (producto.getStock().get() < producto.getStockMinimo().get()) {
            tarjeta.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 10; -fx-background-radius: 8; -fx-background-color: #ffcccc;");
        } else {
            tarjeta.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 10; -fx-background-radius: 8; -fx-background-color: #f9f9f9;");
        }

        tarjeta.setUserData(producto);
        
     // Abrir detalles al hacer doble clic en la tarjeta
        tarjeta.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                abrirVentanaDetalleProducto(producto);
            }
        });

        // Imagen del producto centrada y con bordes redondeados
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        String ruta = producto.getImagen();
        Image imagenProducto;
        if (ruta != null && !ruta.isEmpty() && new File(ruta).exists()) {
            try {
                imagenProducto = new Image(new File(ruta).toURI().toString());
            } catch (Exception e) {
                imagenProducto = new Image(getClass().getResourceAsStream("/assets/no-image.png"));
            }
        } else {
            imagenProducto = new Image(getClass().getResourceAsStream("/assets/no-image.png"));
        }
        imageView.setImage(imagenProducto);

        // Labels con estilos
        Label nombreLabel = new Label(producto.getNombre().get());
        nombreLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        nombreLabel.setTextFill(Color.web("#2C3E50"));
        nombreLabel.setWrapText(true);
        nombreLabel.setTextAlignment(TextAlignment.CENTER);

        Label codigoLabel = new Label("Código: " + producto.getCodigo().get());
        codigoLabel.setFont(Font.font("Segoe UI", 13));
        codigoLabel.setTextFill(Color.web("#34495E"));

        Label categoriaLabel = new Label("Categoría: " + producto.getCategoria().get());
        categoriaLabel.setFont(Font.font("Segoe UI", 13));
        categoriaLabel.setTextFill(Color.web("#34495E"));

        Label descripcionLabel = new Label(producto.getDescripcion().get());
        descripcionLabel.setFont(Font.font("Segoe UI", 12));
        descripcionLabel.setTextFill(Color.web("#7F8C8D"));
        descripcionLabel.setWrapText(true);
        descripcionLabel.setMaxHeight(40);

        Label stockLabel = new Label("Stock: " + producto.getStock().get());
        stockLabel.setFont(Font.font("Segoe UI", 14));
        if (producto.getStock().get() < producto.getStockMinimo().get()) {
            stockLabel.setTextFill(Color.RED);
        } else {
            stockLabel.setTextFill(Color.web("#27AE60"));
        }

        // Botones con iconos y estilos
        HBox acciones = new HBox(12);
        acciones.setAlignment(Pos.CENTER);

        Button btnAgregarEntrada = new Button("", new Label("➕"));
        btnAgregarEntrada.setStyle("-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: #27AE60");
        btnAgregarEntrada.setOnAction(e -> abrirVentanaAgregarEntradaExistente(producto));
        setBotonHoverEffect(btnAgregarEntrada, "#2ECC71", "#27AE60");

        Button btnEditar = new Button("", new Label("✏️"));
        btnEditar.setStyle("-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: #2980B9");
        btnEditar.setOnAction(e -> handleEditarProducto(producto));
        setBotonHoverEffect(btnEditar, "#3498DB", "#2980B9");

        Button btnEliminar = new Button("", new Label("❌"));
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: #C0392B");
        btnEliminar.setOnAction(e -> handleEliminarProducto(producto));
        setBotonHoverEffect(btnEliminar, "#E74C3C", "#C0392B");

        acciones.getChildren().addAll(btnAgregarEntrada, btnEditar, btnEliminar);

        // Añadir nodos a la tarjeta
        tarjeta.getChildren().addAll(imageView, nombreLabel, codigoLabel, categoriaLabel, descripcionLabel, stockLabel, acciones);

        return tarjeta;
    }

    // Método auxiliar para efecto hover en botones
    private void setBotonHoverEffect(Button boton, String colorHover, String colorNormal) {
        boton.setOnMouseEntered(e -> boton.setStyle(
            "-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: " + colorHover + "; -fx-cursor: hand;"));
        boton.setOnMouseExited(e -> boton.setStyle(
            "-fx-background-color: transparent; -fx-font-size: 20; -fx-text-fill: " + colorNormal + ";"));
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // ✅ Recargar productos después de editar
            recargarProductosDesdeBD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEliminarProducto(Productos producto) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que quieres eliminar el producto: " + producto.getNombre().get() + "?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                boolean exito = ProductosDAO.eliminarProducto(producto.getId());
                if (exito) {
                    productList.remove(producto);
                    productosMostrados.remove(producto);
                    mostrarProductos(productosMostrados);
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
            controller.setProducto(producto);

            Stage stage = new Stage();
            stage.setTitle("Agregar Entrada - Producto Existente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // ✅ Recargar productos después de añadir entrada
            recargarProductosDesdeBD();
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

            // ✅ Recargar productos después de añadir
            recargarProductosDesdeBD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExportarPDF(ActionEvent event) {
        ExportadorPDF.exportarProductosAPDF(productosMostrados, (Stage) contenedorProductos.getScene().getWindow());
    }
    
 // Método para abrir ventana detalle
    private void abrirVentanaDetalleProducto(Productos producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetalleProductoView.fxml"));
            Parent root = loader.load();

            DetalleProductoController controller = loader.getController();
            controller.setProducto(producto);

            Stage stage = new Stage();
            stage.setTitle("Detalle del producto: " + producto.getNombre().get());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea ventana padre
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Aquí puedes mostrar diálogo de error si quieres
        }
    }
}