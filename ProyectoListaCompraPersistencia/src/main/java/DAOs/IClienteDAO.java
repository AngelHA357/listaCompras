package DAOs;

import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IClienteDAO {

    public Cliente agregarCliente(Cliente cliente) throws PersistenciaException;

    public Cliente obtenerClientePorId(Long id) throws PersistenciaException;

    public List<Cliente> obtenerTodosLosClientes() throws PersistenciaException;

    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;

    public Cliente eliminarCliente(Long id) throws PersistenciaException;

    public Cliente obtenerClientePorUsuarioYContrasena(String usuario, String contrasenia) throws PersistenciaException;
}
