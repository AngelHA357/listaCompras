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
    public Cliente agregarCliente(Cliente cliente) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();

            return em.find(Cliente.class, cliente.getId());
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
    public Cliente obtenerClientePorId(Long id) throws PersistenciaException {
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
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();

            return em.find(Cliente.class, cliente.getId());
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
    public Cliente eliminarCliente(String id) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        Cliente clienteEliminado = null; // Variable para almacenar el cliente a eliminar
        try {
            em.getTransaction().begin();
            clienteEliminado = em.find(Cliente.class, id);
            if (clienteEliminado != null) {
                em.remove(clienteEliminado);
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
        return clienteEliminado;
    }

    @Override
    public Cliente obtenerClientePorUsuarioYContrasena(String usuario, String contrasenia) throws PersistenciaException {
        EntityManager em = conexion.crearConexion();
        try {
            Query query = em.createQuery("SELECT c FROM Cliente c WHERE c.usuario = :usuario AND c.contrasenia = :contrasenia");
            query.setParameter("usuario", usuario);
            query.setParameter("contrasenia", contrasenia);
            return (Cliente) query.getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener cliente por usuario y contraseña", e);
        } finally {
            em.close();
        }
    }

}
