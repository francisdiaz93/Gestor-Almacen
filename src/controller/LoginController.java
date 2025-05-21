package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {

	    // Elementos de la interfaz de usuario que se enlazan con los controles en el FXML
	    @FXML private TextField txtUsuario;  // Campo para ingresar el usuario
	    @FXML private PasswordField txtContrasena;  // Campo para ingresar la contraseña
	    @FXML private Label lblMensaje;  // Label para mostrar mensajes de error o éxito

	    // Método que se ejecuta cuando el usuario hace clic en el botón "Iniciar sesión"
	    @FXML
	    private void handleLogin(ActionEvent event) {
	        // Obtener los valores ingresados por el usuario
	        String usuario = txtUsuario.getText();
	        String contrasena = txtContrasena.getText();

	        // Validar si los campos no están vacíos
	        if (usuario.isEmpty() || contrasena.isEmpty()) {
	            lblMensaje.setText("Rellena todos los campos.");
	            return;
	        }

	        // Conexión a la base de datos y validación de las credenciales
	        try (Connection conn = DatabaseConnection.getConnection()) {

	            // Consulta SQL para verificar las credenciales en la base de datos
	            String query = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, usuario);  // Establecer el nombre de usuario en la consulta
	            stmt.setString(2, contrasena);  // Establecer la contraseña en la consulta

	            // Ejecutar la consulta
	            ResultSet rs = stmt.executeQuery();

	            // Si se encuentran los resultados (usuario y contraseña coinciden)
	            if (rs.next()) {
	                int idUsuario = rs.getInt("id");  // Obtener id del usuario
	                String nombreUsuario = rs.getString("nombre");
	                String rol = rs.getString("rol");

	                Sesion.iniciarSesion(idUsuario, nombreUsuario, rol);

	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
	                Parent root = loader.load();
	                Stage stage = (Stage) txtUsuario.getScene().getWindow();
	                Scene scene = new Scene(root);
	                stage.setScene(scene);
	                stage.setMaximized(true);
	                stage.show();
	            
	            } else {
	                // Si las credenciales no son correctas, mostrar mensaje de error
	                lblMensaje.setText("Credenciales incorrectas.");
	            }

	        } catch (Exception e) {
	            // Si hay algún error de conexión o en la consulta, mostrar mensaje de error
	            e.printStackTrace();
	            lblMensaje.setText("Error de conexión.");
	        }
	    }
	}


