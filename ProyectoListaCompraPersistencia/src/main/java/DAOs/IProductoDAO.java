package DAOs;

import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la entidad Producto.
 */
public interface IProductoDAO {

    /**
     * Método para crear un producto
     *
     * @param producto
     * @return Producto agregado.
     * @throws PersistenciaException
     */
    public Producto agregarProducto(Producto producto) throws PersistenciaException;

    /**
     * Método para obtener un producto por su ID
     *
     * @param id
     * @return Producto concreto.
     * @throws PersistenciaException
     */
    public Producto obtenerProductoPorId(Long id) throws PersistenciaException;

    /**
     * Método para obtener todos los productos
     *
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    public List<Producto> obtenerTodosLosProductos() throws PersistenciaException;

    /**
     * Método para actualizar un producto existente
     *
     * @param producto
     * @return Producto actualizado.
     * @throws PersistenciaException
     */
    public Producto actualizarProducto(Producto producto) throws PersistenciaException;

    /**
     * Método para eliminar un producto por su ID
     *
     * @param id
     * @return Producto que se elimino.
     * @throws PersistenciaException
     */
    public Producto eliminarProducto(Long id) throws PersistenciaException;

    /**
     * Método para obtener los productos de una categoría determinada
     *
     * @param categoria
     * @param compraId
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    public List<Producto> filtrarPorCategoriaYCompraId(String categoria, Long compraId) throws PersistenciaException;

    /**
     * Método para obtener los productos de una compra según su id
     *
     * @param compraId
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    public List<Producto> obtenerProductosPorCompraId(Long compraId) throws PersistenciaException;

    /**
     * Método para obtener un producto por todas sus características.
     *
     * @param nombre
     * @param categoria
     * @param comprado
     * @param cantidad
     * @param compraId
     * @return Producto concreto.
     * @throws PersistenciaException
     */
    public Producto obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) throws PersistenciaException;
}
