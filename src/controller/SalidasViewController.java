package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import model.Salidas;
import database.ProductosDAO;
import database.SalidasDAO;
import database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.stage.FileChooser;
import java.io.File;


public class SalidasViewController {

    @FXML private TableView<Salidas> salidasTable;

    @FXML private TableColumn<Salidas, String> colNombreProducto;
    @FXML private TableColumn<Salidas, String> colCodigoProducto;
    @FXML private TableColumn<Salidas, String> colDepartamento;
    @FXML private TableColumn<Salidas, String> colCantidad;
    @FXML private TableColumn<Salidas, String> colFecha;
    @FXML private TableColumn<Salidas, String> colMotivo;
    @FXML private TableColumn<Salidas, String> colNumeroFactura;

    @FXML private TextField buscarField;

    @FXML private Button btnAgregarSalida;
    @FXML private Button btnExportarPDF;

    @FXML private TextField codigoProductoField;
    @FXML private TextField departamentoField;
    @FXML private TextField cantidadField;
    @FXML private DatePicker fechaSalidaPicker;
    @FXML private TextField motivoField;

    private ObservableList<Salidas> listaSalidas;

    @FXML
    public void initialize() {
        listaSalidas = FXCollections.observableArrayList();
        cargarSalidas();
        
        btnExportarPDF.setOnAction(e -> exportarSalidasAPDF());


        colCodigoProducto.setCellValueFactory(data ->
            new SimpleStringProperty(obtenerCodigoProducto(data.getValue().getProductoId()))
        );
        colNombreProducto.setCellValueFactory(data ->
            new SimpleStringProperty(obtenerNombreProducto(data.getValue().getProductoId()))
        );
        colDepartamento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartamento()));
        colCantidad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCantidad())));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaSalida().toString()));
        colMotivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotivo()));
        colNumeroFactura.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroFactura()));

        salidasTable.setItems(listaSalidas);

        // Listener para el campo buscarField para filtrar la tabla en tiempo real
        buscarField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarSalidas(newValue);
        });
    }

    private void cargarSalidas() {
        listaSalidas.setAll(SalidasDAO.obtenerSalidas());
    }

    private void filtrarSalidas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            salidasTable.setItems(listaSalidas);
            return;
        }

        ObservableList<Salidas> listaFiltrada = FXCollections.observableArrayList();
        String filtroLower = filtro.toLowerCase();

        for (Salidas s : listaSalidas) {
            String nombreProducto = obtenerNombreProducto(s.getProductoId()).toLowerCase();
            String numeroFactura = s.getNumeroFactura() != null ? s.getNumeroFactura().toLowerCase() : "";
            String departamento = s.getDepartamento() != null ? s.getDepartamento().toLowerCase() : "";

            if (nombreProducto.contains(filtroLower) || numeroFactura.contains(filtroLower) || departamento.contains(filtroLower)) {
                listaFiltrada.add(s);
            }
        }

        salidasTable.setItems(listaFiltrada);
    }

    private String obtenerCodigoProducto(int productoId) {
        String codigo = "";
        String sql = "SELECT codigo FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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

    private String obtenerNombreProducto(int productoId) {
        String nombre = "";
        String sql = "SELECT nombre FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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

    private int obtenerIdPorCodigo(String codigo) {
        int id = -1;
        String sql = "SELECT id FROM productos WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el ID del producto: " + e.getMessage());
        }
        return id;
    }

    @FXML
    private void handleAgregarSalida() throws SQLException {
        try {
            String codigoProducto = codigoProductoField.getText().trim();
            if (codigoProducto.isEmpty()) {
                mostrarAlerta("Campo vacío", "Ingrese el código del producto.");
                return;
            }

            int productoId = obtenerIdPorCodigo(codigoProducto);
            if (productoId == -1) {
                mostrarAlerta("Producto no encontrado", "No existe un producto con ese código.");
                return;
            }

            String departamento = departamentoField.getText().trim();
            int cantidad = Integer.parseInt(cantidadField.getText().trim());
            LocalDate fechaLocal = fechaSalidaPicker.getValue();
            String motivo = motivoField.getText().trim();

            if (departamento.isEmpty() || motivo.isEmpty() || fechaLocal == null) {
                mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos.");
                return;
            }

            // Obtener stock actual y mínimo
            int stockActualAntes = ProductosDAO.obtenerStockActual(productoId);
            int stockMinimo = ProductosDAO.obtenerStockMinimo(productoId);

            System.out.println("Stock actual antes: " + stockActualAntes);
            System.out.println("Stock mínimo: " + stockMinimo);
            System.out.println("Cantidad a restar: " + cantidad);

            if (cantidad > stockActualAntes) {
                mostrarAlerta("Stock insuficiente", "No hay suficiente stock para esta salida.");
                return;
            }

            // Registrar salida
            String numeroFactura = "S" + System.currentTimeMillis();

            Salidas salida = new Salidas();
            salida.setProductoId(productoId);
            salida.setDepartamento(departamento);
            salida.setCantidad(cantidad);
            salida.setFechaSalida(java.sql.Date.valueOf(fechaLocal));
            salida.setMotivo(motivo);
            salida.setUsuarioId(Sesion.getUsuarioId());
            salida.setNumeroFactura(numeroFactura);

            boolean exito = SalidasDAO.agregarSalida(salida);

            if (exito) {
                boolean stockActualizado = ProductosDAO.restarStock(productoId, cantidad);

                if (stockActualizado) {
                    int stockActualDespues = ProductosDAO.obtenerStockActual(productoId);
                    System.out.println("Stock actual después: " + stockActualDespues);

                    if (stockActualDespues < stockMinimo) {
                        mostrarAlertaStockBajo(productoId, stockActualDespues, stockMinimo);
                    }

                    listaSalidas.add(salida);
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error al actualizar stock", "No se pudo actualizar el stock.");
                }
            } else {
                mostrarAlerta("Error al registrar", "No se pudo guardar la salida en la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Datos inválidos", "Verifique que la cantidad sea un número válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado", "Ocurrió un error inesperado.");
        }
    }


    private void mostrarAlertaStockBajo(int productoId, int stockActual, int stockMinimo) {
        String nombre = ProductosDAO.obtenerNombreProducto(productoId);

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Stock bajo mínimo");
        alerta.setHeaderText("Producto: " + nombre);
        alerta.setContentText("Stock actual: " + stockActual + "\nStock mínimo: " + stockMinimo);
        alerta.showAndWait();
    }


    private void limpiarCampos() {
        codigoProductoField.clear();
        departamentoField.clear();
        cantidadField.clear();
        fechaSalidaPicker.setValue(null);
        motivoField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void abrirFormularioAgregarSalida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AgregarSalidaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Salida");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarSalidas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirAgregarSalida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AgregarSalidaView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nueva Salida");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarSalidas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void exportarSalidasAPDF() {
    	List<Salidas> salidasVisibles = new ArrayList<>(salidasTable.getItems()); // tu lista con las salidas

        try (PDDocument document = new PDDocument()) {
            PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Título
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, landscape.getHeight() - 50);
            contentStream.showText("Reporte de Salidas");
            contentStream.endText();

            float margin = 50;
            float yStart = landscape.getHeight() - 80;
            float yPosition = yStart;
            float rowHeight = 20;
            float tableWidth = landscape.getWidth() - 2 * margin;

            // Columnas para Salidas
            String[] columnas = {"Fecha Salida", "ID", "Producto", "Departamento", "Motivo", "Factura", "Cantidad"};
            float[] colProportions = {0.10f, 0.06f, 0.26f, 0.16f, 0.15f, 0.20f, 0.08f};

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

            for (Salidas salida : salidasVisibles) {
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

                // Convertir fecha salida a String usando toString() para solo fecha sin hora
                String fechaSalida = salida.getFechaSalida() != null ? salida.getFechaSalida().toString() : "";
                String id = String.valueOf(salida.getId());
                String producto = obtenerNombreProducto(salida.getProductoId());
                String departamento = salida.getDepartamento() != null ? salida.getDepartamento() : "";
                String motivo = salida.getMotivo() != null ? salida.getMotivo() : "";
                String factura = salida.getNumeroFactura() != null ? salida.getNumeroFactura() : "";
                String cantidad = String.valueOf(salida.getCantidad());

                String[] valores = {fechaSalida, id, producto, departamento, motivo, factura, cantidad};

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
            File archivo = fileChooser.showSaveDialog(salidasTable.getScene().getWindow());

            if (archivo != null) {
                document.save(archivo);
                mostrarAlerta("Exportación exitosa", "Reporte de salidas exportado correctamente.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "Error al exportar el reporte de salidas.");
        }
    }

}

