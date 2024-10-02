package Conversiones;

import DTOs.ClienteDTO;
import Entidades.Cliente;

public class ClientesConversiones {

    // Conversión de Cliente (entidad) a ClienteDTO
    public ClienteDTO convertirEntidadADTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellidoPaterno(cliente.getApellidoPaterno());
        clienteDTO.setApellidoMaterno(cliente.getApellidoMaterno());
        clienteDTO.setUsuario(cliente.getUsuario());
        return clienteDTO;
    }

    // Conversión de ClienteDTO a Cliente (entidad para persistencia)
    public Cliente convertirDTOAEntidad(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellidoPaterno(clienteDTO.getApellidoPaterno());
        cliente.setApellidoMaterno(clienteDTO.getApellidoMaterno());
        cliente.setUsuario(clienteDTO.getUsuario());
        // La contraseña no se incluye en el DTO por motivos de seguridad,
        // pero debe manejarse por separado en el sistema.
        return cliente;
    }
}
