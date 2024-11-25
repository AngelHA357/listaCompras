/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

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
import static org.mockito.Mockito.never;
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
            when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

            ProductoDTO resultadoDTO = gestorProductos.agregarProducto(productoDTO);

            verify(conversionesMock, times(1)).dtoAEntidad(productoDTO);
            verify(conversionesMock, times(1)).entidadADTO(producto, false);
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
//    @Test
//    public void testActualizarProducto() throws PersistenciaException, NegocioException {
//        Producto productoExistente = new Producto("Producto Original", "Categoria A", false, null, 5.0);
//        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
//        Producto productoActualizado = new Producto("Producto Actualizado", "Categoria A", false, null, 10.0);
//
//        when(productoDAOMock.obtenerProductoPorId(1L)).thenReturn(productoExistente);
//        when(conversionesMock.dtoAEntidad(productoDTO)).thenReturn(productoActualizado);
//        when(productoDAOMock.actualizarProducto(productoActualizado)).thenReturn(productoActualizado);
//        when(conversionesMock.entidadADTO(productoActualizado, false)).thenReturn(productoDTO);
//
//        ProductoDTO resultado = gestorProductos.actualizarProducto(productoDTO);
//
//        assertNotNull(resultado);
//        assertEquals(productoDTO.getNombre(), resultado.getNombre());
//        verify(productoDAOMock, times(1)).obtenerProductoPorId(1L);
//        verify(productoDAOMock, times(1)).actualizarProducto(productoActualizado);
//    }

    /**
     * Prueba la actualización de un producto que no existe. Verifica que el
     * resultado sea nulo si no se encuentra el producto.
     */
//    @Test
//    public void testActualizarProductoQueNoExiste() throws PersistenciaException, NegocioException {
//        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5.0);
//        
//        when(productoDAOMock.obtenerProductoPorId(999L)).thenReturn(null);
//
//        assertThrows(NegocioException.class, () -> gestorProductos.actualizarProducto(productoDTO));
//
//        verify(productoDAOMock, times(1)).obtenerProductoPorId(999L);
//        verify(productoDAOMock, never()).actualizarProducto(any());
//    }

    /**
     * Prueba la eliminación de un producto. Verifica que se invoca
     * correctamente al DAO con el ID del producto.
     */
    @Test
    public void testEliminarProducto() throws PersistenciaException, NegocioException {
        Producto productoExistente = new Producto("Producto Original", "Categoria A", false, null, 5.0);

         when(productoDAOMock.obtenerProductoPorId(1L)).thenReturn(productoExistente);

        gestorProductos.eliminarProducto(1L);

        verify(productoDAOMock, times(1)).obtenerProductoPorId(1L);
        verify(productoDAOMock, times(1)).eliminarProducto(1L);
    }

    /**
     * Prueba la eliminación de un producto que no existe. Verifica que se
     * invoca correctamente al DAO con el ID inexistente.
     */
    @Test
    public void testEliminarProductoQueNoExiste() throws PersistenciaException, NegocioException {
        when(productoDAOMock.obtenerProductoPorId(999L)).thenReturn(null);

        assertThrows(NegocioException.class, () -> gestorProductos.eliminarProducto(999L));

        verify(productoDAOMock, times(1)).obtenerProductoPorId(999L);
        verify(productoDAOMock, never()).eliminarProducto(anyLong());
    }

    /**
     * Prueba la obtención de un producto por ID. Verifica que se realicen las
     * conversiones y se obtenga el producto correcto.
     */
    @Test
    public void testObtenerProductoPorId() throws PersistenciaException, NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
        Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5.0);

        when(productoDAOMock.obtenerProductoPorId(anyLong())).thenReturn(producto);
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        ProductoDTO resultadoDTO = gestorProductos.obtenerProductoPorId(1L);

        verify(productoDAOMock, times(1)).obtenerProductoPorId(1L);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);

        assertNotNull(resultadoDTO);
        assertEquals("Producto de Prueba", resultadoDTO.getNombre());
    }

    /**
     * Prueba la obtención de un producto que no existe por ID. Verifica que el
     * resultado sea nulo si no se encuentra el producto.
     */
    @Test
    public void testObtenerProductoPorId_Inexistente() throws PersistenciaException, NegocioException {
        when(productoDAOMock.obtenerProductoPorId(999L)).thenReturn(null);

        assertThrows(NegocioException.class, () -> gestorProductos.obtenerProductoPorId(999L));

        verify(productoDAOMock, times(1)).obtenerProductoPorId(999L);
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
            when(conversionesMock.entidadADTO(producto, false)).thenReturn(productoDTO);
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
    public void testObtenerProductoPorCaracteristicasInexistentes() throws PersistenciaException, NegocioException {
         when(productoDAOMock.obtenerProductoPorCaracteristicas("Inexistente", "Categoría X", true, 5.0, 999L))
            .thenReturn(null);

        assertThrows(NegocioException.class, () -> gestorProductos.obtenerProductoPorCaracteristicas(
            "Inexistente", "Categoría X", true, 5.0, 999L
        ));

        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Inexistente", "Categoría X", true, 5.0, 999L);
    }

}
