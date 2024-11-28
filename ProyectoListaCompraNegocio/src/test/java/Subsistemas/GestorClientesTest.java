package Subsistemas;

import Conversiones.ClientesConversiones;
import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorClientesTest {

    public GestorClientesTest() {
    }
    private IClienteDAO clienteDAOMock;
    private IGestorClientes gestorClientes;
    private ClientesConversiones conversionesMock;

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        clienteDAOMock = mock(ClienteDAO.class);
        conversionesMock = mock(ClientesConversiones.class);

        gestorClientes = new GestorClientes(clienteDAOMock, conversionesMock);
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Se verifica que el método agregarCliente agregue un cliente
     * correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testAgregarCliente() throws PersistenciaException {
        try {
            ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

            Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
            when(conversionesMock.convertirDTOAEntidad(any(ClienteDTO.class))).thenReturn(cliente);
            when(conversionesMock.convertirEntidadADTO(any(Cliente.class))).thenReturn(clienteDTO);

            when(clienteDAOMock.agregarCliente(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO resultadoDTO = gestorClientes.agregarCliente(clienteDTO);

            verify(conversionesMock, times(1)).convertirDTOAEntidad(clienteDTO);
            verify(conversionesMock, times(1)).convertirEntidadADTO(cliente);
            verify(clienteDAOMock, times(1)).agregarCliente(cliente);

            assertNotNull(resultadoDTO);
            assertEquals("Victor Humberto", resultadoDTO.getNombre());
        } catch (NegocioException ex) {
            Logger.getLogger(GestorClientesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se verifica que se lance una excepción cuando el usuario es nulo.
     */
    @Test
    public void testAgregarCliente_UsuarioNulo() {
        ClienteDTO clienteDTO = new ClienteDTO(null, "Encinas", "Guzmán", null, "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    /**
     * Se verifica que se lance una excepción cuando el usuario está vacío.
     */
    @Test
    public void testAgregarCliente_UsuarioVacio() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "", "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    /**
     * Se verifica que el método encontrarClientePorUsuarioYContrasena retorne
     * el cliente correcto si existe.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si ocurre un error de negocio.
     */
    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteExistente() throws PersistenciaException, NegocioException {
        String usuario = "toribio";
        String contrasenia = "ABCD1234";

        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);

        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(cliente);
        when(conversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        assertNotNull(clienteObtenido);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals(contrasenia, clienteObtenido.getContrasenia());

        verify(clienteDAOMock, times(1)).obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        verify(conversionesMock, times(1)).convertirEntidadADTO(cliente);
    }

    /**
     * Se verifica que el método encontrarClientePorUsuarioYContrasena retorne
     * null si el cliente no existe.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si ocurre un error de negocio.
     */
    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteInexistente() throws PersistenciaException, NegocioException {
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseniaErronea";

        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(null);

        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        assertNull(clienteObtenido);
        verify(clienteDAOMock, times(1)).obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
    }

    /**
     * Se verifica que se lance una excepción cuando el usuario es nulo en
     * encontrarClientePorUsuarioYContrasena.
     */
    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioNulo() {
        String usuario = null;
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    /**
     * Se verifica que se lance una excepción cuando el usuario está vacío en
     * encontrarClientePorUsuarioYContrasena.
     */
    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioVacio() {
        String usuario = "";
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    @Test
    public void testAgregarCliente_NombreVacio() {
        ClienteDTO clienteDTO = new ClienteDTO("", "Encinas", "Guzmán", "toribio", "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testAgregarCliente_ApellidoPaternoVacio() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "", "Guzmán", "toribio", "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testAgregarCliente_ApellidoMaternoVacio() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "Encinas", "", "toribio", "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testAgregarCliente_ContraseniaVacia() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "Encinas", "Guzmán", "toribio", "");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testAgregarCliente_UsuarioExistente() throws PersistenciaException {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "Encinas", "Guzmán", "toribio", "ABCD1234");
        List<Cliente> clientesExistentes = new ArrayList<>();
        clientesExistentes.add(new Cliente("Otro", "Usuario", "Existente", "toribio", "otra123"));

        when(clienteDAOMock.obtenerTodosLosClientes()).thenReturn(clientesExistentes);

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });

        verify(clienteDAOMock, times(1)).obtenerTodosLosClientes();
    }

    @Test
    public void testAgregarCliente_ErrorPersistencia() throws PersistenciaException, NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO("Victor", "Encinas", "Guzmán", "toribio", "ABCD1234");
        Cliente cliente = new Cliente("Victor", "Encinas", "Guzmán", "toribio", "ABCD1234");

        when(clienteDAOMock.obtenerTodosLosClientes()).thenThrow(new PersistenciaException("Error de conexión"));
        when(conversionesMock.convertirDTOAEntidad(any(ClienteDTO.class))).thenReturn(cliente);

        ClienteDTO resultado = gestorClientes.agregarCliente(clienteDTO);
        assertNull(resultado);

        verify(clienteDAOMock, times(1)).obtenerTodosLosClientes();
    }
    
    
}
