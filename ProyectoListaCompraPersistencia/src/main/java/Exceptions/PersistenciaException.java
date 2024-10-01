/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author pc
 */
public class PersistenciaException extends Exception{

    /**
     * Constructor sin parámetros.
     */
    public PersistenciaException() {
    }

    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param message Mensaje de error.
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje de error y una causa subyacente.
     *
     * @param message Mensaje de error.
     * @param cause Causa subyacente.
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor que recibe una causa subyacente.
     *
     * @param cause Causa subyacente.
     */
    public PersistenciaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor que recibe un mensaje de error, una causa subyacente, un
     * indicador de supresión de habilitación y un indicador de escribibilidad
     * de rastreo.
     *
     * @param message Mensaje de error.
     * @param cause Causa subyacente.
     * @param enableSuppression Indicador de supresión de habilitación.
     * @param writableStackTrace Indicador de escribibilidad de rastreo.
     */
    public PersistenciaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
