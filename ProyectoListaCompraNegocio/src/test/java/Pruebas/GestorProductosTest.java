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
 * @author JoseH
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

    @Test
    public void testAgregarProducto() throws PersistenciaException {
        try {
            // Creamos un ProductoDTO de prueba
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

    @Test
    public void testAgregarProductoConDatosNulos() {
        ProductoDTO productoDTO = new ProductoDTO(null, null, false, null, 0.0);

        assertThrows(NegocioException.class, () -> {
            gestorProductos.agregarProducto(productoDTO);
        });
    }

    @Test
    public void testActualizarProducto() throws PersistenciaException {
        // Creamos un ProductoDTO de prueba
        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
        Producto producto = new Producto("Producto Original", "Categoria A", false, null, 5.0);

        // Simulamos la conversión de ProductoDTO a Producto
        when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);

        // Simulamos que el DAO guarda el producto y devuelve la entidad
        when(productoDAOMock.actualizarProducto(any(Producto.class))).thenReturn(producto);

        // Simulamos la conversión de Producto a ProductoDTO para el retorno
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        // Llamamos al método bajo prueba
        ProductoDTO resultadoDTO = gestorProductos.actualizarProducto(productoDTO);

        // Verificamos las interacciones con los mocks
        verify(conversionesMock, times(1)).dtoAEntidad(productoDTO);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);
        verify(productoDAOMock, times(1)).actualizarProducto(producto);

        // Verificamos que el resultado no sea nulo y sea correcto
        assertNotNull(resultadoDTO);
        assertEquals("Producto Original", resultadoDTO.getNombre());

        // Cambiamos el nombre del producto y volvemos a actualizar
        productoDTO.setNombre("Producto Actualizado");
        producto.setNombre("Producto Actualizado");

        resultadoDTO = gestorProductos.actualizarProducto(productoDTO);

        // Verificamos que el resultado actualizado sea correcto
        assertNotNull(resultadoDTO);
        assertEquals("Producto Actualizado", resultadoDTO.getNombre());
    }

    @Test
    public void testActualizarProductoQueNoExiste() throws PersistenciaException {
        // Creamos un ProductoDTO para un producto que no existe
        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5.0);
        Producto producto = new Producto("Producto Inexistente", "Categoria X", false, null, 5.0);

        // Simulamos la conversión de ProductoDTO a Producto
        when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);

        // Simulamos la conversión de Producto a ProductoDTO para el retorno
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        // Simulamos que el DAO no encuentra el producto para actualizar
        when(productoDAOMock.actualizarProducto(any(Producto.class))).thenReturn(null);

        // Llamamos al método bajo prueba
        ProductoDTO resultado = gestorProductos.actualizarProducto(productoDTO);

        // Verificamos que el resultado sea null si el producto no existe
        assertNull(resultado);

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).actualizarProducto(any(Producto.class));
    }
//    

    @Test
    public void testEliminarProducto() throws PersistenciaException {
        // Simulamos un ID de un producto
        Long id = 1L;

        gestorProductos.eliminarProducto(id);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock).eliminarProducto(anyLong());
        verify(productoDAOMock).eliminarProducto(longArgumentCaptor.capture());
        assertEquals(1L, longArgumentCaptor.getValue());
    }

    @Test
    public void testEliminarProductoQueNoExiste() throws PersistenciaException {
        // Simulamos un ID de un producto inexistente
        Long idInexistente = 999L;

        gestorProductos.eliminarProducto(idInexistente);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock).eliminarProducto(anyLong());
        verify(productoDAOMock).eliminarProducto(longArgumentCaptor.capture());
        assertEquals(idInexistente, longArgumentCaptor.getValue());
    }
//    

    @Test
    public void testObtenerProductoPorId() throws PersistenciaException {
        // Creamos un ProductoDTO de prueba
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
        Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5.0);

        // Simulamos que el DAO retorna el producto al buscar por ID
        when(productoDAOMock.obtenerProductoPorId(anyLong())).thenReturn(producto);

        // Simulamos la conversión de Producto a ProductoDTO
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        // Llamamos al método bajo prueba
        ProductoDTO resultadoDTO = gestorProductos.obtenerProductoPorId(1L);

        // Verificamos las interacciones con los mocks
        verify(productoDAOMock, times(1)).obtenerProductoPorId(1L);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);

        // Verificamos que el resultado no sea nulo y sea correcto
        assertNotNull(resultadoDTO);
        assertEquals("Producto de Prueba", resultadoDTO.getNombre());
    }

    @Test
    public void testObtenerProductoPorId_Inexistente() throws PersistenciaException {
        long idInexistente = 9999L;

        // Simulamos que el DAO no encuentra el producto
        when(productoDAOMock.obtenerProductoPorId(idInexistente)).thenReturn(null);

        // Llamamos al método bajo prueba
        ProductoDTO resultado = gestorProductos.obtenerProductoPorId(idInexistente);

        // Verificamos que el resultado sea null si no existe el producto
        assertNull(resultado);

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductoPorId(idInexistente);
    }

    @Test
    public void testObtenerProductoPorCaracteristicas() throws PersistenciaException {
        try {
            // Crear mocks necesarios
            ICompraDAO compraDAOMock = mock(ICompraDAO.class);
            CompraConversiones conversionesCompra = mock(CompraConversiones.class);
            IGestorCompras compraBO = new GestorCompras(compraDAOMock, conversionesCompra);

            // Creamos una CompraDTO de prueba
            CompraDTO compraDTO = new CompraDTO("Compra", null);
            Compra compra = new Compra("Compra", null);
            when(conversionesCompra.dtoAEntidad(compraDTO)).thenReturn(compra);
            when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
            when(conversionesCompra.entidadADTO(compra)).thenReturn(compraDTO);
            compraDTO = compraBO.agregarCompra(compraDTO);

            // Creamos un ProductoDTO con características específicas
            ProductoDTO productoDTO = new ProductoDTO("Producto E", "Categoria E", true, compraDTO, 35.0);
            Producto producto = new Producto("Producto E", "Categoria E", true, compra, 35.0);

            when(conversionesMock.dtoAEntidad(productoDTO)).thenReturn(producto);
            when(conversionesMock.entidadADTO(producto, false)).thenReturn(productoDTO);
            when(productoDAOMock.agregarProducto(any(Producto.class))).thenReturn(producto);

            // Simulamos la búsqueda del producto por características
            when(productoDAOMock.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId())).thenReturn(producto);

            // Llamamos al método bajo prueba
            ProductoDTO resultado = gestorProductos.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());

            // Verificaciones
            assertNotNull(resultado);
            assertEquals("Producto E", resultado.getNombre());
            assertEquals("Categoria E", resultado.getCategoria());
            assertEquals(35.0, resultado.getCantidad());

            // Verificación de que el método fue invocado correctamente
            verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());
        } catch (NegocioException ex) {
            Logger.getLogger(GestorProductosTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testObtenerProductoPorCaracteristicasInexistentes() throws PersistenciaException {
        // Simulamos que no se encuentra un producto con las características especificadas
        when(productoDAOMock.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L)).thenReturn(null);

        ProductoDTO resultado = gestorProductos.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);

        // Verificación
        assertNull(resultado);

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);
    }

    @Test
    public void testObtenerTodosLosProductos() throws PersistenciaException {
        // Creamos datos de prueba
        Producto producto1 = new Producto("Producto F", "Categoria F", false, null, 40.0);
        Producto producto2 = new Producto("Producto G", "Categoria G", false, null, 45.0);
        ProductoDTO productoDTO1 = new ProductoDTO("Producto F", "Categoria F", false, null, 40.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto G", "Categoria G", false, null, 45.0);

        // Simulamos que el DAO retorna una lista de productos
        when(productoDAOMock.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto1, producto2));

        // Simulamos la conversión de Producto a ProductoDTO
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean()))
                .thenReturn(productoDTO1)
                .thenReturn(productoDTO2);

        // Llamamos al método bajo prueba
        List<ProductoDTO> resultado = gestorProductos.obtenerTodosLosProductos();

        // Verificamos las interacciones con los mocks
        verify(productoDAOMock, times(1)).obtenerTodosLosProductos();
        verify(conversionesMock, times(2)).entidadADTO(any(Producto.class), anyBoolean());

        // Verificamos el resultado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto F")));
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto G")));
    }

    @Test
    public void testObtenerTodosLosProductosSinProductos() throws PersistenciaException {
        // Simulamos que el método del DAO devuelve una lista vacía
        when(productoDAOMock.obtenerTodosLosProductos()).thenReturn(Collections.emptyList());

        // Llamamos al método bajo prueba
        List<ProductoDTO> resultado = gestorProductos.obtenerTodosLosProductos();

        // Verificaciones
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificamos que el método del DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerTodosLosProductos();
    }

}
