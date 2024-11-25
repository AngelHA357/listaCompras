/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 */
public class NegocioException extends Exception {

    /**
     * Construye una instancia de NegocioException sin un mensaje específico.
     */
    public NegocioException() {
    }

    /**
     * Construye una instancia de NegocioException con un mensaje específico que
     * describe la causa de la excepción.
     *
     * @param message El mensaje que describe la causa de la excepción.
     */
    public NegocioException(String message) {
        super(message);
    }
}
