package Conversiones;

import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class ProductosConversionesTest {

    public ProductosConversionesTest() {
    }

    private ProductosConversiones conversiones;

    @Mock
    private ClientesConversiones clientesConversionesMock;

    @BeforeEach
    public void setUp() {
        clientesConversionesMock = mock(ClientesConversiones.class);
        conversiones = new ProductosConversiones(clientesConversionesMock);
    }

    @Test
    public void testDtoAEntidad_ConCompraYCliente() {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        CompraDTO compraDTO = new CompraDTO("Compra Test", clienteDTO);
        compraDTO.setId(1L);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Producto Test");
        productoDTO.setCategoria("Categoria Test");
        productoDTO.setComprado(false);
        productoDTO.setCantidad(10.0);
        productoDTO.setCompra(compraDTO);

        Cliente clienteEsperado = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        when(clientesConversionesMock.convertirDTOAEntidad(clienteDTO)).thenReturn(clienteEsperado);

        Producto producto = conversiones.dtoAEntidad(productoDTO);

        assertNotNull(producto);
        assertEquals(1L, producto.getId());
        assertEquals("Producto Test", producto.getNombre());
        assertNotNull(producto.getCompra());
        assertEquals(1L, producto.getCompra().getId());
        verify(clientesConversionesMock).convertirDTOAEntidad(clienteDTO);
    }

    @Test
    public void testDtoAEntidad_DTONulo() {
        assertNull(conversiones.dtoAEntidad(null));
    }

    @Test
    public void testDtoAEntidad_SinId() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Test");
        productoDTO.setCategoria("Categoria Test");
        productoDTO.setComprado(true);
        productoDTO.setCantidad(10.0);

        Producto producto = conversiones.dtoAEntidad(productoDTO);

        assertNotNull(producto);
        assertNull(producto.getId());
        assertEquals("Producto Test", producto.getNombre());
        assertEquals("Categoria Test", producto.getCategoria());
        assertTrue(producto.isComprado());
        assertEquals(10.0, producto.getCantidad());
    }

    @Test
    public void testEntidadADTO_DatosCompletos() {
        Cliente cliente = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Compra compra = new Compra("Compra Test", cliente);
        compra.setId(1L);

        List<Producto> productos = new ArrayList<>();
        Producto otroProducto = new Producto("Otro", "Categoria", true, compra, 20.0);
        otroProducto.setId(2L);
        productos.add(otroProducto);
        compra.setProductos(productos);

        Producto producto = new Producto("Producto", "Categoria", false, compra, 10.0);
        producto.setId(1L);

        ClienteDTO clienteDTOEsperado = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        when(clientesConversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTOEsperado);

        ProductoDTO dto = conversiones.entidadADTO(producto, true);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Producto", dto.getNombre());
        assertEquals("Categoria", dto.getCategoria());
        assertFalse(dto.isComprado());
        assertEquals(10.0, dto.getCantidad());
        assertNotNull(dto.getCompraDTO());
        assertEquals(1L, dto.getCompraDTO().getId());

        verify(clientesConversionesMock, times(2)).convertirEntidadADTO(cliente);
    }

    @Test
    public void testCompraEntidadADTO_DatosCompletos() {
        Cliente cliente = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Compra compra = new Compra("Compra Test", cliente);
        compra.setId(1L);

        List<Producto> productos = new ArrayList<>();
        Producto producto = new Producto("Producto", "Categoria", false, compra, 10.0);
        producto.setId(1L);
        productos.add(producto);
        compra.setProductos(productos);

        ClienteDTO clienteDTOEsperado = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        when(clientesConversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTOEsperado);

        CompraDTO dto = conversiones.compraEntidadADTO(compra, true);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Compra Test", dto.getNombreCompra());
        assertNotNull(dto.getProductos());
        assertEquals(1, dto.getProductos().size());

        verify(clientesConversionesMock, times(2)).convertirEntidadADTO(cliente);
    }

    @Test
    public void testEntidadADTO_EntidadNula() {
        assertNull(conversiones.entidadADTO(null, false));
    }

    @Test
    public void testCompraDtoAEntidad_DatosCompletos() {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        CompraDTO compraDTO = new CompraDTO("Compra Test", clienteDTO);
        compraDTO.setId(1L);

        List<ProductoDTO> productosDTO = new ArrayList<>();
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Producto Test");
        productosDTO.add(productoDTO);
        compraDTO.setProductos(productosDTO);

        Cliente clienteEsperado = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        when(clientesConversionesMock.convertirDTOAEntidad(clienteDTO)).thenReturn(clienteEsperado);

        Compra compra = conversiones.compraDtoAEntidad(compraDTO);

        assertNotNull(compra);
        assertEquals(1L, compra.getId());
        assertEquals("Compra Test", compra.getNombre());
        assertNotNull(compra.getProductos());
        assertEquals(1, compra.getProductos().size());
        verify(clientesConversionesMock).convertirDTOAEntidad(clienteDTO);
    }

    @Test
    public void testCompraDtoAEntidad_DTONulo() {
        assertNull(conversiones.compraDtoAEntidad(null));
    }

    @Test
    public void testCompraEntidadADTO_EntidadNula() {
        assertNull(conversiones.compraEntidadADTO(null, false));
    }
}
