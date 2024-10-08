package DAOs;

import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IClienteDAO {

    /**
     * Método para agregar un nuevo cliente.
     *
     * @param cliente Cliente a agregar.
     * @return Cliente agregado.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Cliente agregarCliente(Cliente cliente) throws PersistenciaException;

    /**
     * Método para obtener un cliente por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return Cliente encontrado.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Cliente obtenerClientePorId(Long id) throws PersistenciaException;

    /**
     * Método para obtener todos los clientes.
     *
     * @return Lista de todos los clientes.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public List<Cliente> obtenerTodosLosClientes() throws PersistenciaException;

    /**
     * Método para actualizar un cliente existente.
     *
     * @param cliente Cliente con los nuevos datos.
     * @return Cliente actualizado.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;

    /**
     * Método para eliminar un cliente por su ID.
     *
     * @param id ID del cliente a eliminar.
     * @return Cliente eliminado.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Cliente eliminarCliente(Long id) throws PersistenciaException;

    /**
     * Método para obtener un cliente por su usuario y contraseña.
     *
     * @param usuario Nombre de usuario del cliente.
     * @param contrasenia Contraseña del cliente.
     * @return Cliente que coincide con las credenciales proporcionadas.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Cliente obtenerClientePorUsuarioYContrasena(String usuario, String contrasenia) throws PersistenciaException;
}
