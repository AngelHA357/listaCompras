package BOs;

import DTOs.ClienteDTO;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;

public interface IClienteBO {

    // Método para agregar un cliente
    public ClienteDTO agregarCliente(ClienteDTO clienteDTO);

    // Método para encontrar un cliente por usuario y contraseña
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena) throws NegocioException;
}
