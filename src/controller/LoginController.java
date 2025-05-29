package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {

	    // Elementos de la interfaz de usuario que se enlazan con los controles en el FXML
	    @FXML private TextField txtUsuario; 
	    @FXML private PasswordField txtContrasena;
	    @FXML private Label lblMensaje;
	    @FXML
	    private AnchorPane rootPane;

	    @FXML
	    private void handleLogin(ActionEvent event) {
	        String usuario = txtUsuario.getText();
	        String contrasena = txtContrasena.getText();

	        if (usuario.isEmpty() || contrasena.isEmpty()) {
	            lblMensaje.setText("Rellena todos los campos.");
	            return;
	        }

	        try (Connection conn = DatabaseConnection.getConnection()) {
	            String query = "SELECT * FROM usuarios WHERE usuario = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, usuario);

	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String contrasenaHasheada = rs.getString("contraseña");

	                if (BCrypt.checkpw(contrasena, contrasenaHasheada)) {
	                    int idUsuario = rs.getInt("id");
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
	                    lblMensaje.setText("Credenciales incorrectas.");
	                }
	            } else {
	                lblMensaje.setText("Credenciales incorrectas.");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            lblMensaje.setText("Error de conexión.");
	        }
	    }
	}


