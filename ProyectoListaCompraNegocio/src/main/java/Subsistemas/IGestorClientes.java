/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.ClienteDTO;
import Exceptions.NegocioException;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public interface IGestorClientes {

    /**
     * Método para agregar un nuevo cliente.
     *
     * @param clienteDTO Datos del cliente a agregar.
     * @return ClienteDTO con los detalles del cliente agregado.
     * @throws NegocioException Si ocurre algún error en la lógica de negocio.
     */
    public ClienteDTO agregarCliente(ClienteDTO clienteDTO) throws NegocioException;

    /**
     * Método para encontrar un cliente utilizando su usuario y contraseña.
     *
     * @param usuario Nombre de usuario del cliente.
     * @param contrasena Contraseña del cliente.
     * @return ClienteDTO con los detalles del cliente encontrado.
     * @throws NegocioException Si el cliente no existe o si ocurre algún error
     * en la lógica de negocio.
     */
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena) throws NegocioException;
}
