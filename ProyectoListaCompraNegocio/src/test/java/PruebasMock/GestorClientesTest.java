/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

import Conversiones.ClientesConversiones;
import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorClientes;
import Subsistemas.IGestorClientes;
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
 * @author JoseH
 */
public class GestorClientesTest {

    private IClienteDAO clienteDAOMock;
    private IGestorClientes gestorClientes;
    ClientesConversiones conversionesMock;

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
            // Se crea un ClienteDTO de prueba
            ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

            // Se simula la conversión de ClienteDTO a Cliente
            Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
            when(conversionesMock.convertirDTOAEntidad(any(ClienteDTO.class))).thenReturn(cliente);
            when(conversionesMock.convertirEntidadADTO(any(Cliente.class))).thenReturn(clienteDTO);

            // Se simula que el DAO retorna un Cliente al agregar
            when(clienteDAOMock.agregarCliente(any(Cliente.class))).thenReturn(cliente);

            // Se llama al método bajo prueba
            ClienteDTO resultadoDTO = gestorClientes.agregarCliente(clienteDTO);

            // Se verifican las interacciones con los mocks
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

        // Se crean un ClienteDTO y un Cliente de prueba
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);

        // Se simula que el DAO encuentra el cliente por usuario y contraseña
        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(cliente);
        when(conversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTO);

        // Se llama al método bajo prueba
        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        // Se verifica que se encontró el cliente correcto y que la conversión se realizó
        assertNotNull(clienteObtenido);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals(contrasenia, clienteObtenido.getContrasenia());

        // Se verifican las interacciones con los mocks
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

        // Se simula que el DAO no encuentra ningún cliente
        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(null);

        // Se llama al método bajo prueba
        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        // Se verifica que el método retorne null cuando no se encuentra el cliente
        assertNull(clienteObtenido);

        // Se verifica que el mock del DAO fue invocado correctamente
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
}
