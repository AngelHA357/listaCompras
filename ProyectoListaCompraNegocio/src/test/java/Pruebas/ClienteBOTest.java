/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import Subsistemas.ClienteBO;
import Subsistemas.IClienteBO;
import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ClientesConversiones;
import DAOs.ClienteDAO;
import DAOs.IClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author JoseH
 */
public class ClienteBOTest {

    private IClienteDAO clienteDAOMock;  
    private IClienteBO clienteBO;
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
        
        clienteBO = new ClienteBO(clienteDAOMock, conversionesMock);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAgregarCliente() throws PersistenciaException {
        // Creamos un ClienteDTO de prueba
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

        // Simulamos la conversión de ClienteDTO a Cliente usando el mock de ClientesConversiones
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        when(conversionesMock.convertirDTOAEntidad(any(ClienteDTO.class))).thenReturn(cliente);

        when(conversionesMock.convertirEntidadADTO(any(Cliente.class))).thenReturn(clienteDTO);

        // Simulamos que el DAO retorna un Cliente al agregar
        when(clienteDAOMock.agregarCliente(any(Cliente.class))).thenReturn(cliente);

        // Llamamos al método bajo prueba
        ClienteDTO resultadoDTO = clienteBO.agregarCliente(clienteDTO);

        // Verificamos que la conversión fue llamada correctamente
        verify(conversionesMock, times(1)).convertirDTOAEntidad(clienteDTO);
        verify(conversionesMock, times(1)).convertirEntidadADTO(cliente);
        // Verificamos que el método agregarCliente del DAO fue invocado correctamente
        verify(clienteDAOMock, times(1)).agregarCliente(cliente);

        // Verificamos que el ClienteDTO devuelto es correcto
        assertNotNull(resultadoDTO);
        assertEquals("Victor Humberto", resultadoDTO.getNombre());
    }
    
//    @Test
//    public void testAgregarCliente_UsuarioNulo() {
//        ClienteDTO clienteDTO = new ClienteDTO(null, "Encinas", "Guzmán", null, "ABCD1234");
//
//        assertThrows(NegocioException.class, () -> {
//            clienteBO.agregarCliente(clienteDTO);
//        });
//    }
//    
//    @Test
//    public void testAgregarCliente_UsuarioVacio() {
//        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "", "ABCD1234");
//
//        assertThrows(NegocioException.class, () -> {
//            clienteBO.agregarCliente(clienteDTO);
//        });
//    }

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
        ClienteDTO clienteObtenido = clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

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
        ClienteDTO clienteObtenido = clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        // Verificamos que el método retorne null cuando no se encuentra el cliente
        assertNull(clienteObtenido);

        // Verificamos que el mock del DAO fue invocado correctamente
        verify(clienteDAOMock, times(1)).obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
    }


//    @Test
//    public void testEncontrarClientePorUsuarioYContrasena_UsuarioNulo() {
//        String usuario = null;
//        String contrasenia = "ABCD1234";
//
//        assertThrows(NegocioException.class, () -> {
//            clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
//        });
//    }
//
//    @Test
//    public void testEncontrarClientePorUsuarioYContrasena_UsuarioVacio() {
//        String usuario = "";
//        String contrasenia = "ABCD1234";
//
//        assertThrows(NegocioException.class, () -> {
//            clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
//        });
//    }
}
