package Conversiones;

import DTOs.ClienteDTO;
import Entidades.Cliente;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class ClientesConversiones {

    /**
     * Convierte un objeto Cliente a un objeto ClienteDTO.
     *
     * @param cliente El objeto Cliente que se desea convertir.
     * @return Un objeto ClienteDTO que representa al cliente, o null si el
     * cliente es null.
     */
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

    /**
     * Convierte un objeto ClienteDTO a un objeto Cliente.
     *
     * @param clienteDTO El objeto ClienteDTO que se desea convertir.
     * @return Un objeto Cliente que representa al cliente, o null si el
     * clienteDTO es null.
     */
    public Cliente convertirDTOAEntidad(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }

        Cliente cliente = new Cliente();

        if (clienteDTO.getId() != null) {
            cliente.setId(clienteDTO.getId());
        }

        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellidoPaterno(clienteDTO.getApellidoPaterno());
        cliente.setApellidoMaterno(clienteDTO.getApellidoMaterno());
        cliente.setUsuario(clienteDTO.getUsuario());
        cliente.setContrasenia(clienteDTO.getContrasenia());
        return cliente;
    }
}
