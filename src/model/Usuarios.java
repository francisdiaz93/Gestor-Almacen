package model;

public class Usuarios {

    // Atributos
    private int id;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String rol;

    // Constructor vacío
    public Usuarios() {
    }

    // Constructor con todos los parámetros
    public Usuarios(int id, String nombre, String usuario, String contraseña, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    // Métodos getter y setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContrasena(String contrasena) {
        this.contraseña = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método toString para representar el objeto Usuario como una cadena
    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contraseña + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}

