package ExportadorPDF;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.NegocioException;
import Subsistemas.IGestorCompras;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author victo
 */
public class ExportadorPDFTest {

    private IGestorCompras mockGestorCompra;
    private ExportadorPDF exportador;

    @BeforeEach
    void configurar() {
        mockGestorCompra = mock(IGestorCompras.class);
        exportador = new ExportadorPDF(mockGestorCompra);
    }

    @Test
    void exportarListaCompraConProductos() throws NegocioException {
        CompraDTO compra = new CompraDTO();
        compra.setNombreCompra("Compra Mensual");

        ProductoDTO producto = new ProductoDTO();
        producto.setNombre("Manzanas");
        producto.setCategoria("Frutas");
        producto.setComprado(false);
        producto.setCompraDTO(compra);
        producto.setCantidad(5.0);

        List<ProductoDTO> productos = Arrays.asList(producto);
        compra.setProductos(productos);

        when(mockGestorCompra.obtenerCompraPorId(1L)).thenReturn(compra);

        String rutaPDF = exportador.exportarCompraAPDF(1L);
        assertEquals("Compra Mensual.pdf", rutaPDF);
    }
    
    @Test
    void exportarListaCompraInexistente() throws NegocioException {
        when(mockGestorCompra.obtenerCompraPorId(1L)).thenReturn(null);

        assertThrows(NegocioException.class, () -> {
            exportador.exportarCompraAPDF(1L);
        });
    }

    @Test
    void exportarListaCompraConProductosNull() throws NegocioException {
        CompraDTO compra = new CompraDTO();
        compra.setNombreCompra("Compra Test");
        compra.setProductos(null);

        when(mockGestorCompra.obtenerCompraPorId(1L)).thenReturn(compra);

        String rutaPDF = exportador.exportarCompraAPDF(1L);
        assertEquals("Compra Test_vacia.pdf", rutaPDF);
    }

    @Test
    void exportarListaCompraConProductosVacios() throws NegocioException {
        CompraDTO compra = new CompraDTO();
        compra.setNombreCompra("Compra Test");
        compra.setProductos(new ArrayList<>());

        when(mockGestorCompra.obtenerCompraPorId(1L)).thenReturn(compra);

        String rutaPDF = exportador.exportarCompraAPDF(1L);
        assertEquals("Compra Test_vacia.pdf", rutaPDF);
    }

}
