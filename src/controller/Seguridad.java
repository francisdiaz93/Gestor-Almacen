package controller;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {

    // Encripta una contraseña en texto plano
    public static String encriptarPassword(String passwordPlano) {
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
    }

    // Verifica una contraseña ingresada contra su hash almacenado
    public static boolean verificarPassword(String passwordPlano, String hashGuardado) {
        return BCrypt.checkpw(passwordPlano, hashGuardado);
    }
}