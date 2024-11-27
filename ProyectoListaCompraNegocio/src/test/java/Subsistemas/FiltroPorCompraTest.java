package Subsistemas;

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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
public class FiltroPorCompraTest {

    public FiltroPorCompraTest() {
    }

    private IFiltroPorCompra filtroCompra;
    private ICompraDAO compraDAOMock;
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
        compraDAOMock = mock(ICompraDAO.class);
        productoDAOMock = mock(ProductoDAO.class);
        conversionesMock = mock(ProductosConversiones.class);

        filtroCompra = new FiltroPorCompra(productoDAOMock, conversionesMock, compraDAOMock);
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Se verifica que el método obtenerProductosFiltrarPorCompra retorne los
     * productos filtrados correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testFiltrarPorCompra() throws PersistenciaException {
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

            ProductoDTO productoDTO1 = new ProductoDTO("Producto C", "Categoria D", false, compraDTO, 25.0);
            ProductoDTO productoDTO2 = new ProductoDTO("Producto D", "Categoria D", false, compraDTO, 30.0);
            Producto producto1 = new Producto("Producto C", "Categoria D", false, compra, 25.0);
            Producto producto2 = new Producto("Producto D", "Categoria D", false, compra, 30.0);

            when(conversionesMock.dtoAEntidad(productoDTO1)).thenReturn(producto1);
            when(conversionesMock.dtoAEntidad(productoDTO2)).thenReturn(producto2);
            when(productoDAOMock.obtenerProductosPorCompraId(anyLong())).thenReturn(Arrays.asList(producto1, producto2));

            List<ProductoDTO> resultado = filtroCompra.obtenerProductosFiltrarPorCompra(1L);

            assertNotNull(resultado);
            assertEquals(2, resultado.size());

            verify(productoDAOMock, times(1)).obtenerProductosPorCompraId(1L);
        } catch (NegocioException ex) {
            Logger.getLogger(FiltroPorCompraTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se verifica que el método obtenerProductosFiltrarPorCompra retorne una
     * lista vacía para una compra inexistente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si ocurre un error de negocio.
     */
    @Test
    public void testFiltrarPorCompraInexistente() throws PersistenciaException, NegocioException {
        Long compraIdInexistente = 999L;

        when(compraDAOMock.obtenerCompraPorId(compraIdInexistente)).thenReturn(null);

        NegocioException thrown = assertThrows(
                NegocioException.class,
                () -> filtroCompra.obtenerProductosFiltrarPorCompra(compraIdInexistente),
                "Debe lanzar NegocioException cuando la compra no existe"
        );

        assertEquals("No existe una compra con el ID proporcionado: 999", thrown.getMessage());

        verify(productoDAOMock, never()).obtenerProductosPorCompraId(compraIdInexistente);
    }
}
