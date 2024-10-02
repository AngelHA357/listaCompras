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
import Exceptions.PersistenciaException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClienteBO implements IClienteBO {

    private IConexion conexion;
    private final IClienteDAO clienteDAO;
    private final ClientesConversiones conversiones;

    public ClienteBO() {
        conexion = Conexion.getInstance();
        this.clienteDAO = new ClienteDAO(conexion);
        this.conversiones = new ClientesConversiones();
    }

   
    @Override
    public void agregarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = conversiones.convertirDTOAEntidad(clienteDTO);
        try {
            clienteDAO.agregarCliente(cliente);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasenia) {
        try {
            Cliente cliente = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
            return conversiones.convertirEntidadADTO(cliente);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
