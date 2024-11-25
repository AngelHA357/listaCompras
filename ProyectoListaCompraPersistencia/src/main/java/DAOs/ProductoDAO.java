package DAOs;

import Conexion.IConexion;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * 
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 */
public class ProductoDAO implements IProductoDAO {

    private final IConexion conexion;

    public ProductoDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Método para crear un producto
     *
     * @param producto
     * @return Producto agregado.
     * @throws PersistenciaException
     */
    @Override
    public Producto agregarProducto(Producto producto) throws PersistenciaException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = conexion.crearConexion();
            tx = em.getTransaction();
            tx.begin();
            em.persist(producto); // Inserta el producto en la base de datos
            tx.commit();
            return em.find(Producto.class, producto.getId());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            throw new PersistenciaException("Error al agregar el producto: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método para obtener un producto por su ID
     *
     * @param id
     * @return Producto concreto.
     * @throws PersistenciaException
     */
    @Override
    public Producto obtenerProductoPorId(Long id) throws PersistenciaException {
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            return em.find(Producto.class, id); // Busca el producto por su ID
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener el producto por ID: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método para obtener todos los productos
     *
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    @Override
    public List<Producto> obtenerTodosLosProductos() throws PersistenciaException {
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            Query query = em.createQuery("SELECT p FROM Producto p"); // Consulta para obtener todos los productos
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener todos los productos: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método para actualizar un producto existente
     *
     * @param producto
     * @return Producto actualizado.
     * @throws PersistenciaException
     */
    @Override
    public Producto actualizarProducto(Producto producto) throws PersistenciaException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = conexion.crearConexion();
            tx = em.getTransaction();
            tx.begin();
            em.merge(producto); // Actualiza el producto
            tx.commit();
            return em.find(Producto.class, producto.getId());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            throw new PersistenciaException("Error al actualizar el producto: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método para eliminar un producto por su ID
     *
     * @param id
     * @return Producto que se elimino.
     * @throws PersistenciaException
     */
    @Override
    public Producto eliminarProducto(Long id) throws PersistenciaException {
        EntityManager em = null;
        EntityTransaction tx = null;
        Producto productoEliminado = null;
        try {
            em = conexion.crearConexion();
            tx = em.getTransaction();
            tx.begin();
            productoEliminado = em.find(Producto.class, id);
            if (productoEliminado != null) {
                em.remove(productoEliminado); // Elimina el producto si existe
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            throw new PersistenciaException("Error al eliminar el producto: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return productoEliminado;
    }

    /**
     * Método para obtener los productos de una categoría determinada
     *
     * @param categoria
     * @param compraId
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    @Override
    public List<Producto> filtrarPorCategoriaYCompraId(String categoria, Long compraId) throws PersistenciaException {
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            Query query = em.createQuery("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.compra.id = :compraId");
            query.setParameter("categoria", categoria);
            query.setParameter("compraId", compraId);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al filtrar productos por categoría y compra: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método para obtener los productos de una compra según su id
     *
     * @param compraId
     * @return Lista de Productos.
     * @throws PersistenciaException
     */
    @Override
    public List<Producto> obtenerProductosPorCompraId(Long compraId) throws PersistenciaException {
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            Query query = em.createQuery("SELECT p FROM Producto p WHERE p.compra.id = :compraId");
            query.setParameter("compraId", compraId);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener los productos por ID de compra: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

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
    @Override
    public Producto obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) throws PersistenciaException {
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            Query query = em.createQuery("SELECT p FROM Producto p WHERE p.nombre = :nombre AND p.categoria = :categoria "
                    + "AND p.comprado = :comprado AND p.cantidad = :cantidad AND p.compra.id = :compraId");
            query.setParameter("nombre", nombre);
            query.setParameter("categoria", categoria);
            query.setParameter("comprado", comprado);
            query.setParameter("cantidad", cantidad);
            query.setParameter("compraId", compraId);
            List<Producto> resultados = query.getResultList();

            // Retornamos el primer producto encontrado o null si no hay coincidencias
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener el producto por características: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
