package DAOs;

import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;
/**
 * 
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 */
public interface ICompraDAO {

    /**
     * Método para agregar una nueva compra.
     *
     * @param compra Compra a agregar.
     * @return Compra agregada.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Compra agregarCompra(Compra compra) throws PersistenciaException;

    /**
     * Método para obtener una compra por su ID.
     *
     * @param id ID de la compra a buscar.
     * @return Compra encontrada.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Compra obtenerCompraPorId(Long id) throws PersistenciaException;

    /**
     * Método para obtener todas las compras.
     *
     * @return Lista de todas las compras.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public List<Compra> obtenerTodasLasCompras() throws PersistenciaException;

    /**
     * Método para eliminar una compra por su ID.
     *
     * @param id ID de la compra a eliminar.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public void eliminarCompra(Long id) throws PersistenciaException;

    /**
     * Método para obtener todas las compras de un cliente.
     *
     * @param clienteId ID del cliente.
     * @return Lista de compras del cliente.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public List<Compra> obtenerComprasPorCliente(Long clienteId) throws PersistenciaException;

    /**
     * Método para obtener una compra por nombre y cliente.
     *
     * @param nombre Nombre de la compra.
     * @param clienteId ID del cliente.
     * @return Compra correspondiente al nombre y cliente especificados.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    public Compra obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws PersistenciaException;
}
