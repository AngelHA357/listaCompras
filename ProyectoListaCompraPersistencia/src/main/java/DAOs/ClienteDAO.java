package DAOs;

import Conexion.IConexion;
import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ClienteDAO implements IClienteDAO {

    private final IConexion conexion;

    public ClienteDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregarCliente(Cliente cliente) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al agregar cliente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente obtenerClientePorId(String id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            return em.find(Cliente.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener cliente por ID", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            Query query = em.createQuery("SELECT c FROM Cliente c");
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener todos los clientes", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarCliente(Cliente cliente) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar cliente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarCliente(String id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar cliente", e);
        } finally {
            em.close();
        }
    }
}
