package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MainController {

    @FXML
    private StackPane panelPrincipal;

    @FXML
    private void mostrarProductos() {
        cargarVista("/view/ProductosView.fxml");
    }

    @FXML
    private void mostrarEntradas() {
        cargarVista("/view/EntradasView.fxml");
    }

    @FXML
    private void mostrarSalidas() {
        cargarVista("/view/SalidasView.fxml");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("/view/ReportesView.fxml");
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
            
            System.out.println("Ventana de registro abierta.");  // para depuración

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al abrir registro.fxml: " + e.getMessage());
        }
    }


    @FXML
    private void handleCerrar() {
        System.exit(0);
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProductsView.fxml"));
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
    	
    	System.out.println("ROL ACTUAL: " + Sesion.getRolUsuario()); // << DEBUG
        // Verificar el rol del usuario actual y actualizar el menú
        if (!Sesion.getRolUsuario().equals("admin")) {
            // Si no es admin, deshabilitar el menú de agregar usuario
            menuAgregar.setVisible(false);
        }
    }
    
}
