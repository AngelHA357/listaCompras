package DAOs;

import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la entidad Producto.
 */
public interface IProductoDAO {
    
    // Método para crear un producto
    public Producto agregarProducto(Producto producto) throws PersistenciaException;
    
    // Método para obtener un producto por su ID
    public Producto obtenerProductoPorId(Long id) throws PersistenciaException;
    
    // Método para obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() throws PersistenciaException;
    
    // Método para actualizar un producto existente
    public Producto actualizarProducto(Producto producto) throws PersistenciaException;
    
    // Método para eliminar un producto por su ID
    public Producto eliminarProducto(Long id) throws PersistenciaException;
    
    //Método para obtener los productos de una categoría determinada
    public List<Producto> filtrarPorCategoria(String categoria) throws PersistenciaException;
    
    //Método para obtener los productos de una compra según su id
    public List<Producto> obtenerProductosPorCompraId(Long compraId) throws PersistenciaException;
}
