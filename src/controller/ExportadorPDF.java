package controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Productos;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportadorPDF {

	public static void exportarProductosAPDF(List<Productos> productos, Stage stage) {
	    try (PDDocument document = new PDDocument()) {
	        PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
	        PDPage page = new PDPage(landscape);
	        document.addPage(page);

	        PDPageContentStream contentStream = new PDPageContentStream(document, page);

	        // Título
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
	        contentStream.beginText();
	        contentStream.newLineAtOffset(50, landscape.getHeight() - 50);
	        contentStream.showText("Reporte de Productos en Stock");
	        contentStream.endText();

	        // Parámetros
	        float margin = 50;
	        float yStart = landscape.getHeight() - 80;
	        float yPosition = yStart;
	        float rowHeight = 20;
	        float tableWidth = landscape.getWidth() - 2 * margin;

	        // NUEVO ORDEN DE COLUMNAS
	        String[] columnas = {"Fecha Ingreso", "Nombre", "Código", "Categoría", "Proveedor", "Marca", "Cantidad"};
	        float[] colProportions = {0.15f, 0.20f, 0.13f, 0.13f, 0.18f, 0.13f, 0.08f};

	        // Calcular anchos
	        float[] colWidths = new float[colProportions.length];
	        for (int i = 0; i < colProportions.length; i++) {
	            colWidths[i] = tableWidth * colProportions[i];
	        }

	        // Dibujar encabezados
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

	        for (Productos producto : productos) {
	            if (yPosition < margin) {
	                contentStream.close();

	                page = new PDPage(landscape);
	                document.addPage(page);
	                contentStream = new PDPageContentStream(document, page);

	                yPosition = yStart;

	                // Redibujar encabezados
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
	            String[] valores = {
	                    producto.getFechaIngreso().get() != null ? producto.getFechaIngreso().get().toString() : "",
	                    producto.getNombre().get(),
	                    producto.getCodigo().get(),
	                    producto.getCategoria().get(),
	                    producto.getProveedor().get(),
	                    producto.getMarca().get(),
	                    String.valueOf(producto.getStock().get())
	            };

	            for (int i = 0; i < valores.length; i++) {
	                contentStream.beginText();
	                contentStream.newLineAtOffset(xPosition + 2, yPosition + 5);
	                contentStream.showText(valores[i]);
	                contentStream.endText();
	                xPosition += colWidths[i];
	            }

	            // Línea horizontal por fila
	            contentStream.moveTo(margin, yPosition);
	            contentStream.lineTo(margin + tableWidth, yPosition);
	            contentStream.stroke();

	            yPosition -= rowHeight;
	        }

	        // Líneas verticales
	        float xLine = margin;
	        contentStream.setLineWidth(0.5f);
	        contentStream.setStrokingColor(0, 0, 0);
	        float yLineTop = yStart;
	        float yLineBottom = yPosition + rowHeight;

	        for (float w : colWidths) {
	            contentStream.moveTo(xLine, yLineTop);
	            contentStream.lineTo(xLine, yLineBottom);
	            contentStream.stroke();
	            xLine += w;
	        }
	        contentStream.moveTo(margin + tableWidth, yLineTop);
	        contentStream.lineTo(margin + tableWidth, yLineBottom);
	        contentStream.stroke();

	        contentStream.close();

	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Guardar reporte PDF");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
	        File file = fileChooser.showSaveDialog(stage);

	        if (file != null) {
	            document.save(file);
	            System.out.println("Reporte PDF guardado en: " + file.getAbsolutePath());
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
