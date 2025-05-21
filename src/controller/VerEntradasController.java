package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Entradas;
import model.Salidas;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class VerEntradasController {
	
	@FXML
	private Button cerrarButton;
    @FXML
    private TableView<Entradas> tableView;
    @FXML
    private TableColumn<Entradas, String> colNombreProducto;
    @FXML
    private TableColumn<Entradas, String> colCodigoProducto;
    @FXML
    private TableColumn<Entradas, Integer> colCantidad;
    @FXML
    private TableColumn<Entradas, Date> colFechaIngreso;
    @FXML
    private TableColumn<Entradas, String> colProveedor;
    @FXML
    private TableColumn<Entradas, String> colNumeroFactura;
    @FXML
    private TableColumn<Entradas, String> colObservaciones;
    
    @FXML
    private TextField buscadorTextField;
    private ObservableList<Entradas> listaEntradas;
    
    // Método para establecer la conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/almacen_db", "root", "");
    }
    
    private String obtenerNombreProducto(int productoId) {
        String nombre = "";
        String sql = "SELECT nombre FROM productos WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre del producto: " + e.getMessage());
        }
        return nombre;
    }
    
    private String obtenerCodigoProducto(int productoId) {
        String codigo = "";
        String sql = "SELECT codigo FROM productos WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                codigo = rs.getString("codigo");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el código del producto: " + e.getMessage());
        }
        return codigo;
    }
    
    public void cargarEntradas() {
        ObservableList<Entradas> entradas = FXCollections.observableArrayList();

        String query = "SELECT * FROM entradas";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Entradas entrada = new Entradas();
                entrada.setFechaIngreso(rs.getDate("fecha_ingreso"));
                entrada.setId(rs.getInt("id"));
                entrada.setProductoId(rs.getInt("producto_id"));
                entrada.setCantidad(rs.getInt("cantidad"));
                entrada.setProveedor(rs.getString("proveedor"));
                entrada.setNumeroFactura(rs.getString("numero_factura"));
                entrada.setObservaciones(rs.getString("observaciones"));
                entrada.setUsuarioId(rs.getInt("usuario_id"));

                entradas.add(entrada);
            }

            // Actualiza la lista global y la tabla
            listaEntradas.clear();
            listaEntradas.addAll(entradas);
            tableView.setItems(listaEntradas);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void filtrarEntradas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.setItems(listaEntradas);
            return;
        }

        ObservableList<Entradas> listaFiltrada = FXCollections.observableArrayList();
        String filtroLower = filtro.toLowerCase();

        for (Entradas e : listaEntradas) {
            String nombreProducto = obtenerNombreProducto(e.getProductoId()).toLowerCase();
            String numeroFactura = e.getNumeroFactura().toLowerCase();

            if (nombreProducto.contains(filtroLower) || numeroFactura.contains(filtroLower)) {
                listaFiltrada.add(e);
            }
        }

        tableView.setItems(listaFiltrada);
    }


    @FXML
    public void initialize() {
        listaEntradas = FXCollections.observableArrayList();

        colCodigoProducto.setCellValueFactory(data -> 
            new SimpleStringProperty(obtenerCodigoProducto(data.getValue().getProductoId()))
        );

        colNombreProducto.setCellValueFactory(data ->
            new SimpleStringProperty(obtenerNombreProducto(data.getValue().getProductoId()))
        );

        colFechaIngreso.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        cargarEntradas();

        tableView.setItems(listaEntradas);

        // Listener para filtrar cuando escribes en el TextField
        buscadorTextField.textProperty().addListener((obs, oldText, newText) -> filtrarEntradas(newText));
    }

    
    @FXML
    private void abrirFormularioAgregarEntrada() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Entrada - Producto Nuevo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarEntradas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
