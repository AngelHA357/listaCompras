package PruebasComponentes;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import DAOs.CompraDAO;
import DAOs.IClienteDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Esta clase permite realizar pruebas unitarias con la Compra
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class CompraDAOTest {

    private ICompraDAO compraDAO;
    private IClienteDAO clienteDAO;
    private IProductoDAO productoDAO;
    IConexion conexion;
    private static Long clienteIdCounter = 1000L;

    /**
     * Constructor por defecto.
     */
    public CompraDAOTest() {
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
        compraDAO = new CompraDAO(conexion);
        clienteDAO = new ClienteDAO(conexion);
        productoDAO = new ProductoDAO(conexion);
        limpiarBaseDeDatos();
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }

    private void limpiarBaseDeDatos() throws PersistenciaException {
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        if (!productos.isEmpty()) {
            for (Producto producto : productos) {
                productoDAO.eliminarProducto(producto.getId());
            }
        }

        
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        if (!compras.isEmpty()) {
            for (Compra compra : compras) {
                compraDAO.eliminarCompra(compra.getId());
            }
        }
        
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        if (!clientes.isEmpty()) {
            for (Cliente cliente : clientes) {
                clienteDAO.eliminarCliente(cliente.getId());
            }

        }
    }

    /**
     * Se verifica que el método agregarCompra agregue una compra correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testAgregarCompra() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra de Prueba", cliente);
        Compra resultado = compraDAO.agregarCompra(compra);

        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombre());
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getCliente());
    }

    /**
     * Se verifica que se lance una excepción cuando el nombre de la compra es
     * nulo.
     */
    @Test
    public void testAgregarCompra_NombreNulo() {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        Compra compra = new Compra(null, cliente);

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.agregarCompra(compra);
        }, "Error al agregar compra");
    }

    /**
     * Se verifica que se lance una excepción cuando el nombre de la compra está
     * vacío.
     */
    @Test
    public void testAgregarCompra_NombreVacio() {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        Compra compra = new Compra("", cliente);

        assertThrows(PersistenciaException.class, () -> {
            compraDAO.agregarCompra(compra);
        }, "Error al agregar compra");
    }

    /**
     * Se verifica que el método obtenerCompraPorId retorne la compra correcta
     * si existe.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra de Prueba", cliente);
        compra = compraDAO.agregarCompra(compra);

        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());

        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombre());
    }

    /**
     * Se verifica que se retorne null al intentar obtener una compra
     * inexistente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorId_Inexistente() throws PersistenciaException {
        Compra resultado = compraDAO.obtenerCompraPorId(9999L);
        assertNull(resultado);
    }

    /**
     * Se verifica que se obtengan todas las compras correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        compraDAO.agregarCompra(new Compra("Compra 1", cliente));
        compraDAO.agregarCompra(new Compra("Compra 2", cliente));

        List<Compra> compras = compraDAO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertEquals(2, compras.size());
    }

    /**
     * Se verifica que se retorne una lista vacía cuando no hay compras.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerTodasLasCompras_Vacio() throws PersistenciaException {
        List<Compra> resultado = compraDAO.obtenerTodasLasCompras();
        assertTrue(resultado.isEmpty());
    }

    /**
     * Se verifica que se elimine una compra correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testEliminarCompra() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra de Prueba", cliente);
        compra = compraDAO.agregarCompra(compra);

        compraDAO.eliminarCompra(compra.getId());
        Compra eliminada = compraDAO.obtenerCompraPorId(compra.getId());

        assertNull(eliminada);
    }

    /**
     * Se verifica que no se lance excepción al intentar eliminar una compra
     * inexistente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testEliminarCompra_Inexistente() throws PersistenciaException {
        compraDAO.eliminarCompra(9999L); // No debería lanzar excepción
    }

    /**
     * Se verifica que se obtenga una compra existente por su nombre y cliente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_Existente() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Test", cliente);
        compraDAO.agregarCompra(compra);

        Compra resultado = compraDAO.obtenerCompraPorNombreYCliente("Compra Test", cliente.getId());

        assertNotNull(resultado);
        assertEquals("Compra Test", resultado.getNombre());
    }

    /**
     * Se verifica que se retorne null cuando no se encuentra la compra por
     * nombre y cliente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_NoExiste() throws PersistenciaException {
        Cliente cliente = new Cliente("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra resultado = compraDAO.obtenerCompraPorNombreYCliente("Compra Inexistente", cliente.getId());
        assertNull(resultado);
    }
}
