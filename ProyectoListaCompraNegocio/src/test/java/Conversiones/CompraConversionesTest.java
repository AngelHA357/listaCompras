package Conversiones;

import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class CompraConversionesTest {

    public CompraConversionesTest() {
    }

    private CompraConversiones conversiones;

    @Mock
    private ProductosConversiones productosConversionesMock;

    @Mock
    private ClientesConversiones clientesConversionesMock;

    @BeforeEach
    public void setUp() {
        productosConversionesMock = mock(ProductosConversiones.class);
        clientesConversionesMock = mock(ClientesConversiones.class);

        conversiones = new CompraConversiones(productosConversionesMock, clientesConversionesMock);
    }

    @Test
    public void testDtoAEntidad_DatosCompletos() {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        CompraDTO compraDTO = new CompraDTO("Compra Test", clienteDTO);
        compraDTO.setId(1L);

        ProductoDTO productoDTO = new ProductoDTO("Producto", "Categoria", false, compraDTO, 10.0);

        Cliente clienteEsperado = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Producto productoEsperado = new Producto("Producto", "Categoria", false, null, 10.0);

        when(clientesConversionesMock.convertirDTOAEntidad(clienteDTO)).thenReturn(clienteEsperado);
        when(productosConversionesMock.dtoAEntidad(productoDTO)).thenReturn(productoEsperado);

        Compra compra = conversiones.dtoAEntidad(compraDTO);

        assertNotNull(compra);
        assertEquals(1L, compra.getId());
        assertEquals("Compra Test", compra.getNombre());
        verify(clientesConversionesMock).convertirDTOAEntidad(clienteDTO);
    }

    @Test
    public void testEntidadADTO_DatosCompletos() {
        Cliente cliente = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Compra compra = new Compra("Compra Test", cliente);
        compra.setId(1L);

        Producto producto = new Producto("Producto", "Categoria", false, compra, 10.0);
        compra.setProductos(Arrays.asList(producto));

        ClienteDTO clienteDTOEsperado = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        ProductoDTO productoDTOEsperado = new ProductoDTO("Producto", "Categoria", false, null, 10.0);

        when(clientesConversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTOEsperado);
        when(productosConversionesMock.entidadADTO(eq(producto), anyBoolean())).thenReturn(productoDTOEsperado);

        CompraDTO dto = conversiones.entidadADTO(compra);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Compra Test", dto.getNombreCompra());
        verify(clientesConversionesMock).convertirEntidadADTO(cliente);
        verify(productosConversionesMock).entidadADTO(eq(producto), anyBoolean());
    }

    @Test
    public void testDtoAEntidad_DTONulo() {
        assertNull(conversiones.dtoAEntidad(null));
    }

    @Test
    public void testDtoAEntidad_SinId() {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        CompraDTO compraDTO = new CompraDTO("Compra Test", clienteDTO);
        Cliente clienteEsperado = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");

        when(clientesConversionesMock.convertirDTOAEntidad(clienteDTO)).thenReturn(clienteEsperado);

        Compra compra = conversiones.dtoAEntidad(compraDTO);

        assertNotNull(compra);
        assertNull(compra.getId());
        assertEquals("Compra Test", compra.getNombre());
    }

    @Test
    public void testDtoAEntidad_ConProductos() {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        CompraDTO compraDTO = new CompraDTO("Compra Test", clienteDTO);
        List<ProductoDTO> productosDTO = new ArrayList<>();
        ProductoDTO productoDTO = new ProductoDTO("Producto", "Categoria", false, compraDTO, 10.0);
        productosDTO.add(productoDTO);
        compraDTO.setProductos(productosDTO);

        Cliente clienteEsperado = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Producto productoEsperado = new Producto("Producto", "Categoria", false, null, 10.0);

        when(clientesConversionesMock.convertirDTOAEntidad(clienteDTO)).thenReturn(clienteEsperado);
        when(productosConversionesMock.dtoAEntidad(productoDTO)).thenReturn(productoEsperado);

        Compra compra = conversiones.dtoAEntidad(compraDTO);

        assertNotNull(compra);
        assertNotNull(compra.getProductos());
        assertFalse(compra.getProductos().isEmpty());
        verify(productosConversionesMock).dtoAEntidad(productoDTO);
    }

    @Test
    public void testEntidadADTO_EntidadNula() {
        assertNull(conversiones.entidadADTO(null));
    }

    @Test
    public void testEntidadADTO_SinProductos() {
        Cliente cliente = new Cliente("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        Compra compra = new Compra("Compra Test", cliente);
        compra.setId(1L);
        compra.setProductos(new ArrayList<>());

        ClienteDTO clienteDTOEsperado = new ClienteDTO("Nombre", "ApellidoP", "ApellidoM", "usuario", "pass");
        when(clientesConversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTOEsperado);

        CompraDTO dto = conversiones.entidadADTO(compra);

        assertNotNull(dto);
        assertNotNull(dto.getProductos());
        assertTrue(dto.getProductos().isEmpty());
        verifyNoInteractions(productosConversionesMock);
    }
}
