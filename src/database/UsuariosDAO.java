package database;

import java.sql.*;
import model.Usuarios;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UsuariosDAO {

    // Método para obtener todos los usuarios
    public List<Usuarios> obtenerUsuarios() {
        List<Usuarios> listaUsuarios = new ArrayList<>();
        String query = "SELECT * FROM usuarios";

        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuarios usuario = new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                );
                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    // Método para obtener un usuario por su id
    public Usuarios obtenerUsuarioPorId(int id) {
        Usuarios usuario = null;
        String query = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // Método para agregar un nuevo usuario
    public boolean agregarUsuario(Usuarios usuario) {
        String query = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Hashear la contraseña antes de guardarla
            String contrasenaHasheada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, contrasenaHasheada);
            stmt.setString(4, usuario.getRol());

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar un usuario
    public boolean actualizarUsuario(Usuarios usuario) {
        String query = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena = ?, rol = ? WHERE id = ?";

        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Hashear la contraseña antes de guardarla
            String contrasenaHasheada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, contrasenaHasheada);
            stmt.setString(4, usuario.getRol());
            stmt.setInt(5, usuario.getId());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un usuario
    public boolean eliminarUsuario(int id) {
        String query = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


public boolean existeUsuario(String correo, String contrasenaIngresada) {
    String query = "SELECT contrasena FROM usuarios WHERE correo = ?";
    try (Connection conn = database.DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, correo);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String contrasenaHasheada = rs.getString("contrasena");
            // Compara la contraseña ingresada con la hasheada en BD
            return BCrypt.checkpw(contrasenaIngresada, contrasenaHasheada);
        } else {
            // No existe usuario con ese correo
            return false;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    /*public void migrarContrasenas() {
        String selectQuery = "SELECT id, contraseña FROM usuarios";
        String updateQuery = "UPDATE usuarios SET contraseña = ? WHERE id = ?";

        try (Connection conn = database.DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String contrasenaTextoPlano = rs.getString("contraseña");

                // Verificamos si ya está encriptada (usualmente comienza con $2a$ o $2b$)
                if (contrasenaTextoPlano.startsWith("$2a$") || contrasenaTextoPlano.startsWith("$2b$")) {
                    continue; // ya está encriptada
                }

                // Encriptar la contraseña
                String contrasenaEncriptada = controller.Seguridad.encriptarPassword(contrasenaTextoPlano);

                // Actualizar en la base de datos
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, contrasenaEncriptada);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                }
            }

            System.out.println("Migración completada exitosamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
