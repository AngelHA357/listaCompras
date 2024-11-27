
package Subsistemas;

import Conversiones.ProductosConversiones;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorProductos;
import Subsistemas.IGestorProductos;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorProductosTest {
    
    public GestorProductosTest() {
    }
    
    private IGestorProductos gestorProductos;
    private IProductoDAO productoDAOMock;
    private ProductosConversiones conversionesMock;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true");
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba");
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
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
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
     *
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
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se producen problemas en la lógica de negocio
     * durante la operación.
     */
    @Test
    public void testActualizarProducto() throws PersistenciaException, NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
        productoDTO.setId(1L);

        Producto productoExistente = new Producto("Producto Original", "Categoria A", false, null, 5.0);
        productoExistente.setId(1L);

        Producto productoActualizado = new Producto("Producto Actualizado", "Categoria A", false, null, 10.0);
        productoActualizado.setId(1L);

        when(productoDAOMock.obtenerProductoPorId(1L)).thenReturn(productoExistente);
        when(conversionesMock.dtoAEntidad(productoDTO)).thenReturn(productoActualizado);
        when(productoDAOMock.actualizarProducto(productoActualizado)).thenReturn(productoActualizado);
        when(conversionesMock.entidadADTO(productoActualizado, false)).thenReturn(productoDTO);

        ProductoDTO resultado = gestorProductos.actualizarProducto(productoDTO);

        assertNotNull(resultado);
        assertEquals(productoDTO.getNombre(), resultado.getNombre());
        verify(productoDAOMock).obtenerProductoPorId(1L);
        verify(productoDAOMock).actualizarProducto(productoActualizado);
        verify(conversionesMock).entidadADTO(productoActualizado, false);
    }

    /**
     * Prueba la actualización de un producto que no existe. Verifica que se
     * lanza una excepción de negocio si no se encuentra el producto.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si el producto a actualizar no existe.
     */
    @Test
    public void testActualizarProductoQueNoExiste() throws PersistenciaException, NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5.0);
        productoDTO.setId(999L);

        when(productoDAOMock.obtenerProductoPorId(999L)).thenReturn(null);

        NegocioException thrown = assertThrows(
                NegocioException.class,
                () -> gestorProductos.actualizarProducto(productoDTO)
        );

        assertEquals("No se puede actualizar un producto inexistente con ID: 999", thrown.getMessage());
        verify(productoDAOMock).obtenerProductoPorId(999L);
        verify(productoDAOMock, never()).actualizarProducto(any());
        verify(conversionesMock, never()).entidadADTO(any(), anyBoolean());
    }

    /**
     * Prueba la eliminación de un producto. Verifica que se invoca
     * correctamente al DAO con el ID del producto.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se producen problemas en la lógica de negocio
     * durante la operación.
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
     * Prueba la obtención de un producto por ID. Verifica que se realicen las
     * conversiones y se obtenga el producto correcto.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se producen problemas en la lógica de negocio
     * durante la operación.
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
     * Prueba la eliminación de un producto inexistente. Verifica que se lanza
     * una excepción de negocio si el producto no existe.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se intenta eliminar un producto inexistente.
     */
    @Test
    public void testEliminarProductoInexistente() throws PersistenciaException, NegocioException {
        long idProductoInexistente = 999L;
        when(productoDAOMock.obtenerProductoPorId(idProductoInexistente)).thenReturn(null);

        NegocioException exception = assertThrows(
                NegocioException.class,
                () -> gestorProductos.eliminarProducto(idProductoInexistente)
        );

        assertEquals("No se puede eliminar un producto inexistente con ID: 999", exception.getMessage());
        verify(productoDAOMock, times(1)).obtenerProductoPorId(idProductoInexistente);
        verify(productoDAOMock, never()).eliminarProducto(anyLong());
    }

    /**
     * Prueba la obtención de un producto existente por sus características.
     * Verifica que se realicen las conversiones y se obtenga el producto
     * correcto.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se producen problemas en la lógica de negocio
     * durante la operación.
     */
    @Test
    public void testObtenerProductoExistentePorCaracteristicas() throws PersistenciaException, NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5d);
        Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5d);

        when(productoDAOMock.obtenerProductoPorCaracteristicas(any(), any(), anyBoolean(), any(), isNull())).thenReturn(producto);
        when(conversionesMock.entidadADTO(eq(producto), anyBoolean())).thenReturn(productoDTO);

        ProductoDTO resultadoDTO = gestorProductos.obtenerProductoPorCaracteristicas(
                productoDTO.getNombre(), productoDTO.getCategoria(), productoDTO.isComprado(), productoDTO.getCantidad(), null
        );

        assertNotNull(resultadoDTO);
        assertEquals(productoDTO.getNombre(), resultadoDTO.getNombre());
        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas(any(), any(), anyBoolean(), any(), isNull());
        verify(conversionesMock, times(1)).entidadADTO(eq(producto), anyBoolean());
    }

    /**
     * Prueba la obtención de un producto inexistente por características.
     * Verifica que se devuelva null cuando no se encuentra el producto.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la capa de
     * persistencia.
     * @throws NegocioException si se producen problemas en la lógica de negocio
     * durante la operación.
     */
    @Test
    public void testObtenerProductoInexistentePorCaracteristicas() throws PersistenciaException, NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5d);
        when(productoDAOMock.obtenerProductoPorCaracteristicas(any(), any(), anyBoolean(), any(), isNull())).thenReturn(null);

        assertThrows(
                NegocioException.class,
                () -> gestorProductos.obtenerProductoPorCaracteristicas(
                        productoDTO.getNombre(), productoDTO.getCategoria(), productoDTO.isComprado(), productoDTO.getCantidad(), null)
        );

        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas(any(), any(), anyBoolean(), any(), isNull());
        verify(conversionesMock, never()).entidadADTO(any(), anyBoolean());
    }

}
