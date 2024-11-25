/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import Conexion.Conexion;
import Conexion.IConexion;
import DTOs.ClienteDTO;
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

/**
 *
 * @author JoseH
 */
public class GestorClientesIntegrationTest {

    private IGestorClientes gestorClientes;
    private IConexion conexion;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true");
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba");
    }

    @BeforeEach
    public void setUp() {
        conexion = Conexion.getInstance();
        gestorClientes = new GestorClientes();
    }

    @AfterEach
    public void tearDown() {
        try {
            if (conexion != null) {
                conexion.rollback();
            }
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorClientesIntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    @Test
//    public void testAgregarCliente() throws PersistenciaException, NegocioException {
//        // Crear un cliente para prueba con todos los campos requeridos
//        ClienteDTO clienteDTO = new ClienteDTO(
//            "Victor Humberto", 
//            "Encinas", 
//            "Guzmán", 
//            "toribio_test", 
//            "ABCD1234"
//        );
//
//        // Agregar el cliente
//        ClienteDTO resultadoDTO = gestorClientes.agregarCliente(clienteDTO);
//
//        // Verificar que se agregó correctamente y que todos los campos están presentes
//        assertNotNull(resultadoDTO, "El cliente retornado no debe ser nulo");
//        assertNotNull(resultadoDTO.getId(), "El cliente debe tener un ID asignado");
//        assertEquals("Victor Humberto", resultadoDTO.getNombre());
//        assertEquals("Encinas", resultadoDTO.getApellidoPaterno());
//        assertEquals("Guzmán", resultadoDTO.getApellidoMaterno());
//        assertEquals("toribio_test", resultadoDTO.getUsuario());
//        assertEquals("ABCD1234", resultadoDTO.getContrasenia());
//
//        // Verificar que el cliente puede ser recuperado
//        ClienteDTO clienteRecuperado = gestorClientes.encontrarClientePorUsuarioYContrasena(
//            "toribio_test", 
//            "ABCD1234"
//        );
//        assertNotNull(clienteRecuperado, "El cliente debe poder ser recuperado después de agregarlo");
//        assertEquals(resultadoDTO.getId(), clienteRecuperado.getId());
//    }

    @Test
    public void testAgregarCliente_NombreNulo() {
        ClienteDTO clienteDTO = new ClienteDTO(
            null,  
            "Encinas", 
            "Guzmán", 
            "toribio_test", 
            "ABCD1234"
        );

        NegocioException thrown = assertThrows(
            NegocioException.class,
            () -> gestorClientes.agregarCliente(clienteDTO),
            "Debe lanzar NegocioException cuando el nombre es nulo"
        );
        assertTrue(thrown.getMessage().contains("nombre"));
    }

    @Test
    public void testAgregarCliente_UsuarioNulo() {
        ClienteDTO clienteDTO = new ClienteDTO(
            "Victor Humberto", 
            "Encinas", 
            "Guzmán", 
            null,  
            "ABCD1234"
        );

        NegocioException thrown = assertThrows(
            NegocioException.class,
            () -> gestorClientes.agregarCliente(clienteDTO),
            "Debe lanzar NegocioException cuando el usuario es nulo"
        );
        assertTrue(thrown.getMessage().contains("usuario"));
    }

//    @Test
//    public void testEncontrarClientePorUsuarioYContrasena_ClienteExistente() 
//            throws PersistenciaException, NegocioException {
//        ClienteDTO clienteDTO = new ClienteDTO(
//            "Victor Humberto", 
//            "Encinas", 
//            "Guzmán", 
//            "toribio_test", 
//            "ABCD1234"
//        );
//        ClienteDTO clienteAgregado = gestorClientes.agregarCliente(clienteDTO);
//        assertNotNull(clienteAgregado.getId(), "El cliente debe haberse creado con un ID");
//
//        // Intentar encontrar el cliente
//        ClienteDTO clienteObtenido = gestorClientes.encontrarClientePorUsuarioYContrasena(
//            "toribio_test", 
//            "ABCD1234"
//        );
//
//        // Verificar que se encontró el cliente correcto con todos sus datos
//        assertNotNull(clienteObtenido, "El cliente debe ser encontrado");
//        assertEquals(clienteAgregado.getId(), clienteObtenido.getId());
//        assertEquals("toribio_test", clienteObtenido.getUsuario());
//        assertEquals("Victor Humberto", clienteObtenido.getNombre());
//        assertEquals("Encinas", clienteObtenido.getApellidoPaterno());
//        assertEquals("Guzmán", clienteObtenido.getApellidoMaterno());
//    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteInexistente() {
        assertThrows(NegocioException.class,
            () -> gestorClientes.encontrarClientePorUsuarioYContrasena(
                "usuarioInexistente",
                "contraseniaErronea"
            ),
            "Debe lanzar NegocioException cuando el cliente no existe"
        );
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ContrasenaIncorrecta() 
            throws PersistenciaException, NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO(
            "Victor Humberto", 
            "Encinas", 
            "Guzmán", 
            "toribio_test", 
            "ABCD1234"
        );
        gestorClientes.agregarCliente(clienteDTO);

        // Intentar encontrar el cliente con contraseña incorrecta
        assertThrows(NegocioException.class,
            () -> gestorClientes.encontrarClientePorUsuarioYContrasena(
                "toribio_test", 
                "contraseniaIncorrecta"
            ),
            "Debe lanzar NegocioException cuando la contraseña es incorrecta"
        );
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioNulo() {
        assertThrows(NegocioException.class,
            () -> gestorClientes.encontrarClientePorUsuarioYContrasena(null, "ABCD1234"),
            "Debe lanzar NegocioException cuando el usuario es nulo"
        );
    }
}
