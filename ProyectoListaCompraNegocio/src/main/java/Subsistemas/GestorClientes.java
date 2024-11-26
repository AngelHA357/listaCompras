/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subsistemas;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ClientesConversiones;
import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorClientes implements IGestorClientes {

    private IConexion conexion;
    private final IClienteDAO clienteDAO;
    private final ClientesConversiones conversiones;

    public GestorClientes() {
        conexion = Conexion.getInstance();
        this.clienteDAO = new ClienteDAO(conexion);
        this.conversiones = new ClientesConversiones();
    }

    public GestorClientes(IClienteDAO clienteDAO, ClientesConversiones conversiones) {
        this.clienteDAO = clienteDAO;
        this.conversiones = conversiones;
    }

    @Override
    public ClienteDTO agregarCliente(ClienteDTO clienteDTO) throws NegocioException {
        try {
            List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();

            if (clienteDTO.getNombre() == null || clienteDTO.getNombre().isBlank()) {
                throw new NegocioException("El nombre no puede ser nulo o estar en blanco");
            }
            if (clienteDTO.getApellidoPaterno() == null || clienteDTO.getApellidoPaterno().isBlank()) {
                throw new NegocioException("El apellido paterno no puede ser nulo o estar en blanco");
            }
            if (clienteDTO.getApellidoMaterno() == null || clienteDTO.getApellidoMaterno().isBlank()) {
                throw new NegocioException("El apellido materno no puede ser nulo o estar en blanco");
            }
            if (clienteDTO.getUsuario() == null || clienteDTO.getUsuario().isBlank()) {
                throw new NegocioException("El usuario no puede ser nulo o estar en blanco");
            }
            if (clienteDTO.getContrasenia() == null || clienteDTO.getContrasenia().isBlank()) {
                throw new NegocioException("La contraseña no puede ser nula o estar en blanco");
            }

            for (Cliente clienteExistente : clientes) {
                if (clienteExistente.getUsuario().equals(clienteDTO.getUsuario())) {
                    throw new NegocioException("El usuario ya está registrado. Por favor, elige un usuario diferente.");
                }
            }

            Cliente cliente = conversiones.convertirDTOAEntidad(clienteDTO);
            try {
                Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);
                return conversiones.convertirEntidadADTO(clienteAgregado);
            } catch (PersistenciaException ex) {
                Logger.getLogger(GestorClientes.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorClientes.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasenia) throws NegocioException {
        if (usuario == null || usuario.isBlank()) {
            throw new NegocioException("El usuario no puede ser nulo o estar en blanco");
        }

        try {
            Cliente cliente = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
            return conversiones.convertirEntidadADTO(cliente);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se encontró al usuario");
        }
    }
}
