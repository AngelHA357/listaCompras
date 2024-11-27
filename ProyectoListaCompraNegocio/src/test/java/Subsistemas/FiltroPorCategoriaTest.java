
package Subsistemas;

import Conversiones.ProductosConversiones;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.PersistenciaException;
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
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class FiltroPorCategoriaTest {
    
    public FiltroPorCategoriaTest() {
    }
    
    private IFiltroPorCategoria filtroProducto;
    private IProductoDAO productoDAOMock;
    private ProductosConversiones conversionesMock;

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
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

    /**
     * Se verifica que el método filtrarPorCategoriaYCompraId retorne los
     * productos filtrados correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testFiltrarPorCategoriaYCompraId() throws PersistenciaException {
        CompraDTO compraDTO = new CompraDTO("Compra 1", null);
        ProductoDTO productoDTO = new ProductoDTO("Producto A", "Categoria C", false, compraDTO, 15.0);
        Producto producto = new Producto("Producto A", "Categoria C", false, null, 15.0);

        when(productoDAOMock.filtrarPorCategoriaYCompraId(anyString(), anyLong())).thenReturn(Arrays.asList(producto));
        when(conversionesMock.entidadADTO(any(Producto.class), anyBoolean())).thenReturn(productoDTO);

        List<ProductoDTO> resultado = filtroProducto.filtrarPorCategoriaYCompraId("Categoria C", 1L);

        verify(productoDAOMock, times(1)).filtrarPorCategoriaYCompraId("Categoria C", 1L);
        verify(conversionesMock, times(1)).entidadADTO(producto, false);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto A", resultado.get(0).getNombre());
    }

    /**
     * Se verifica que el método filtrarPorCategoriaYCompraId retorne una lista
     * vacía cuando no hay resultados.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testFiltrarPorCategoriaYCompraId_SinResultados() throws PersistenciaException {
        when(productoDAOMock.filtrarPorCategoriaYCompraId(anyString(), anyLong())).thenReturn(Collections.emptyList());

        List<ProductoDTO> resultado = filtroProducto.filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(productoDAOMock, times(1)).filtrarPorCategoriaYCompraId("Categoria Inexistente", 9999L);
    }

}
