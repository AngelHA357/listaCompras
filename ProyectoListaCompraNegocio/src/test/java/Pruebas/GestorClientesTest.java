/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

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

    @Test
    public void testAgregarCliente() throws PersistenciaException {
        try {
            // Creamos un ClienteDTO de prueba
            ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

            // Simulamos la conversión de ClienteDTO a Cliente usando el mock de ClientesConversiones
            Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
            when(conversionesMock.convertirDTOAEntidad(any(ClienteDTO.class))).thenReturn(cliente);

            when(conversionesMock.convertirEntidadADTO(any(Cliente.class))).thenReturn(clienteDTO);

            // Simulamos que el DAO retorna un Cliente al agregar
            when(clienteDAOMock.agregarCliente(any(Cliente.class))).thenReturn(cliente);

            // Llamamos al método bajo prueba
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

    @Test
    public void testAgregarCliente_UsuarioNulo() {
        ClienteDTO clienteDTO = new ClienteDTO(null, "Encinas", "Guzmán", null, "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testAgregarCliente_UsuarioVacio() {
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "", "ABCD1234");

        assertThrows(NegocioException.class, () -> {
            gestorClientes.agregarCliente(clienteDTO);
        });
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteExistente() throws PersistenciaException, NegocioException {
        String usuario = "toribio";
        String contrasenia = "ABCD1234";

        // Creamos un ClienteDTO y Cliente de prueba
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);

        // Simulamos que el DAO encuentra el cliente por usuario y contraseña
        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(cliente);

        // Simulamos la conversión de Cliente a ClienteDTO
        when(conversionesMock.convertirEntidadADTO(cliente)).thenReturn(clienteDTO);

        // Llamamos al método bajo prueba
        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        // Verificamos que se encontró el cliente correcto y que la conversión se realizó
        assertNotNull(clienteObtenido);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals(contrasenia, clienteObtenido.getContrasenia());

        // Verificamos que los mocks fueron invocados correctamente
        verify(clienteDAOMock, times(1)).obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        verify(conversionesMock, times(1)).convertirEntidadADTO(cliente);
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteInexistente() throws PersistenciaException, NegocioException {
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseniaErronea";

        // Simulamos que el DAO no encuentra ningún cliente
        when(clienteDAOMock.obtenerClientePorUsuarioYContrasena(usuario, contrasenia)).thenReturn(null);

        // Llamamos al método bajo prueba
        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        // Verificamos que el método retorne null cuando no se encuentra el cliente
        assertNull(clienteObtenido);

        // Verificamos que el mock del DAO fue invocado correctamente
        verify(clienteDAOMock, times(1)).obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioNulo() {
        String usuario = null;
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioVacio() {
        String usuario = "";
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            gestorClientes.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }
}
