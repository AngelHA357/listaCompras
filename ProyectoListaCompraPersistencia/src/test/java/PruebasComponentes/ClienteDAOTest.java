/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasComponentes;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
public class ClienteDAOTest {
    private ClienteDAO clienteDAO;
    private IConexion conexion;
    
    public ClienteDAOTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true");
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba");
    }
    
    @BeforeEach
    public void setUp() throws PersistenciaException {
        conexion = Conexion.getInstance();
        clienteDAO = new ClienteDAO(conexion);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAgregarCliente() throws PersistenciaException{
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        
        Cliente resultado = clienteDAO.agregarCliente(cliente);
        
        
        assertNotNull(resultado.getId()); 
        assertEquals("Victor Humberto", resultado.getNombre());
        
    }
    
     @Test
    public void obtenerClienteExistente() throws PersistenciaException {
        // Crear un nuevo cliente
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

        // Agregar el cliente y obtener el resultado con su ID asignado
        Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);

        // Usar el ID del cliente recién agregado para obtenerlo de la base de datos
        Cliente resultado = clienteDAO.obtenerClientePorId(clienteAgregado.getId());

        // Verificar que el cliente obtenido tiene el mismo ID y los mismos datos
        assertNotNull(resultado); // Asegurarse de que el cliente no sea null
        assertEquals(clienteAgregado.getId(), resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("toribio", resultado.getUsuario());
        assertEquals("ABCD1234", resultado.getContrasenia());
    }
 
    @Test
    public void obtenerClienteInexistente() throws PersistenciaException {
        Cliente cliente = clienteDAO.obtenerClientePorId(Long.MAX_VALUE);
        assertNull(cliente);

    }
    
    @Test
    public void testObtenerTodosLosClientes() throws PersistenciaException {
        clienteDAO.agregarCliente(new Cliente("Cliente 1", "Apellido 1", "Apellido 1", "usuario1", "pass1"));
        clienteDAO.agregarCliente(new Cliente("Cliente 2", "Apellido 2", "Apellido 2", "usuario2", "pass2"));

        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        
        assertNotNull(clientes);
        assertTrue(clientes.size() >= 2);
    }
    
     @Test
    public void testObtenerClientePorUsuarioYContrasenaClienteExistente() throws PersistenciaException {
        // Datos de prueba
         String usuario = "wacho" + System.currentTimeMillis();
        String contrasenia = "ABCD1234";
        
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setContrasenia(contrasenia);
        
        clienteDAO.agregarCliente(cliente);

        // Obtener el cliente
        Cliente clienteObtenido = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);

        // Verificar el cliente
        assertNotNull(cliente);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals("ABCD1234", clienteObtenido.getContrasenia());
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_ClienteInexistente() throws PersistenciaException {
        // Datos de prueba
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseñaErronea";

        // Verificar que no se obtenga un cliente
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }
    
   
}
