package Conexion;

import Exceptions.PersistenciaException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton para manejar la conexión con JPA
 */
public class Conexion implements IConexion {

    // Atributo estático para la instancia Singleton de EntityManagerFactory
    private static EntityManagerFactory entityManagerFactory;
    private static Conexion instance;

    // Constructor privado para evitar instanciación desde fuera
    private Conexion() {
    }

    // Método para obtener una única instancia de EntityManagerFactory
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
        // Usamos la única instancia de EntityManagerFactory para crear el EntityManager
        return getEntityManagerFactory().createEntityManager();
    }

    // Método para cerrar la EntityManagerFactory cuando ya no se necesite
    public static void cerrarConexion() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
