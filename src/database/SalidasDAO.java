package database;

import model.Salidas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalidasDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3307/almacen_db";
    private static final String USER = "root";
    private static final String PASS = "";

    public static List<Salidas> obtenerSalidas() {
        List<Salidas> lista = new ArrayList<>();
        String sql = "SELECT * FROM salidas ORDER BY fecha_salida DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Salidas s = new Salidas();
                s.setId(rs.getInt("id"));
                s.setProductoId(rs.getInt("producto_id"));
                s.setDepartamento(rs.getString("departamento"));
                s.setCantidad(rs.getInt("cantidad"));
                s.setFechaSalida(rs.getDate("fecha_salida"));
                s.setMotivo(rs.getString("motivo"));
                s.setUsuarioId(rs.getInt("usuario_id"));
                s.setNumeroFactura(rs.getString("numeroFactura"));
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean agregarSalida(Salidas salida) throws SQLException {
        String sqlInsert = "INSERT INTO salidas (producto_id, departamento, cantidad, fecha_salida, motivo, usuario_id, numeroFactura) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ? AND cantidad >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdateStock = conn.prepareStatement(sqlUpdateStock)
            ) {


                // Actualizar stock solo si hay suficiente cantidad
                stmtUpdateStock.setInt(1, salida.getCantidad());
                stmtUpdateStock.setInt(2, salida.getProductoId());
                stmtUpdateStock.setInt(3, salida.getCantidad());
                int filasAfectadas = stmtUpdateStock.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback();
                    return false; // no hay suficiente stock
                }

                // Insertar registro de salida con costo unitario ajustado
                stmtInsert.setInt(1, salida.getProductoId());
                stmtInsert.setString(2, salida.getDepartamento());
                stmtInsert.setInt(3, salida.getCantidad());
                stmtInsert.setDate(4, new java.sql.Date(salida.getFechaSalida().getTime()));
                stmtInsert.setString(5, salida.getMotivo());
                stmtInsert.setInt(6, salida.getUsuarioId());
                stmtInsert.setString(7, salida.getNumeroFactura());

                stmtInsert.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    public static String generarNumeroFactura() {
        System.out.println("Generando número de factura...");
        String sql = "SELECT COUNT(*) FROM salidas";
        int cantidad = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar número de factura: " + e.getMessage());
        }

        System.out.println("Cantidad de salidas actuales: " + cantidad);
        String numeroFactura = String.format("SAL%05d", cantidad + 1);
        System.out.println("Número de factura generado: " + numeroFactura);
        return numeroFactura;
    }

 // Método para obtener todas las salidas de un producto específico
    public static List<Salidas> obtenerPorProducto(int productoId) {
        List<Salidas> lista = new ArrayList<>();
        String sql = "SELECT * FROM salidas WHERE producto_id = ? ORDER BY fecha_salida DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salidas s = new Salidas();
                s.setId(rs.getInt("id"));
                s.setProductoId(rs.getInt("producto_id"));
                s.setDepartamento(rs.getString("departamento"));
                s.setCantidad(rs.getInt("cantidad"));
                s.setFechaSalida(rs.getDate("fecha_salida"));
                s.setMotivo(rs.getString("motivo"));
                s.setUsuarioId(rs.getInt("usuario_id"));
                s.setNumeroFactura(rs.getString("numeroFactura"));
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}