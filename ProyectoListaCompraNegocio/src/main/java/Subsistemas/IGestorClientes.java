/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.ClienteDTO;
import Exceptions.NegocioException;

/**
 *
 * @author IJCF
 */
public interface IGestorClientes {

    // Método para agregar un cliente
    public ClienteDTO agregarCliente(ClienteDTO clienteDTO);

    // Método para encontrar un cliente por usuario y contraseña
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena) throws NegocioException;
}
