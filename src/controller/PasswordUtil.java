package controller;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Genera un hash BCrypt de la contraseña
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verifica si la contraseña ingresada coincide con el hash
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}