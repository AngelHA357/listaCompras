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

    ICompraDAO compraDAO;
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
    public void setUp() {
        conexion = Conexion.getInstance();
        compraDAO = new CompraDAO(conexion);
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }

    /**
     * Permite borrar los datos agregados en la base de datos.
     *
     * @throws PersistenciaException Se lanza en caso de que faller alguna
     * conexión.
     */
    private void limpiarBaseDeDatos() throws PersistenciaException {
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }

        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            clienteDAO.eliminarCliente(cliente.getId());
        }

        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }

    }

    /**
     * Permite probar la adición de una nueva compra.
     *
     * @throws PersistenciaException Se lanza en caso de error al agregar la
     * compra.
     */
    @Test
    public void agregarCompra() throws PersistenciaException {
        Compra compra = new Compra("Cosas para el GYM", null);

        Compra resultado = compraDAO.agregarCompra(compra);

        assertNotNull(resultado.getId());
        assertEquals("Cosas para el GYM", resultado.getNombre());
    }

    /**
     * Permite probar la eliminación de una compra existente.
     *
     * @throws PersistenciaException Se lanza en caso de error al eliminar la
     * compra.
     */
    @Test
    public void eliminarCompra() throws PersistenciaException {
        Compra compra = new Compra("EjemploCompra", null);
        compraDAO.agregarCompra(compra);
        compraDAO.eliminarCompra(compra.getId());
        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());

        assertNull(resultado);
    }

    /**
     * Permite probar la obtención de una compra por su ID.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener la
     * compra.
     */
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        Compra compra = new Compra("Compra Test", null);
        compraDAO.agregarCompra(compra);

        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());

        assertNotNull(resultado);
        assertEquals(compra.getId(), resultado.getId());
    }

    /**
     * Permite probar la obtención de todas las compras registradas.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener las
     * compras.
     */
    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        compraDAO.agregarCompra(new Compra("Compra 1", null));
        compraDAO.agregarCompra(new Compra("Compra 2", null));

        List<Compra> compras = compraDAO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.size() >= 2);
    }

    /**
     * Permite probar la actualización de una compra existente.
     *
     * @throws PersistenciaException Se lanza en caso de error al actualizar la
     * compra.
     */
    @Test
    public void testActualizarCompra() throws PersistenciaException {
        Compra compraOriginal = new Compra("Compra Inicial", null);
        compraDAO.agregarCompra(compraOriginal);

        compraOriginal.setNombre("Compra Actualizada");

        Compra compraActualizada = compraDAO.actualizarCompra(compraOriginal);

        assertNotNull(compraActualizada);
        assertEquals(compraOriginal.getId(), compraActualizada.getId());
        assertEquals("Compra Actualizada", compraActualizada.getNombre());
    }
 
}
