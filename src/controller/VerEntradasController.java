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
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class VerEntradasController {
	
	@FXML
	private Button cerrarButton;
	
	@FXML
    private Button btnExportarPDF;
    
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
    
    // --- Conexión DB ---
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/almacen_db", "root", "");
    }
    
    // Obtener nombre producto por id
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
    
    // Obtener código producto por id
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
    
    // Obtener nombre usuario por id para reporte PDF
    private String obtenerNombreUsuario(int usuarioId) {
        String nombre = "";
        String sql = "SELECT nombre FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre del usuario: " + e.getMessage());
        }
        return nombre;
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

        // Listener para filtrar entradas según texto
        buscadorTextField.textProperty().addListener((obs, oldText, newText) -> filtrarEntradas(newText));
        
        // Asocia el botón de exportar PDF
        btnExportarPDF.setOnAction(event -> exportarEntradasAPDF());
    }
    
    @FXML
    private void exportarEntradasAPDF() {
        List<Entradas> entradasVisibles = tableView.getItems();

        try (PDDocument document = new PDDocument()) {
            PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Título
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, landscape.getHeight() - 50);
            contentStream.showText("Reporte de Entradas");
            contentStream.endText();

            float margin = 50;
            float yStart = landscape.getHeight() - 80;
            float yPosition = yStart;
            float rowHeight = 20;
            float tableWidth = landscape.getWidth() - 2 * margin;

            // NUEVO ORDEN DE COLUMNAS
            String[] columnas = {"Fecha Ingreso", "ID", "Producto", "Proveedor", "Cantidad", "Factura"};
            float[] colProportions = {0.22f, 0.08f, 0.24f, 0.20f, 0.12f, 0.14f};

            float[] colWidths = new float[colProportions.length];
            for (int i = 0; i < colProportions.length; i++) {
                colWidths[i] = tableWidth * colProportions[i];
            }

            // Encabezados
            yPosition -= rowHeight;
            float xPosition = margin;
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            for (int i = 0; i < columnas.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition + 5);
                contentStream.showText(columnas[i]);
                contentStream.endText();
                xPosition += colWidths[i];
            }

            // Línea bajo encabezados
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();

            yPosition -= rowHeight;
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            for (Entradas entrada : entradasVisibles) {
                if (yPosition < margin) {
                    contentStream.close();

                    page = new PDPage(landscape);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);

                    yPosition = yStart;

                    // Redibujar encabezados en nueva página
                    xPosition = margin;
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    for (int i = 0; i < columnas.length; i++) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition + 2, yPosition + 5);
                        contentStream.showText(columnas[i]);
                        contentStream.endText();
                        xPosition += colWidths[i];
                    }
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition);
                    contentStream.stroke();

                    yPosition -= rowHeight;
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                }

                xPosition = margin;

                // NUEVO ORDEN DE VALORES
                String fechaIngreso = entrada.getFechaIngreso() != null ? entrada.getFechaIngreso().toString() : "";
                String id = String.valueOf(entrada.getId());
                String producto = obtenerNombreProducto(entrada.getProductoId());
                String proveedor = entrada.getProveedor() != null ? entrada.getProveedor() : "";
                String cantidad = String.valueOf(entrada.getCantidad());
                String factura = entrada.getNumeroFactura() != null ? entrada.getNumeroFactura() : "";

                String[] valores = {fechaIngreso, id, producto, proveedor, cantidad, factura};

                for (int i = 0; i < valores.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition + 2, yPosition + 5);
                    contentStream.showText(valores[i]);
                    contentStream.endText();
                    xPosition += colWidths[i];
                }

                yPosition -= rowHeight;
            }

            contentStream.close();

            // Guardar PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar reporte PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
            File archivo = fileChooser.showSaveDialog(tableView.getScene().getWindow());

            if (archivo != null) {
                document.save(archivo);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana() {
    	Stage stage = (Stage) cerrarButton.getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    private void abrirFormularioAgregarEntrada(javafx.event.ActionEvent event) {
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
