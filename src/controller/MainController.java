package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.ButtonBar;

public class MainController {

    @FXML
    private StackPane panelPrincipal;

    @FXML
    private void mostrarProductos() {
        cargarVista("/view/ProductsView.fxml");
    }

    @FXML
    private void mostrarEntradas() {
        cargarVista("/view/VerEntradasView.fxml");
    }

    @FXML
    private void mostrarSalidas() {
        cargarVista("/view/VerSalidasView.fxml");
    }

    @FXML
    private void registrarUsuarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de nuevo usuario");
            stage.setScene(new Scene(root));
            stage.show();
            
            System.out.println("Ventana de registro abierta.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al abrir registro.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void abrirAgregarEntrada() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tipo de entrada");
        alert.setHeaderText("¿Qué tipo de entrada desea registrar?");
        alert.setContentText("Seleccione una opción:");

        ButtonType btnExistente = new ButtonType("Producto existente");
        ButtonType btnNuevo = new ButtonType("Producto nuevo");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnExistente, btnNuevo, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == btnExistente) {
                abrirVentanaEntradaExistente();
            } else if (result.get() == btnNuevo) {
                abrirVentanaProductoNuevo();
            }
        }
    }
    
    private void abrirVentanaEntradaExistente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Entrada - Producto Existente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void abrirVentanaProductoNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEntradaview.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrar Producto Nuevo + Entrada");

            // Crear la escena con el contenido del FXML
            Scene scene = new Scene(root);

            // Establecer la escena
            stage.setScene(scene);

            // Configurar la ventana para que no sea redimensionable
            stage.setResizable(false);  // Puedes cambiar esto a false si no deseas que el usuario redimensione la ventana

            // Establecer un tamaño mínimo de la ventana, pero que la ventana se ajuste al contenido
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            // Asegurarse de que el tamaño de la ventana se ajuste al contenido (sin maximizar)
            stage.sizeToScene();

            // Mostrar la ventana como modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrar() {
        System.exit(0);
    }
    @FXML
    private void onVerEntradasClick(ActionEvent event) {
        cargarVista("/view/VerEntradasView.fxml"); // Asegúrate de que la ruta sea correcta
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Pane nuevaVista = loader.load();
            panelPrincipal.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private Menu menuAgregar;

    
    @FXML
    private void initialize() {
    	
    	cargarVista("/view/HomeView.fxml");
    	
    	System.out.println("ROL ACTUAL: " + Sesion.getRolUsuario()); // << DEBUG
        // Verificar el rol del usuario actual y actualizar el menú
        if (!Sesion.getRolUsuario().equals("admin")) {
            // Si no es admin, deshabilitar el menú de agregar usuario
            menuAgregar.setVisible(false);
        }
    }
}
