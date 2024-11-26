package DAOs;

import Conexion.IConexion;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
/**
 * 
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 */
public class CompraDAO implements ICompraDAO {

    private final IConexion conexion;

    public CompraDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Método para agregar una nueva compra.
     *
     * @param compra Compra a agregar.
     * @return Compra agregada.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public Compra agregarCompra(Compra compra) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(compra);
            em.getTransaction().commit();
            return em.find(Compra.class, compra.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar compra", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método para obtener una compra por su ID.
     *
     * @param id ID de la compra a buscar.
     * @return Compra encontrada.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public Compra obtenerCompraPorId(Long id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            return em.find(Compra.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener compra por ID", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método para obtener todas las compras.
     *
     * @return Lista de todas las compras.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public List<Compra> obtenerTodasLasCompras() throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            Query query = em.createQuery("SELECT c FROM Compra c");
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener todas las compras", e);
        } finally {
            em.close();
        }
    }


    /**
     * Método para eliminar una compra por su ID.
     *
     * @param id ID de la compra a eliminar.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public void eliminarCompra(Long id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            Compra compra = em.find(Compra.class, id);
            if (compra != null) {
                em.remove(compra);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar compra", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método para obtener todas las compras de un cliente.
     *
     * @param clienteId ID del cliente.
     * @return Lista de compras del cliente.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public List<Compra> obtenerComprasPorCliente(Long clienteId) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            Query query = em.createQuery("SELECT c FROM Compra c WHERE c.cliente.id = :clienteId");
            query.setParameter("clienteId", clienteId);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener compras por cliente", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método para obtener una compra por nombre y cliente.
     *
     * @param nombre Nombre de la compra.
     * @param clienteId ID del cliente.
     * @return Compra correspondiente al nombre y cliente especificados.
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Override
    public Compra obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            Query query = em.createQuery("SELECT c FROM Compra c WHERE c.nombre = :nombre AND c.cliente.id = :clienteId");
            query.setParameter("nombre", nombre);
            query.setParameter("clienteId", clienteId);

            return (Compra) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener compra por nombre y cliente", e);
        } finally {
            em.close();
        }
    }

}
