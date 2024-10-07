/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BOs;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ClientesConversiones;
import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClienteBO implements IClienteBO {

    private IConexion conexion;
    private final IClienteDAO clienteDAO;
    private final ClientesConversiones conversiones;

    public ClienteBO(IClienteDAO clienteDAO, ClientesConversiones conversiones){
        this.clienteDAO = clienteDAO;
        this.conversiones = conversiones;
    }
    
    public ClienteBO() {
        conexion = Conexion.getInstance();
        this.clienteDAO = new ClienteDAO(conexion);
        this.conversiones = new ClientesConversiones();
    }

   
    @Override
    public ClienteDTO agregarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = conversiones.convertirDTOAEntidad(clienteDTO);
        try {
            Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);
            return conversiones.convertirEntidadADTO(clienteAgregado);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    
    @Override
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasenia) throws NegocioException {
        try {
            Cliente cliente = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
            return conversiones.convertirEntidadADTO(cliente);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se encontró al usuario");
        }
    }

    
    
    @Override
    public IClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    @Override
    public ClientesConversiones getConversiones() {
        return conversiones;
    }
    
    
    
    
}
