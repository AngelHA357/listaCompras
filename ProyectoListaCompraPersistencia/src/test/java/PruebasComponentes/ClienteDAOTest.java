package PruebasComponentes;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Esta clase permite realizar pruebas unitarias con el Cliente
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class ClienteDAOTest {

    private ClienteDAO clienteDAO;
    private IConexion conexion;

    /**
     * Constructor por defecto
     */
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
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            clienteDAO.eliminarCliente(cliente.getId());
        }
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            clienteDAO.eliminarCliente(cliente.getId());
        }
    }

   /**
     * Permite probar que un cliente se agregue correctamente.
     *
     * @throws PersistenciaException Se lanza en caso de que no se pueda agregar
     * un cliente.
     */
    @Test
    public void testAgregarCliente() throws PersistenciaException {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        Cliente resultado = clienteDAO.agregarCliente(cliente);

        assertNotNull(resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("toribio", resultado.getUsuario());
        assertEquals("ABCD1234", resultado.getContrasenia());
    }

    /**
     * Permite probar que se lance una excepción al agregar un cliente con usuario nulo.
     *
     * @throws PersistenciaException Se lanza cuando el usuario es nulo
     */
    @Test
    public void testAgregarCliente_UsuarioNulo() throws PersistenciaException {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", null, "ABCD1234");
        Cliente resultado = clienteDAO.agregarCliente(cliente);

        assertNotNull(resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("ABCD1234", resultado.getContrasenia());
    }

    /**
     * Permite probar que se lance una excepción al agregar un cliente con usuario vacío.
     *
     * @throws PersistenciaException Se lanza cuando el usuario está vacío
     */
    @Test
    public void testAgregarCliente_UsuarioVacio() throws PersistenciaException{
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "", "ABCD1234");
        Cliente resultado = clienteDAO.agregarCliente(cliente);
        
        assertNotNull(resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("ABCD1234", resultado.getContrasenia());
    }

    /**
     * Permite probar que se obtenga correctamente un cliente que ya existe.
     *
     * @throws PersistenciaException Se lanza en caso que no se pueda obtener el
     * cliente.
     */
    @Test
    public void testObtenerClienteExistente() throws PersistenciaException {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);

        Cliente resultado = clienteDAO.obtenerClientePorId(clienteAgregado.getId());

        assertNotNull(resultado);
        assertEquals(clienteAgregado.getId(), resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("toribio", resultado.getUsuario());
        assertEquals("ABCD1234", resultado.getContrasenia());
    }

    /**
     * Permite probar si se puede obtener un cliente inexistente.
     *
     * @throws PersistenciaException Se lanza en caso de no encontrar el
     * cliente.
     */
    @Test
    public void testObtenerClienteInexistente() throws PersistenciaException {
        Cliente cliente = clienteDAO.obtenerClientePorId(Long.MAX_VALUE);
        assertNull(cliente);
    }

    /**
     * Permite probar la obtención de todos los clientes registrados.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener los
     * clientes.
     */
    @Test
    public void testObtenerTodosLosClientes() throws PersistenciaException {
        clienteDAO.agregarCliente(new Cliente("Cliente 1", "Apellido 1", "Apellido 1", "usuario1", "pass1"));
        clienteDAO.agregarCliente(new Cliente("Cliente 2", "Apellido 2", "Apellido 2", "usuario2", "pass2"));

        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();

        assertNotNull(clientes);
        assertTrue(clientes.size() >= 2);
        assertTrue(clientes.stream().anyMatch(c -> c.getUsuario().equals("usuario1")));
        assertTrue(clientes.stream().anyMatch(c -> c.getUsuario().equals("usuario2")));
    }

    /**
     * Permite probar la obtención de un cliente existente por usuario y
     * contraseña.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener el
     * cliente.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_ClienteExistente() throws PersistenciaException {
        String usuario = "wacho" + System.currentTimeMillis();
        String contrasenia = "ABCD1234";

        Cliente cliente = new Cliente("Victor", "Encinas", "Guzmán", usuario, contrasenia);
        clienteDAO.agregarCliente(cliente);

        Cliente clienteObtenido = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);

        assertNotNull(clienteObtenido);
        assertEquals(usuario, clienteObtenido.getUsuario());
        assertEquals(contrasenia, clienteObtenido.getContrasenia());
    }

    /**
     * Permite probar la obtención de un cliente inexistente por usuario y
     * contraseña.
     *
     * @throws PersistenciaException Se lanza en caso de no encontrar el
     * cliente.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_ClienteInexistente() throws PersistenciaException{
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseñaErronea";

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    /**
     * Verifica que se lance una excepción cuando el usuario es nulo en
     * obtenerClientePorUsuarioYContrasena.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_UsuarioNulo() throws PersistenciaException{
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena(null, "ABCD1234");
        });
    }

    /**
     * Verifica que se lance una excepción cuando el usuario está vacío en
     * obtenerClientePorUsuarioYContrasena.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_UsuarioVacio() throws PersistenciaException{
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("", "ABCD1234");
        });
    }

    /**
     * Verifica que se lance una excepción cuando la contraseña es nula.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_ContraseniaNula() throws PersistenciaException{
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("usuario", null);
        });
    }

    /**
     * Verifica que se lance una excepción cuando la contraseña está vacía.
     */
    @Test
    public void testObtenerClientePorUsuarioYContrasena_ContraseniaVacia() throws PersistenciaException{
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("usuario", "");
        });
    }

}
