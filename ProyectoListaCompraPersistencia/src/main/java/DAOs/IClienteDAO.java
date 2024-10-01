package DAOs;

import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IClienteDAO {

    void agregarCliente(Cliente cliente) throws PersistenciaException;

    Cliente obtenerClientePorId(String id) throws PersistenciaException;

    List<Cliente> obtenerTodosLosClientes() throws PersistenciaException;

    void actualizarCliente(Cliente cliente) throws PersistenciaException;

    void eliminarCliente(String id) throws PersistenciaException;
}
