package DAOs;

import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la entidad Producto.
 */
public interface IProductoDAO {
    
    // Método para crear un producto
    Producto agregarProducto(Producto producto) throws PersistenciaException;
    
    // Método para obtener un producto por su ID
    Producto obtenerProductoPorId(Long id) throws PersistenciaException;
    
    // Método para obtener todos los productos
    List<Producto> obtenerTodosLosProductos() throws PersistenciaException;
    
    // Método para actualizar un producto existente
    Producto actualizarProducto(Producto producto) throws PersistenciaException;
    
    // Método para eliminar un producto por su ID
    Producto eliminarProducto(Long id) throws PersistenciaException;
}
