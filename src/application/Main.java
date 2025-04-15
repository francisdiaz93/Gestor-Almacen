package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    
    	public void start(Stage primaryStage) throws Exception {
    	 // Cargar la vista de Login (LoginView.fxml)
       /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(loader.load());  // Crear la escena con el archivo FXML
        primaryStage.setTitle("Login - Gestión de Almacén");  // Establecer el título de la ventana
        primaryStage.setScene(scene);  // Asignar la escena a la ventana
        primaryStage.show();  // Mostrar la ventana*/
    	
    		//Carga el archivo FXML donde tiene toda la interfaz principal.
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
    		Scene scene = new Scene(loader.load());
    		
    		scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
    		primaryStage.setTitle("Gestión de Almacén");
    		primaryStage.setScene(scene);
    		
    		// Maximizar la ventana al iniciar
            primaryStage.setMaximized(true);
        	primaryStage.show();
    	}
    	 
    	public static void main(String[] args) {
    		launch(args);
    	}
}
