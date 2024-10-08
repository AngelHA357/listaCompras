/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

import Conversiones.ProductosConversiones;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import Subsistemas.FiltroPorCategoria;
import Subsistemas.IFiltroPorCategoria;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * @author victo
 */
public class FIltroPorCategoriaTest {
    private IFiltroPorCategoria filtroProducto;
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
        
        
        filtroProducto = new FiltroPorCategoria(productoDAOMock, conversionesMock);
    }
    
    @AfterEach
    public void tearDown() {
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
        List<ProductoDTO> resultado = filtroProducto.filtrarPorCategoriaYCompraId("Categoria C", 1L);

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
        List<ProductoDTO> resultado = filtroProducto.filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);

        // Verificamos que el resultado sea una lista vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificamos que el DAO fue invocado correctamente
        verify(productoDAOMock, times(1)).filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);
    }
    
    
}
