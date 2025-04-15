package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML
    private StackPane panelPrincipal;

    @FXML
    private void mostrarProductos() {
        cargarVista("/view/ProductosView.fxml");
    }
    
   /* private void añadirProductos() {
    	cargarVista("/view/ProductosView.fxml");
    }*/

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
    
    /*public void initialize() {
        // Cargar el logo al iniciar
        Image logo = new Image(getClass().getResource("/images/LOGO.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(300); // Ajusta el tamaño si quieres
        logoView.setPreserveRatio(true);
    }*/
}
