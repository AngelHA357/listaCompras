package org.itson.pruebas.listacomprapresentacion.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author victo
 */
public class Validadores {

    /**
     * Valida si el nombre proporcionado cumple con el formato permitido.
     *
     * @param nombre Nombre a validar.
     * @return true si el nombre es válido, false de lo contrario.
     */
    public boolean validaNombre(String nombre) {
        Pattern patron = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
        Matcher matcher = patron.matcher(nombre);

        return matcher.matches();
    }

    /**
     * Valida si el apellido proporcionado cumple con el formato permitido.
     *
     * @param apellido Apellido a validar.
     * @return true si el apellido es válido, false de lo contrario.
     */
    public boolean validaApellido(String apellido) {
        Pattern patron = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
        Matcher matcher = patron.matcher(apellido);

        return matcher.matches();
    }

    /**
     * Valida si el nombre de usuario proporcionado cumple con el formato
     * permitido.
     *
     * @param usuario Nombre de usuario a validar.
     * @return true si el nombre de usuario es válido, false de lo contrario.
     */
    public boolean validaUsuario(String usuario) {
        Pattern patron = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = patron.matcher(usuario);

        return matcher.matches();
    }

    /**
     * Valida si la contraseña proporcionada cumple con el formato permitido.
     * Debe contener al menos una letra mayúscula, una letra minúscula y un
     * número.
     *
     * @param contrasena Contraseña a validar.
     * @return true si la contraseña es válida, false de lo contrario.
     */
    public boolean validaContrasena(String contrasena) {
        Pattern patron = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).+$");
        Matcher matcher = patron.matcher(contrasena);

        return matcher.matches();
    }

}
