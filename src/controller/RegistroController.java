package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegistroController{

	@FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;
    @FXML private ComboBox<String> comboRol;

    @FXML
    public void registrarUsuario(ActionEvent event) {
    	String nombre = txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        String rol = comboRol.getValue();

        if (usuario.isEmpty() || contraseña.isEmpty() || rol == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Completa todos los campos.");
            return;
        }


        String sql = "INSERT INTO usuarios (nombre, usuario, contraseña, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String contraseñaHasheada = BCrypt.hashpw(contraseña, BCrypt.gensalt());
        	stmt.setString(1, nombre);
            stmt.setString(2, usuario);
            stmt.setString(3, contraseñaHasheada);
            stmt.setString(4, rol);
            stmt.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Usuario registrado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
    	txtNombre.clear();
        txtUsuario.clear();
        txtContraseña.clear();
        comboRol.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML
    public void initialize() {
        comboRol.setItems(FXCollections.observableArrayList("admin", "usuario"));
    }
    
}

