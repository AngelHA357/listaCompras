package DAOs;

import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IClienteDAO {

    Cliente agregarCliente(Cliente cliente) throws PersistenciaException;

    Cliente obtenerClientePorId(Long id) throws PersistenciaException;

    List<Cliente> obtenerTodosLosClientes() throws PersistenciaException;

    Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;

    Cliente eliminarCliente(String id) throws PersistenciaException;

    Cliente obtenerClientePorUsuarioYContrasena(String usuario, String contrasenia) throws PersistenciaException;
}
