package database;

import model.Entradas;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradasDAO {

    // Método para insertar una nueva entrada con transacción
    public static boolean insertarEntrada(Entradas entrada) {
        String queryEntrada = "INSERT INTO entradas (producto_id, cantidad, fecha_ingreso, proveedor, usuario_id, numero_factura, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            // Obtener la conexión a la base de datos
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Desactivar autocommit para usar transacciones

            // Insertar en la tabla entradas
            try (PreparedStatement stmt = conn.prepareStatement(queryEntrada)) {
                stmt.setInt(1, entrada.getProductoId());
                stmt.setInt(2, entrada.getCantidad());
                stmt.setDate(3, new java.sql.Date(entrada.getFechaIngreso().getTime()));
                stmt.setString(4, entrada.getProveedor());
                stmt.setInt(5, entrada.getUsuarioId());
                stmt.setString(6, entrada.getNumeroFactura());
                stmt.setString(7, entrada.getObservaciones());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted <= 0) {
                    conn.rollback(); // Revertir si no se insertó la entrada
                    return false;
                }
            }

            // Actualizar el producto si es necesario (Ejemplo de actualización de stock)
            String queryActualizarProducto = "UPDATE productos SET cantidad = cantidad + ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryActualizarProducto)) {
                stmt.setInt(1, entrada.getCantidad()); // Sumamos la cantidad de la entrada al stock del producto
                stmt.setInt(2, entrada.getProductoId());
                stmt.executeUpdate();
            }

            conn.commit(); // Confirmar toda la transacción
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir cambios si ocurre un error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar el comportamiento por defecto
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Método para actualizar una entrada existente
    public boolean actualizarEntrada(Entradas entrada) {
        String query = "UPDATE entradas SET producto_id = ?, cantidad = ?, fecha_ingreso = ?, proveedor = ?, usuario_id = ?, " +
                "numero_factura = ?, observaciones = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, entrada.getProductoId());
            stmt.setInt(2, entrada.getCantidad());
            stmt.setDate(3, new java.sql.Date(entrada.getFechaIngreso().getTime()));
            stmt.setString(4, entrada.getProveedor());
            stmt.setInt(5, entrada.getUsuarioId());
            stmt.setString(6, entrada.getNumeroFactura());
            stmt.setString(7, entrada.getObservaciones());
            stmt.setInt(8, entrada.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Si la actualización fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar una entrada por ID
    public boolean eliminarEntrada(int id) {
        String query = "DELETE FROM entradas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; // Si la eliminación fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener una entrada por su ID
    public Entradas obtenerEntradaPorId(int id) {
        String query = "SELECT * FROM entradas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Entradas(
                        rs.getInt("id"),
                        new SimpleIntegerProperty(rs.getInt("producto_id")),
                        new SimpleIntegerProperty(rs.getInt("cantidad")),
                        rs.getDate("fecha_ingreso"),
                        rs.getString("proveedor"),
                        rs.getInt("usuario_id"),
                        rs.getString("numero_factura"),
                        rs.getString("observaciones")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra la entrada
    }

    // Método para obtener todas las entradas
    public static List<Entradas> obtenerTodasLasEntradas() {
        List<Entradas> entradasList = new ArrayList<>();
        String query = "SELECT * FROM entradas";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Entradas entrada = new Entradas(
                        rs.getInt("id"),
                        new SimpleIntegerProperty(rs.getInt("producto_id")),
                        new SimpleIntegerProperty(rs.getInt("cantidad")),
                        rs.getDate("fecha_ingreso"),
                        rs.getString("proveedor"),
                        rs.getInt("usuario_id"),
                        rs.getString("numero_factura"),
                        rs.getString("observaciones")
                );
                entradasList.add(entrada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entradasList;
    }
}

