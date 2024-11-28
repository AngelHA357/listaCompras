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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author JoseH
 */
public class CompraMockTest {

    @Mock
    private Cliente mockCliente;

    @Mock
    private IConexion mockConexion;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    private Compra compra;
    private ProductoDAO productoDAO;

    public CompraMockTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws PersistenciaException {
        MockitoAnnotations.openMocks(this);

        when(mockCliente.getId()).thenReturn(1L);
        when(mockCliente.getNombre()).thenReturn("Cliente Test");

        when(mockConexion.crearConexion()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).begin();
        doNothing().when(mockTransaction).commit();
        when(mockTransaction.isActive()).thenReturn(true);

        compra = new Compra("Compra Test", mockCliente);
        productoDAO = new ProductoDAO(mockConexion);
    }

    @Test
    public void testAgregarProductoACompra() throws PersistenciaException {
        Producto producto = new Producto("Producto Test", "Categoría Test", compra, 10.0);


        doAnswer(invocation -> {
            Producto productoArgumento = invocation.getArgument(0);
            productoArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Producto.class));

        when(mockEntityManager.find(eq(Producto.class), anyLong())).thenReturn(producto);

        Producto productoAgregado = productoDAO.agregarProducto(producto);

        compra.agregarProducto(productoAgregado);

        assertNotNull(compra.getProductos());
        assertEquals(1, compra.getProductos().size());
        assertEquals("Producto Test", compra.getProductos().get(0).getNombre());
        assertEquals("Categoría Test", compra.getProductos().get(0).getCategoria());
        assertEquals(compra, compra.getProductos().get(0).getCompra());

        verify(mockEntityManager).persist(any(Producto.class));
        verify(mockEntityManager).find(eq(Producto.class), anyLong());
        verify(mockTransaction).begin();
        verify(mockTransaction).commit();
    }

    @AfterEach
    public void tearDown() {
    }

}
