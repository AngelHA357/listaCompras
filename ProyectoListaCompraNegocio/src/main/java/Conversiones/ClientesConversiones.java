package Conversiones;

import DTOs.ClienteDTO;
import Entidades.Cliente;

public class ClientesConversiones {

    public ClienteDTO convertirEntidadADTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellidoPaterno(cliente.getApellidoPaterno());
        clienteDTO.setApellidoMaterno(cliente.getApellidoMaterno());
        clienteDTO.setUsuario(cliente.getUsuario());
        clienteDTO.setContrasenia(cliente.getContrasenia());
        return clienteDTO;
    }

    public Cliente convertirDTOAEntidad(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellidoPaterno(clienteDTO.getApellidoPaterno());
        cliente.setApellidoMaterno(clienteDTO.getApellidoMaterno());
        cliente.setUsuario(clienteDTO.getUsuario());
        cliente.setContrasenia(clienteDTO.getContrasenia());
        return cliente;
    }
}
