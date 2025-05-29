package controller;

import database.UsuariosDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Usuarios;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuariosController implements Initializable {

    @FXML private TableView<Usuarios> tablaUsuarios;
    @FXML private TableColumn<Usuarios, Integer> colId;
    @FXML private TableColumn<Usuarios, String> colNombre;
    @FXML private TableColumn<Usuarios, String> colUsuario;
    @FXML private TableColumn<Usuarios, String> colRol;

    private final UsuariosDAO usuariosDAO = new UsuariosDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));


        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<Usuarios> usuarios = usuariosDAO.obtenerUsuarios();
        tablaUsuarios.getItems().setAll(usuarios);
    } 
}