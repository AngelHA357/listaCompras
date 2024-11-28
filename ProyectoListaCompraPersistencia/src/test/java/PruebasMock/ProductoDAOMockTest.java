/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

import Conexion.IConexion;
import DAOs.ProductoDAO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author JoseH
 */
public class ProductoDAOMockTest {

    @Mock
    private IConexion mockConexion;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    private ProductoDAO productoDAO;

    @BeforeEach
    public void setUp() throws PersistenciaException {
        MockitoAnnotations.openMocks(this);
        when(mockConexion.crearConexion()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).begin();
        doNothing().when(mockTransaction).commit();
        when(mockTransaction.isActive()).thenReturn(true);
        productoDAO = new ProductoDAO(mockConexion);
    }

    @Test
    public void testAgregarProducto() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente.setId(1L);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra.setId(1L);
        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);

        doAnswer(invocation -> {
            Producto productoArgumento = invocation.getArgument(0);
            productoArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Producto.class));

        when(mockEntityManager.find(eq(Producto.class), anyLong())).thenReturn(producto);

        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Papel", resultado.getNombre());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());
        assertEquals(compra.getId(), resultado.getCompra().getId());

        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(producto);
        verify(mockEntityManager).find(Producto.class, producto.getId());
        verify(mockTransaction).commit();
    }

    @Test
    public void testAgregarProducto_ErrorPersistencia() {
        Producto producto = new Producto("Papel", "Higiene Personal", false, new Compra(), 6.0);

        doThrow(new PersistenceException("Error al persistir"))
                .when(mockEntityManager).persist(any(Producto.class));

        assertThrows(PersistenciaException.class, () -> {
            productoDAO.agregarProducto(producto);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testAgregarProducto_NombreNulo() throws PersistenciaException {
        Producto producto = new Producto(null, "Higiene Personal", false, null, 6.0);

        doAnswer(invocation -> {
            Producto productoArgumento = invocation.getArgument(0);
            productoArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Producto.class));

        when(mockEntityManager.find(eq(Producto.class), anyLong())).thenReturn(producto);

        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());

        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(producto);
        verify(mockEntityManager).find(eq(Producto.class), anyLong());
        verify(mockTransaction).commit();
    }

    @Test
    public void testAgregarProducto_NombreVacio() throws PersistenciaException {
        Producto producto = new Producto("", "Higiene Personal", false, null, 6.0);

        doAnswer(invocation -> {
            Producto productoArgumento = invocation.getArgument(0);
            productoArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Producto.class));

        when(mockEntityManager.find(eq(Producto.class), anyLong())).thenReturn(producto);

        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());

        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(producto);
        verify(mockEntityManager).find(eq(Producto.class), anyLong());
        verify(mockTransaction).commit();
    }

    @Test
    public void testActualizarProducto() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente.setId(1L);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra.setId(1L);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        producto.setId(1L);

        Producto productoActualizado = new Producto("Papel Premium", "Higiene Personal", true, compra, 8.0);
        productoActualizado.setId(1L);

        when(mockEntityManager.merge(any(Producto.class))).thenReturn(productoActualizado);
        when(mockEntityManager.find(eq(Producto.class), eq(1L))).thenReturn(productoActualizado);

        Producto resultado = productoDAO.actualizarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Papel Premium", resultado.getNombre());
        assertEquals(8.0, resultado.getCantidad());
        assertTrue(resultado.isComprado());

        verify(mockTransaction).begin();
        verify(mockEntityManager).merge(producto);
        verify(mockTransaction).commit();
        verify(mockEntityManager).find(Producto.class, 1L);
    }

    @Test
    public void testActualizarProducto_ErrorActualizacion() {
        Producto producto = new Producto("Papel", "Higiene Personal", false, new Compra(), 6.0);
        producto.setId(1L);

        when(mockEntityManager.merge(any(Producto.class)))
                .thenThrow(new IllegalStateException("Error al actualizar"));

        assertThrows(PersistenciaException.class, () -> {
            productoDAO.actualizarProducto(producto);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testObtenerProductoPorId() throws PersistenciaException {
        Long productoId = 1L;
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente.setId(1L);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra.setId(1L);
        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        producto.setId(productoId);

        when(mockEntityManager.find(Producto.class, productoId)).thenReturn(producto);

        Producto resultado = productoDAO.obtenerProductoPorId(productoId);

        assertNotNull(resultado);
        assertEquals(producto.getId(), resultado.getId());
        assertEquals(producto.getNombre(), resultado.getNombre());
        assertEquals(producto.getCategoria(), resultado.getCategoria());
        assertEquals(producto.getCantidad(), resultado.getCantidad());

        verify(mockEntityManager).find(Producto.class, productoId);
    }

    @Test
    public void testObtenerProductoPorId_NoExiste() throws PersistenciaException {
        when(mockEntityManager.find(Producto.class, 99999L)).thenReturn(null);

        Producto resultado = productoDAO.obtenerProductoPorId(99999L);

        assertNull(resultado);
        verify(mockEntityManager).find(Producto.class, 99999L);
    }

    @Test
    public void testEliminarProducto() throws PersistenciaException {
        Long productoId = 1L;
        Producto producto = new Producto("Papel", "Higiene Personal", false, new Compra(), 6.0);
        producto.setId(productoId);

        when(mockEntityManager.find(Producto.class, productoId)).thenReturn(producto);
        doNothing().when(mockEntityManager).remove(producto);

        Producto resultado = productoDAO.eliminarProducto(productoId);

        assertNotNull(resultado);
        assertEquals(productoId, resultado.getId());

        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Producto.class, productoId);
        verify(mockEntityManager).remove(producto);
        verify(mockTransaction).commit();
    }

    @Test
    public void testEliminarProducto_NoExiste() throws PersistenciaException {
        Long idInexistente = 99999L;

        when(mockEntityManager.find(Producto.class, idInexistente)).thenReturn(null);

        Producto resultado = productoDAO.eliminarProducto(idInexistente);

        assertNull(resultado);
        verify(mockEntityManager).find(Producto.class, idInexistente);
        verify(mockTransaction).begin();
        verify(mockTransaction).commit();
        verify(mockEntityManager, never()).remove(any());
    }

    @Test
    public void testEliminarProducto_ErrorEliminacion() {
        Long productoId = 1L;
        Producto producto = new Producto("Papel", "Higiene Personal", false, new Compra(), 6.0);
        producto.setId(productoId);

        when(mockEntityManager.find(Producto.class, productoId)).thenReturn(producto);
        doThrow(new IllegalStateException("Error al eliminar"))
                .when(mockEntityManager).remove(any(Producto.class));

        assertThrows(PersistenciaException.class, () -> {
            productoDAO.eliminarProducto(productoId);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testObtenerProductosPorCompraId() throws PersistenciaException {
        // Datos de prueba
        Long compraId = 1L;
        List<Producto> productosEsperados = Arrays.asList(
                new Producto("Papel", "Higiene Personal", false, new Compra(), 6.0),
                new Producto("Jabón", "Higiene Personal", false, new Compra(), 3.0)
        );

        // Configurar el mock para Query
        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT p FROM Producto p WHERE p.compra.id = :compraId"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("compraId", compraId)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(productosEsperados);

        // Ejecutar el método
        List<Producto> resultado = productoDAO.obtenerProductosPorCompraId(compraId);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(mockEntityManager).createQuery("SELECT p FROM Producto p WHERE p.compra.id = :compraId");
        verify(mockQuery).setParameter("compraId", compraId);
        verify(mockQuery).getResultList();
    }

    @Test
    public void testObtenerProductosPorCompraId_CompraInexistente() throws PersistenciaException {
        Long compraIdInexistente = 99999L;

        // Configurar el mock para Query con lista vacía
        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT p FROM Producto p WHERE p.compra.id = :compraId"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("compraId", compraIdInexistente)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        // Ejecutar el método
        List<Producto> resultado = productoDAO.obtenerProductosPorCompraId(compraIdInexistente);

        // Verificaciones
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(mockEntityManager).createQuery(anyString());
        verify(mockQuery).setParameter("compraId", compraIdInexistente);
    }
}
