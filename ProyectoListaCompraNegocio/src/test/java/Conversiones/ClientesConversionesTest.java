package Conversiones;

import DTOs.ClienteDTO;
import Entidades.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class ClientesConversionesTest {

    public ClientesConversionesTest() {
    }
    
    private ClientesConversiones conversiones;

    @BeforeEach
    public void setUp() {
        conversiones = new ClientesConversiones();
    }

    @Test
    public void testConvertirDTOAEntidad_DatosCompletos() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "Encinas", "Guzmán", "toribio", "ABCD1234");
        clienteDTO.setId(1L);

        Cliente cliente = conversiones.convertirDTOAEntidad(clienteDTO);

        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("Victor", cliente.getNombre());
        assertEquals("Encinas", cliente.getApellidoPaterno());
        assertEquals("Guzmán", cliente.getApellidoMaterno());
        assertEquals("toribio", cliente.getUsuario());
        assertEquals("ABCD1234", cliente.getContrasenia());
    }

    @Test
    public void testConvertirEntidadADTO_DatosCompletos() {
        Cliente cliente = new Cliente("Victor", "Encinas", "Guzmán", "toribio", "ABCD1234");
        cliente.setId(1L);

        ClienteDTO dto = conversiones.convertirEntidadADTO(cliente);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Victor", dto.getNombre());
        assertEquals("Encinas", dto.getApellidoPaterno());
        assertEquals("Guzmán", dto.getApellidoMaterno());
        assertEquals("toribio", dto.getUsuario());
        assertEquals("ABCD1234", dto.getContrasenia());
    }

    @Test
    public void testConvertirDTOAEntidad_DTONulo() {
        Cliente cliente = conversiones.convertirDTOAEntidad(null);
        assertNull(cliente);
    }

    @Test
    public void testConvertirEntidadADTO_EntidadNula() {
        ClienteDTO dto = conversiones.convertirEntidadADTO(null);
        assertNull(dto);
    }
}
