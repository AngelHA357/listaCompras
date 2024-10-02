/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import Conversiones.ClientesConversiones;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.PersistenciaException;


public class ClienteBO {

    private final IClienteDAO clienteDAO;
    private final ClientesConversiones conversiones;

    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
        this.conversiones = new ClientesConversiones();
    }

   
    public void agregarCliente(ClienteDTO clienteDTO) throws PersistenciaException {
        Cliente cliente = conversiones.convertirDTOAEntidad(clienteDTO);
        clienteDAO.agregarCliente(cliente);
    }

    
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasenia) throws PersistenciaException {
        Cliente cliente = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        return conversiones.convertirEntidadADTO(cliente);
    }
}
