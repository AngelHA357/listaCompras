package Conexion;

import Exceptions.PersistenciaException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 * 
 * Singleton para manejar la conexión con JPA
 */
public class Conexion implements IConexion {

    private static EntityManagerFactory entityManagerFactory;
    private static Conexion instance;
    private EntityManager entityManager; // Atributo para almacenar el EntityManager actual

    private Conexion() {
    }

    private static EntityManagerFactory getEntityManagerFactory() throws PersistenciaException {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("conexionPU");
            } catch (Exception e) {
                throw new PersistenciaException("Error al crear EntityManagerFactory: " + e.getMessage(), e);
            }
        }
        return entityManagerFactory;
    }

    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }

    @Override
    public EntityManager crearConexion() throws PersistenciaException {
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = getEntityManagerFactory().createEntityManager();
        }
        return entityManager;
    }

    
    public static void cerrarConexion() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @Override
    public void rollback() throws PersistenciaException {
        if (entityManager != null) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception e) {
                throw new PersistenciaException("Error al realizar rollback: " + e.getMessage(), e);
            }
        } else {
            throw new PersistenciaException("No hay una conexión activa para realizar rollback.");
        }
    }
}
