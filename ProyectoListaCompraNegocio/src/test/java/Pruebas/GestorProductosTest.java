/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import Conversiones.CompraConversiones;
import Conversiones.ProductosConversiones;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorCompras;
import Subsistemas.GestorProductos;
import Subsistemas.IGestorCompras;
import Subsistemas.IGestorProductos;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345 .
 */
public class GestorProductosTest {

    private IGestorProductos gestorProductos;
    private IProductoDAO productoDAOMock;
    private ProductosConversiones conversionesMock;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true"); // Activar modo prueba si es necesario
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba"); // Limpiar propiedades al finalizar
    }

    @BeforeEach
    public void setUp() throws PersistenciaException {
        productoDAOMock = mock(ProductoDAO.class);
        conversionesMock = mock(ProductosConversiones.class);

        gestorProductos = new GestorProductos(productoDAOMock, conversionesMock);
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Prueba la adición de un producto. Verifica que se realicen las
     * conversiones y se guarde correctamente en el DAO.
     */
    @Test
    public void testAgregarProducto() throws PersistenciaException {
        try {
            ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
            Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5.0);

            when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);
            when(productoDAOMock.agregarProducto(any(Producto.class))).thenReturn(producto);
            when(conversionesMock.entidadADTO(any(Producto.class))).thenReturn(productoDTO);

            ProductoDTO resultadoDTO = gestorProductos.agregarProducto(productoDTO);

            verify(conversionesMock, times(1)).dtoAEntidad(productoDTO);
            verify(conversionesMock, times(1)).entidadADTO(producto);
            verify(productoDAOMock, times(1)).agregarProducto(producto);

            assertNotNull(resultadoDTO);
            assertEquals("Producto de Prueba", resultadoDTO.getNombre());
        } catch (NegocioException ex) {
            Logger.getLogger(GestorProductosTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prueba la adición de un producto con datos nulos. Verifica que se lanza
     * una excepción de negocio.
     */
    @Test
    public void testAgregarProductoConDatosNulos() {
        ProductoDTO productoDTO = new ProductoDTO(null, null, false, null, 0.0);

        assertThrows(NegocioException.class, () -> {
            gestorProductos.agregarProducto(productoDTO);
        });
    }

    /**
     * Prueba la actualización de un producto. Verifica que se realicen las
     * conversiones y se actualice correctamente en el DAO.
     */
    @Test
    public void testActualizarProducto() throws PersistenciaException {
        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
        Producto producto = new Producto("Producto Original", "Categoria A", false, null, 5.0);

        when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);
        when(productoDAOMock.actualizarProducto(any(Producto.class))).thenReturn(producto);
        when(conversionesMock.entidadADTO(any(Producto.class))).thenReturn(productoDTO);

        ProductoDTO resultadoDTO = gestorProductos.actualizarProducto(productoDTO);

        verify(conversionesMock, times(1)).dtoAEntidad(productoDTO);
        verify(conversionesMock, times(1)).entidadADTO(producto);
        verify(productoDAOMock, times(1)).actualizarProducto(producto);

        assertNotNull(resultadoDTO);
        assertEquals("Producto Original", resultadoDTO.getNombre());

        productoDTO.setNombre("Producto Actualizado");
        producto.setNombre("Producto Actualizado");

        resultadoDTO = gestorProductos.actualizarProducto(productoDTO);

        assertNotNull(resultadoDTO);
        assertEquals("Producto Actualizado", resultadoDTO.getNombre());
    }

    /**
     * Prueba la actualización de un producto que no existe. Verifica que el
     * resultado sea nulo si no se encuentra el producto.
     */
    @Test
    public void testActualizarProductoQueNoExiste() throws PersistenciaException {
        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5.0);
        Producto producto = new Producto("Producto Inexistente", "Categoria X", false, null, 5.0);

        when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);
        when(conversionesMock.entidadADTO(any(Producto.class))).thenReturn(productoDTO);
        when(productoDAOMock.actualizarProducto(any(Producto.class))).thenReturn(null);

        ProductoDTO resultado = gestorProductos.actualizarProducto(productoDTO);

        assertNull(resultado);
        verify(productoDAOMock, times(1)).actualizarProducto(any(Producto.class));
    }

    /**
     * Prueba la eliminación de un producto. Verifica que se invoca
     * correctamente al DAO con el ID del producto.
     */
    @Test
    public void testEliminarProducto() throws PersistenciaException {
        Long id = 1L;

        gestorProductos.eliminarProducto(id);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(productoDAOMock).eliminarProducto(anyLong());
        verify(productoDAOMock).eliminarProducto(longArgumentCaptor.capture());
        assertEquals(1L, longArgumentCaptor.getValue());
    }

    /**
     * Prueba la eliminación de un producto que no existe. Verifica que se
     * invoca correctamente al DAO con el ID inexistente.
     */
    @Test
    public void testEliminarProductoQueNoExiste() throws PersistenciaException {
        Long idInexistente = 999L;

        gestorProductos.eliminarProducto(idInexistente);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(productoDAOMock).eliminarProducto(anyLong());
        verify(productoDAOMock).eliminarProducto(longArgumentCaptor.capture());
        assertEquals(idInexistente, longArgumentCaptor.getValue());
    }

    /**
     * Prueba la obtención de un producto por ID. Verifica que se realicen las
     * conversiones y se obtenga el producto correcto.
     */
    @Test
    public void testObtenerProductoPorId() throws PersistenciaException {
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
        Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5.0);

        when(productoDAOMock.obtenerProductoPorId(anyLong())).thenReturn(producto);
        when(conversionesMock.entidadADTO(any(Producto.class))).thenReturn(productoDTO);

        ProductoDTO resultadoDTO = gestorProductos.obtenerProductoPorId(1L);

        verify(productoDAOMock, times(1)).obtenerProductoPorId(1L);
        verify(conversionesMock, times(1)).entidadADTO(producto);

        assertNotNull(resultadoDTO);
        assertEquals("Producto de Prueba", resultadoDTO.getNombre());
    }

    /**
     * Prueba la obtención de un producto que no existe por ID. Verifica que el
     * resultado sea nulo si no se encuentra el producto.
     */
    @Test
    public void testObtenerProductoPorId_Inexistente() throws PersistenciaException {
        long idInexistente = 9999L;

        when(productoDAOMock.obtenerProductoPorId(idInexistente)).thenReturn(null);

        ProductoDTO resultado = gestorProductos.obtenerProductoPorId(idInexistente);

        assertNull(resultado);
        verify(productoDAOMock, times(1)).obtenerProductoPorId(idInexistente);
    }

    /**
     * Prueba la obtención de un producto por características específicas.
     * Verifica que se obtenga el producto correcto al buscar por
     * características.
     */
    @Test
    public void testObtenerProductoPorCaracteristicas() throws PersistenciaException {
        try {
            ICompraDAO compraDAOMock = mock(ICompraDAO.class);
            CompraConversiones conversionesCompra = mock(CompraConversiones.class);
            IGestorCompras compraBO = new GestorCompras(compraDAOMock, conversionesCompra);

            CompraDTO compraDTO = new CompraDTO("Compra", null);
            Compra compra = new Compra("Compra", null);
            when(conversionesCompra.dtoAEntidad(compraDTO)).thenReturn(compra);
            when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
            when(conversionesCompra.entidadADTO(compra)).thenReturn(compraDTO);
            compraDTO = compraBO.agregarCompra(compraDTO);

            ProductoDTO productoDTO = new ProductoDTO("Producto E", "Categoria E", true, compraDTO, 35.0);
            Producto producto = new Producto("Producto E", "Categoria E", true, compra, 35.0);

            when(conversionesMock.dtoAEntidad(productoDTO)).thenReturn(producto);
            when(conversionesMock.entidadADTO(producto)).thenReturn(productoDTO);
            when(productoDAOMock.agregarProducto(any(Producto.class))).thenReturn(producto);

            when(productoDAOMock.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId())).thenReturn(producto);

            ProductoDTO resultado = gestorProductos.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());

            assertNotNull(resultado);
            assertEquals("Producto E", resultado.getNombre());
            assertEquals("Categoria E", resultado.getCategoria());
            assertEquals(35.0, resultado.getCantidad());

            verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());
        } catch (NegocioException ex) {
            Logger.getLogger(GestorProductosTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prueba la obtención de un producto por características inexistentes.
     * Verifica que el resultado sea nulo si no se encuentra el producto.
     */
    @Test
    public void testObtenerProductoPorCaracteristicasInexistentes() throws PersistenciaException {
        // Se simula que no se encuentra un producto con las características especificadas
        when(productoDAOMock.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L)).thenReturn(null);

        ProductoDTO resultado = gestorProductos.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);

        // Verificación
        assertNull(resultado);

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);
    }

    /**
     * Verifica la obtención de todos los productos existentes. Simula una lista
     * de productos y comprueba la correcta interacción con el DAO.
     */
    @Test
    public void testObtenerTodosLosProductos() throws PersistenciaException {
        // Se crean datos de prueba
        Producto producto1 = new Producto("Producto F", "Categoria F", false, null, 40.0);
        Producto producto2 = new Producto("Producto G", "Categoria G", false, null, 45.0);
        ProductoDTO productoDTO1 = new ProductoDTO("Producto F", "Categoria F", false, null, 40.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto G", "Categoria G", false, null, 45.0);

        // Se simula que el DAO retorna una lista de productos
        when(productoDAOMock.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto1, producto2));

        // Se simula la conversión de Producto a ProductoDTO
        when(conversionesMock.entidadADTO(any(Producto.class)))
                .thenReturn(productoDTO1)
                .thenReturn(productoDTO2);

        // Se llama al método bajo prueba
        List<ProductoDTO> resultado = gestorProductos.obtenerTodosLosProductos();

        // Se verifica las interacciones con los mocks
        verify(productoDAOMock, times(1)).obtenerTodosLosProductos();
        verify(conversionesMock, times(2)).entidadADTO(any(Producto.class));

        // Se verifica el resultado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto F")));
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto G")));
    }

    /**
     * Verifica la obtención de todos los productos cuando no existen productos.
     * Simula una lista vacía y comprueba la correcta interacción con el DAO.
     */
    @Test
    public void testObtenerTodosLosProductosSinProductos() throws PersistenciaException {
        // Se simula que el método del DAO devuelve una lista vacía
        when(productoDAOMock.obtenerTodosLosProductos()).thenReturn(Collections.emptyList());

        // Se llama al método bajo prueba
        List<ProductoDTO> resultado = gestorProductos.obtenerTodosLosProductos();

        // Verificaciones
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Se verifica que el método del DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerTodosLosProductos();
    }

}
