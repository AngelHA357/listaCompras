/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import BOs.ClienteBO;
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

/**
 *
 * @author JoseH
 */
public class ClienteBOTest {

    IConexion conexion;
    private ClienteBO clienteBO;
    private ClientesConversiones conversiones;
    IClienteDAO clienteDAO;    

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
        conversiones = new ClientesConversiones();
        clienteBO = new ClienteBO();
        clienteDAO = new ClienteDAO(conexion);
    }
    
    @AfterEach
    public void tearDown() throws PersistenciaException {
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for(Cliente cliente : clientes){
            clienteDAO.eliminarCliente(cliente.getId());
        }
    }

    @Test
    public void testAgregarCliente() throws NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

        
        clienteBO.agregarCliente(clienteDTO);
        
        ClienteDTO clienteAgregado = clienteBO.encontrarClientePorUsuarioYContrasena(clienteDTO.getUsuario(), clienteDTO.getContrasenia());


        assertNotNull(clienteAgregado);  
        assertEquals("Victor Humberto", clienteAgregado.getNombre());
        assertEquals("Encinas", clienteAgregado.getApellidoPaterno());
        assertEquals("Guzmán", clienteAgregado.getApellidoMaterno());
        assertEquals("toribio", clienteAgregado.getUsuario());
        assertEquals("ABCD1234", clienteAgregado.getContrasenia());
    }
    
//    @Test
//    public void testAgregarCliente_UsuarioNulo() {
//        ClienteDTO clienteDTO = new ClienteDTO(null, "Encinas", "Guzmán", null, "ABCD1234");
//
//        assertThrows(NegocioException.class, () -> {
//            clienteBO.agregarCliente(clienteDTO);
//        });
//    }
    
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
        String usuario = "toribio" + System.currentTimeMillis();  // Para evitar duplicados
        String contrasenia = "ABCD1234";

        ClienteDTO clienteDTO = new ClienteDTO("Victor Humberto", "Encinas", "Guzmán", usuario, contrasenia);
        clienteBO.agregarCliente(clienteDTO);

        ClienteDTO clienteObtenido = clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);

        assertNotNull(clienteObtenido);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals(contrasenia, clienteObtenido.getContrasenia());
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_ClienteInexistente() {
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseniaErronea";

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });

        assertEquals("No se encontró al usuario", exception.getMessage());
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioNulo() {
        String usuario = null;
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    @Test
    public void testEncontrarClientePorUsuarioYContrasena_UsuarioVacio() {
        String usuario = "";
        String contrasenia = "ABCD1234";

        assertThrows(NegocioException.class, () -> {
            clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }
}
