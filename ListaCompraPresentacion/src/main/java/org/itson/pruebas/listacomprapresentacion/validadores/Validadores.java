package org.itson.pruebas.listacomprapresentacion.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que permite generar expresiones regulares para validar campos de texto.
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
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

    public boolean validarCantidad(String cantidad) {
        Pattern patron = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = patron.matcher(cantidad);

        return matcher.matches();
    }

    public boolean validarRangoCantidad(double cantidad) {
        return cantidad > 0 && cantidad <= 9999.99;
    }

    public boolean validarDecimalesCantidad(String cantidad) {
        Pattern patron = Pattern.compile("^\\d+(\\.\\d{0,2})?$");
        return patron.matcher(cantidad).matches();
    }

    public boolean validarLongitudNombreProducto(String nombre) {
        return nombre != null && nombre.length() >= 1 && nombre.length() <= 50;
    }
    
    public boolean validarLongitudCategoriaProducto(String nombre) {
        return nombre != null && nombre.length() <= 50;
    }
    
    public boolean validarCategoria(String categoria) {
    Pattern patron = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    return categoria != null && !categoria.trim().isEmpty() && 
           patron.matcher(categoria).matches();
}
}
