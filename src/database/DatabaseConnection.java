package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/almacen_db";
    private static final String USER = "root";  // Cambia según tu configuración
    private static final String PASSWORD = "";  // Deja vacío si no tienes contraseña

    public static Connection getConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Conexión exitosa a la base de datos");
        } else {
            System.out.println("Error en la conexión");
        }
    }
}