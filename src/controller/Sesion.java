package controller;

public class Sesion {
    private static int usuarioId;
    private static String usuario;
    private static String rol;

    public static void iniciarSesion(int id, String nombre, String rolUsuario) {
        usuarioId = id;
        usuario = nombre;
        rol = rolUsuario;
    }

    public static int getUsuarioId() {
        return usuarioId;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static String getRolUsuario() {
        return rol;
    }

    public static void cerrarSesion() {
        usuarioId = 0;
        usuario = null;
        rol = null;
    }
}

