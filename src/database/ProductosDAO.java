package database;

import model.Productos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3307/almacen_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection connection;

    static {
        connection = DatabaseConnection.getConnection();
    }

    public ProductosDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // Crear producto
    public static boolean insertarProducto(Productos producto) {
        String sql = "INSERT INTO productos (nombre, codigo, categoria, descripcion, cantidad, proveedor, fecha_ingreso, marca, stock_minimo, imagen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre().get());
            stmt.setString(2, producto.getCodigo().get());
            stmt.setString(3, producto.getCategoria().get());
            stmt.setString(4, producto.getDescripcion().get());
            stmt.setInt(5, producto.getStock().get());
            stmt.setString(6, producto.getProveedor().get());
            stmt.setDate(7, Date.valueOf(producto.getFechaIngreso().get()));
            stmt.setString(8, producto.getMarca().get());
            stmt.setInt(9, producto.getStockMinimo().get());
            stmt.setString(10, producto.getImagen());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // Leer todos los productos
    public static List<Productos> obtenerProductos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCodigo(rs.getString("codigo"));
                p.setCategoria(rs.getString("categoria"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getInt("cantidad"));
                p.setProveedor(rs.getString("proveedor"));
                p.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                p.setMarca(rs.getString("marca"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setImagen(rs.getString("imagen"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }

        return lista;
    }

    // Actualizar producto
    public static boolean actualizarProducto(Productos producto) {
        String sql = "UPDATE productos SET nombre=?, codigo=?, categoria=?, descripcion=?, cantidad=?, proveedor=?, fecha_ingreso=?, marca=?, stock_minimo=?, imagen=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre().get());
            stmt.setString(2, producto.getCodigo().get());
            stmt.setString(3, producto.getCategoria().get());
            stmt.setString(4, producto.getDescripcion().get());
            stmt.setInt(5, producto.getStock().get());
            stmt.setString(6, producto.getProveedor().get());
            stmt.setDate(7, Date.valueOf(producto.getFechaIngreso().get()));
            stmt.setString(8, producto.getMarca().get());
            stmt.setInt(9, producto.getStockMinimo().get());
            stmt.setString(10, producto.getImagen());
            stmt.setInt(11, producto.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // Eliminar producto
    public static boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // Buscar producto por código
    public static Productos buscarProductoPorCodigo(String codigo) {
        Productos producto = null;
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                producto = new Productos();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setStock(rs.getInt("cantidad"));
                producto.setProveedor(rs.getString("proveedor"));
                Date fechaSql = rs.getDate("fecha_ingreso");
                if (fechaSql != null) {
                    producto.setFechaIngreso(fechaSql.toLocalDate());
                }
                producto.setMarca(rs.getString("marca"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }
        return producto;
    }

    // Obtener categorías distintas
    public static List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM productos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
        }
        return categorias;
    }

    // Actualizar cantidad (sumar al stock actual)
    public static boolean actualizarCantidadProducto(int productoId, int cantidadAgregar) {
        String sql = "UPDATE productos SET cantidad = cantidad + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cantidadAgregar);
            stmt.setInt(2, productoId);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }

    // Generar código automático tipo P001, P002, ...
    public static String generarCodigoProducto() {
        String nuevoCodigo = "P001";
        String sql = "SELECT codigo FROM productos ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String ultimoCodigo = rs.getString("codigo");
                int numero = Integer.parseInt(ultimoCodigo.substring(1));
                nuevoCodigo = String.format("P%03d", numero + 1);
            }
        } catch (Exception e) {
            System.err.println("Error al generar código de producto: " + e.getMessage());
        }
        return nuevoCodigo;
    }

    // Obtener ID a partir del código
    public static int obtenerIdPorCodigo(String codigo) {
        int id = -1;
        String sql = "SELECT id FROM productos WHERE codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID por código: " + e.getMessage());
        }
        return id;
    }

    // Restar stock
    public static boolean restarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ? AND cantidad >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, productoId);
            stmt.setInt(3, cantidad);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al restar stock: " + e.getMessage());
            return false;
        }
    }

    // Listar productos en stock
    public static List<Productos> listarProductosEnStock() {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE cantidad > 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("cantidad"));
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos en stock: " + e.getMessage());
        }
        return productos;
    }
    
    public static int obtenerStockActual(int productoId) {
        String sql = "SELECT cantidad FROM productos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int obtenerStockMinimo(int productoId) {
        int stockMinimo = 0;
        String sql = "SELECT stock_minimo FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                stockMinimo = rs.getInt("stock_minimo");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener stock mínimo: " + e.getMessage());
        }
        return stockMinimo;
    }
    
    public static String obtenerNombreProducto(int productoId) {
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
    
    public static List<Productos> obtenerProductosConStock() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE cantidad > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setStock(rs.getInt("cantidad"));
                p.setProveedor(rs.getString("proveedor"));
                p.setMarca(rs.getString("marca"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
}
