/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import Subsistemas.CompraBO;
import Subsistemas.ICompraBO;
import Subsistemas.IProductoBO;
import Subsistemas.ProductoBO;
import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.CompraConversiones;
import Conversiones.ProductosConversiones;
import DAOs.ClienteDAO;
import DAOs.CompraDAO;
import DAOs.IClienteDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author JoseH
 */
public class ProductoBOTest {
    private IProductoBO productoBO;
    private IProductoDAO productoDAOMock;
    private ProductosConversiones conversionesMock;
    
    public ProductoBOTest() {
    }
    
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
        
        productoBO = new ProductoBO(productoDAOMock, conversionesMock);
    }
    
    @AfterEach
    public void tearDown() {
    }
   

    @Test
    public void testAgregarProducto() throws PersistenciaException {
        // Creamos un ProductoDTO de prueba
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);

        // Simulamos la conversión de ProductoDTO a Producto usando el mock de ProductosConversiones
        Producto producto = new Producto("Producto de Prueba", "Categoria A", false, null, 5.0);
        when(conversionesMock.dtoAEntidad(any(ProductoDTO.class))).thenReturn(producto);

        // Simulamos que el DAO guarda el producto y devuelve la entidad
        when(productoDAOMock.agregarProducto(any(Producto.class))).thenReturn(producto);

        // Simulamos la conversión de Producto a ProductoDTO para el retorno
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        // Llamamos al método bajo prueba
        ProductoDTO resultadoDTO = productoBO.agregarProducto(productoDTO);

        // Verificamos las interacciones con los mocks
        verify(conversionesMock, times(1)).dtoAEntidad(productoDTO);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);
        verify(productoDAOMock, times(1)).agregarProducto(producto);

        // Verificamos que el resultado no sea nulo y sea correcto
        assertNotNull(resultadoDTO);
        assertEquals("Producto de Prueba", resultadoDTO.getNombre());
    }
    
//    @Test
//    public void testAgregarProductoConDatosNulos() {
//        ProductoDTO productoDTO = new ProductoDTO(null, null, false, null, 0.0);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            productoBO.agregarProducto(productoDTO);
//        });
//
//        assertEquals("Los datos del producto no pueden ser nulos excepto la categoría", exception.getMessage());
//    }
    
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
        ProductoDTO resultadoDTO = productoBO.actualizarProducto(productoDTO);

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

        resultadoDTO = productoBO.actualizarProducto(productoDTO);

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
        ProductoDTO resultado = productoBO.actualizarProducto(productoDTO);

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

        productoBO.eliminarProducto(id);

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

        productoBO.eliminarProducto(idInexistente);

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
        ProductoDTO resultadoDTO = productoBO.obtenerProductoPorId(1L);

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
        ProductoDTO resultado = productoBO.obtenerProductoPorId(idInexistente);

        // Verificamos que el resultado sea null si no existe el producto
        assertNull(resultado);

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductoPorId(idInexistente);
    }
    
    @Test
    public void testFiltrarPorCategoriaYCompraId() throws PersistenciaException {
        // Creamos datos de prueba
        CompraDTO compraDTO = new CompraDTO("Compra 1", null);
        ProductoDTO productoDTO = new ProductoDTO("Producto A", "Categoria C", false, compraDTO, 15.0);
        Producto producto = new Producto("Producto A", "Categoria C", false, null, 15.0);

        // Simulamos que el DAO retorna una lista de productos
        when(productoDAOMock.filtrarPorCategoriaYCompraId(anyString(), anyLong())).thenReturn(Arrays.asList(producto));

        // Simulamos la conversión de Producto a ProductoDTO
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        // Llamamos al método bajo prueba
        List<ProductoDTO> resultado = productoBO.filtrarPorCategoriaYCompraId("Categoria C", 1L);

        // Verificamos las interacciones con los mocks
        verify(productoDAOMock, times(1)).filtrarPorCategoriaYCompraId("Categoria C", 1L);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);

        // Verificamos el resultado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto A", resultado.get(0).getNombre());
    }
    
    @Test
    public void testFiltrarPorCategoriaYCompraId_SinResultados() throws PersistenciaException {
        // Simulamos que no hay productos que coincidan con la categoría y el ID de la compra
        when(productoDAOMock.filtrarPorCategoriaYCompraId(anyString(), anyLong())).thenReturn(Collections.emptyList());

        // Llamamos al método bajo prueba
        List<ProductoDTO> resultado = productoBO.filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);

        // Verificamos que el resultado sea una lista vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);
    }
    
    @Test
    public void testFiltrarPorCompra() throws PersistenciaException {
        // Crear mocks necesarios
        ICompraDAO compraDAOMock = mock(ICompraDAO.class);
        CompraConversiones conversionesCompra = mock(CompraConversiones.class);
        ICompraBO compraBO = new CompraBO(compraDAOMock, conversionesCompra);

        // Creamos una CompraDTO de prueba
        CompraDTO compraDTO = new CompraDTO("Compra", null);
        Compra compra = new Compra("Compra", null);
        when(conversionesCompra.dtoAEntidad(compraDTO)).thenReturn(compra);
        when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
        when(conversionesCompra.entidadADTO(compra)).thenReturn(compraDTO);
        compraDTO = compraBO.agregarCompra(compraDTO);

        // Creamos los productos asociados a la compra
        ProductoDTO productoDTO1 = new ProductoDTO("Producto C", "Categoria D", false, compraDTO, 25.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto D", "Categoria D", false, compraDTO, 30.0);
        Producto producto1 = new Producto("Producto C", "Categoria D", false, compra, 25.0);
        Producto producto2 = new Producto("Producto D", "Categoria D", false, compra, 30.0);

        when(conversionesMock.dtoAEntidad(productoDTO1)).thenReturn(producto1);
        when(conversionesMock.dtoAEntidad(productoDTO2)).thenReturn(producto2);
        when(productoDAOMock.agregarProducto(any(Producto.class))).thenReturn(producto1).thenReturn(producto2);

        productoBO.agregarProducto(productoDTO1);
        productoBO.agregarProducto(productoDTO2);

        // Simulamos el filtro por compra
        when(productoDAOMock.obtenerProductosPorCompraId(anyLong())).thenReturn(Arrays.asList(producto1, producto2));

        List<ProductoDTO> resultado = productoBO.filtrarPorCompra(1L);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductosPorCompraId(1L);
    }
    
    @Test
    public void testFiltrarPorCompraInexistente() throws PersistenciaException {
        Long compraIdInexistente = 999L;

        // Simulamos que no se encuentran productos para la compra inexistente
        when(productoDAOMock.obtenerProductosPorCompraId(compraIdInexistente)).thenReturn(Collections.emptyList());

        List<ProductoDTO> resultado = productoBO.filtrarPorCompra(compraIdInexistente);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductosPorCompraId(compraIdInexistente);
    }

    @Test
    public void testObtenerProductoPorCaracteristicas() throws PersistenciaException {
        // Crear mocks necesarios
        ICompraDAO compraDAOMock = mock(ICompraDAO.class);
        CompraConversiones conversionesCompra = mock(CompraConversiones.class);
        ICompraBO compraBO = new CompraBO(compraDAOMock, conversionesCompra);

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
        ProductoDTO resultado = productoBO.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("Producto E", resultado.getNombre());
        assertEquals("Categoria E", resultado.getCategoria());
        assertEquals(35.0, resultado.getCantidad());

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());
    }
    
    
    @Test
    public void testObtenerProductoPorCaracteristicasInexistentes() throws PersistenciaException {
        // Simulamos que no se encuentra un producto con las características especificadas
        when(productoDAOMock.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L)).thenReturn(null);

        ProductoDTO resultado = productoBO.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);

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
        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

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
        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

        // Verificaciones
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificamos que el método del DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerTodosLosProductos();
    }

}
