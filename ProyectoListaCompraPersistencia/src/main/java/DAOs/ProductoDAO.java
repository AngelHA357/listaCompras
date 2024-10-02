package DAOs;

import Conexion.IConexion;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Implementación del DAO para la entidad Producto.
 */
public class ProductoDAO implements IProductoDAO {

    private final IConexion conexion;

    public ProductoDAO(IConexion conexion) {
        this.conexion = conexion;
    }

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
    
    public List<Producto> filtrarPorCategoria(String categoria) throws PersistenciaException{
        EntityManager em = null;
        try {
            em = conexion.crearConexion();
            Query query = em.createQuery("SELECT p FROM Producto p WHERE p.categoria = :categoria");
            query.setParameter("categoria", categoria);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener todos los productos: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        
    }
    
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

}
