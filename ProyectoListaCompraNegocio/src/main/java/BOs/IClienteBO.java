package BOs;

import DTOs.ClienteDTO;
import Exceptions.PersistenciaException;

public interface IClienteBO {

    // Método para agregar un cliente
    void agregarCliente(ClienteDTO clienteDTO);

    // Método para encontrar un cliente por usuario y contraseña
    ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena);
}
