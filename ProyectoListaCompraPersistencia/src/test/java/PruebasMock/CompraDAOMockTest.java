/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

import Conexion.IConexion;
import DAOs.CompraDAO;
import Entidades.Cliente;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author JoseH
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;

public class CompraDAOMockTest {

    @Mock
    private IConexion mockConexion;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    private CompraDAO compraDAO;

    @BeforeEach
    public void setUp() throws PersistenciaException {
        MockitoAnnotations.openMocks(this);
        when(mockConexion.crearConexion()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).begin();
        doNothing().when(mockTransaction).commit();
        when(mockTransaction.isActive()).thenReturn(true);
        compraDAO = new CompraDAO(mockConexion);
    }

    @Test
    public void testAgregarCompra() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        Compra compra = new Compra("Compra de Prueba", cliente);

        doAnswer(invocation -> {
            Compra compraArgumento = invocation.getArgument(0);
            compraArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Compra.class));

        when(mockEntityManager.find(eq(Compra.class), anyLong())).thenReturn(compra);

        Compra resultado = compraDAO.agregarCompra(compra);

        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombre());
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getCliente());

        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(any(Compra.class));
        verify(mockEntityManager).find(eq(Compra.class), anyLong());
        verify(mockTransaction).commit();
    }

    @Test
    public void testAgregarCompra_NombreNulo() {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        Compra compra = new Compra(null, cliente);

        doThrow(new IllegalArgumentException("El nombre no puede ser nulo"))
                .when(mockEntityManager).persist(any(Compra.class));

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.agregarCompra(compra);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testAgregarCompra_NombreVacio() {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        Compra compra = new Compra("", cliente);

        doThrow(new IllegalArgumentException("El nombre no puede estar vacío"))
                .when(mockEntityManager).persist(any(Compra.class));

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.agregarCompra(compra);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testAgregarCompra_ErrorPersistencia() {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        Compra compra = new Compra("Compra Test", cliente);

        doThrow(new PersistenceException("Error al persistir"))
                .when(mockEntityManager).persist(any(Compra.class));

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.agregarCompra(compra);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        Long compraId = 1L;
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        Compra compra = new Compra("Compra de Prueba", cliente);
        compra.setId(compraId);

        when(mockEntityManager.find(Compra.class, compraId)).thenReturn(compra);

        Compra resultado = compraDAO.obtenerCompraPorId(compraId);

        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombre());
        assertEquals(compraId, resultado.getId());

        verify(mockEntityManager).find(Compra.class, compraId);
    }

    @Test
    public void testObtenerCompraPorId_Inexistente() throws PersistenciaException {
        when(mockEntityManager.find(Compra.class, 9999L)).thenReturn(null);

        Compra resultado = compraDAO.obtenerCompraPorId(9999L);
        assertNull(resultado);

        verify(mockEntityManager).find(Compra.class, 9999L);
    }

    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(1L);
        List<Compra> comprasEsperadas = Arrays.asList(
                new Compra("Compra 1", cliente),
                new Compra("Compra 2", cliente)
        );

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Compra c")).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(comprasEsperadas);

        List<Compra> resultado = compraDAO.obtenerTodasLasCompras();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(mockEntityManager).createQuery("SELECT c FROM Compra c");
        verify(mockQuery).getResultList();
    }

    @Test
    public void testObtenerTodasLasCompras_Vacio() throws PersistenciaException {
        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Compra c")).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        List<Compra> resultado = compraDAO.obtenerTodasLasCompras();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(mockEntityManager).createQuery("SELECT c FROM Compra c");
        verify(mockQuery).getResultList();
    }

    @Test
    public void testObtenerCompraPorNombreYCliente() throws PersistenciaException {
        Long clienteId = 1L;
        String nombreCompra = "Compra Test";
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente.setId(clienteId);
        Compra compraEsperada = new Compra(nombreCompra, cliente);

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Compra c WHERE c.nombre = :nombre AND c.cliente.id = :clienteId"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("nombre", nombreCompra)).thenReturn(mockQuery);
        when(mockQuery.setParameter("clienteId", clienteId)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(compraEsperada);

        Compra resultado = compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);

        assertNotNull(resultado);
        assertEquals(nombreCompra, resultado.getNombre());

        verify(mockEntityManager).createQuery("SELECT c FROM Compra c WHERE c.nombre = :nombre AND c.cliente.id = :clienteId");
        verify(mockQuery).setParameter("nombre", nombreCompra);
        verify(mockQuery).setParameter("clienteId", clienteId);
        verify(mockQuery).getSingleResult();
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_NoExiste() throws PersistenciaException {
        Long clienteId = 1L;
        String nombreCompra = "Compra Inexistente";

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Compra c WHERE c.nombre = :nombre AND c.cliente.id = :clienteId"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("nombre", nombreCompra)).thenReturn(mockQuery);
        when(mockQuery.setParameter("clienteId", clienteId)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(new NoResultException("No se encontró la compra"));

        Compra resultado = compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);

        assertNull(resultado);

        verify(mockEntityManager).createQuery("SELECT c FROM Compra c WHERE c.nombre = :nombre AND c.cliente.id = :clienteId");
        verify(mockQuery).setParameter("nombre", nombreCompra);
        verify(mockQuery).setParameter("clienteId", clienteId);
        verify(mockQuery).getSingleResult();
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_NombreNulo() {
        Long clienteId = 1L;

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.obtenerCompraPorNombreYCliente(null, clienteId);
        });
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_NombreVacio() {
        Long clienteId = 1L;

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.obtenerCompraPorNombreYCliente("", clienteId);
        });
    }
}
