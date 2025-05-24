package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    
    	public void start(Stage primaryStage) throws Exception {
    	 // Cargar la vista de Login (LoginView.fxml)
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(loader.load());  // Crear la escena con el archivo FXML
        
     // ✅ Agrega el CSS a la escena
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        primaryStage.setTitle("Login - Gestión de Almacén");  // Establecer el título de la ventana
        primaryStage.setScene(scene);  // Asignar la escena a la ventana
        primaryStage.show();  // Mostrar la ventana
    	}
    	 
    	public static void main(String[] args) {
    		launch(args);
    	}
}
