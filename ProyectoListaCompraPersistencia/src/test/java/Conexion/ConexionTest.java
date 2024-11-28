package Conexion;

import Exceptions.PersistenciaException;
import java.lang.reflect.Field;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author victo
 */
public class ConexionTest {

    public ConexionTest() {
    }

    private static Field entityManagerFactoryField;
    private static Field instanceField;
    private static Field entityManagerField;

    @Mock
    private EntityManagerFactory mockEntityManagerFactory;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    /**
     * Configura referencias a campos privados de la clase Conexion antes de
     * todas las pruebas.
     *
     * @throws NoSuchFieldException Si algún campo especificado no existe en la
     * clase.
     */
    @BeforeAll
    public static void setUpClass() throws NoSuchFieldException {
        entityManagerFactoryField = Conexion.class.getDeclaredField("entityManagerFactory");
        entityManagerFactoryField.setAccessible(true);

        instanceField = Conexion.class.getDeclaredField("instance");
        instanceField.setAccessible(true);

        entityManagerField = Conexion.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
    }

    /**
     * Aqui se prepara lo que se ejecutará antes de cada prueba para evitar
     * errores con singleton y además se mockean acciones del entity manager.
     *
     * @throws IllegalAccessException Si no se puede acceder o modificar los
     * campos privados.
     */
    @BeforeEach
    public void setUp() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        resetSingleton();

        when(mockEntityManagerFactory.createEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        when(mockEntityManager.isOpen()).thenReturn(true);
        when(mockEntityManagerFactory.isOpen()).thenReturn(true);
    }

    /**
     * Restablece el estado del Singleton.
     *
     * @throws IllegalAccessException Si no se puede acceder o modificar los
     * campos privados.
     */
    private void resetSingleton() throws IllegalAccessException {
        entityManagerFactoryField.set(null, null);
        instanceField.set(null, null);
    }

    @Test
    public void testGetInstance() {
        Conexion instance1 = Conexion.getInstance();
        Conexion instance2 = Conexion.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "Debería retornar la misma instancia");
    }

    @Test
    public void testCrearConexion() throws Exception {
        Conexion conexion = Conexion.getInstance();
        entityManagerFactoryField.set(null, mockEntityManagerFactory);

        EntityManager result = conexion.crearConexion();

        assertNotNull(result);
        verify(mockEntityManagerFactory).createEntityManager();
    }

    @Test
    public void testCrearConexion_ReutilizaEntityManagerAbierto() throws Exception {
        Conexion conexion = Conexion.getInstance();
        entityManagerFactoryField.set(null, mockEntityManagerFactory);

        EntityManager result1 = conexion.crearConexion();
        EntityManager result2 = conexion.crearConexion();

        assertSame(result1, result2, "Debería reutilizar el mismo EntityManager");
        verify(mockEntityManagerFactory, times(1)).createEntityManager();
    }

    @Test
    public void testCrearConexion_EntityManagerCerrado() throws Exception {
        Conexion conexion = Conexion.getInstance();
        entityManagerFactoryField.set(null, mockEntityManagerFactory);

        when(mockEntityManager.isOpen()).thenReturn(false);

        conexion.crearConexion();
        conexion.crearConexion();

        verify(mockEntityManagerFactory, times(2)).createEntityManager();
    }

    @Test
    public void testRollback_ConEntityManagerActivo() throws Exception {
        Conexion conexion = Conexion.getInstance();
        entityManagerFactoryField.set(null, mockEntityManagerFactory);
        entityManagerField.set(conexion, mockEntityManager);

        when(mockTransaction.isActive()).thenReturn(true);

        conexion.rollback();

        verify(mockTransaction).rollback();
    }

    @Test
    public void testRollback_SinTransaccionActiva() throws Exception {
        Conexion conexion = Conexion.getInstance();
        entityManagerFactoryField.set(null, mockEntityManagerFactory);
        entityManagerField.set(conexion, mockEntityManager);

        when(mockTransaction.isActive()).thenReturn(false);

        conexion.rollback();

        verify(mockTransaction, never()).rollback();
    }

    @Test
    public void testRollback_SinEntityManager() {
        Conexion conexion = Conexion.getInstance();

        PersistenciaException exception = assertThrows(PersistenciaException.class, () -> {
            conexion.rollback();
        });

        assertEquals("No hay una conexión activa para realizar rollback.", exception.getMessage());
    }

    @Test
    public void testCerrarConexion() throws Exception {
        entityManagerFactoryField.set(null, mockEntityManagerFactory);

        Conexion.cerrarConexion();

        verify(mockEntityManagerFactory).close();
    }

    @Test
    public void testCerrarConexion_FactoryCerrada() throws Exception {
        entityManagerFactoryField.set(null, mockEntityManagerFactory);
        when(mockEntityManagerFactory.isOpen()).thenReturn(false);

        Conexion.cerrarConexion();

        verify(mockEntityManagerFactory, never()).close();
    }

    /**
     * Limpia el estado después de cada prueba.
     *
     * @throws IllegalAccessException Si no se puede acceder o modificar los
     * campos privados.
     */
    @AfterEach
    public void tearDown() throws IllegalAccessException {
        resetSingleton();
    }
}
