package DAOs;

import Conexion.IConexion;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CompraDAO implements ICompraDAO {

    private final IConexion conexion;

    public CompraDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregarCompra(Compra compra) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(compra);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar compra", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Compra obtenerCompraPorId(String id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            return em.find(Compra.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener compra por ID", e);
        } finally {
            em.close();
        }
    }

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

    @Override
    public void actualizarCompra(Compra compra) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(compra);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar compra", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarCompra(String id) throws PersistenciaException {
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
}
