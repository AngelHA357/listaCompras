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
 * Esta clase permite realizar pruebas unitarias con el Producto.
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class ProductoDAOTest {

    IProductoDAO productoDAO;
    ICompraDAO compraDAO;
    IConexion conexion;

    public ProductoDAOTest() {
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
        productoDAO = new ProductoDAO(conexion);
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
        ICompraDAO compraDAO = new CompraDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }

        IClienteDAO clienteDAO = new ClienteDAO(conexion);
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
     * Permite probar la adición de un nuevo producto.
     *
     * @throws PersistenciaException Se lanza en caso de error al agregar el
     * producto.
     */
    @Test
    public void testAgregarProducto() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);

        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Papel", resultado.getNombre());
    }

    /**
     * Permite probar la obtención de un producto por su ID.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener el
     * producto.
     */
    @Test
    public void testObtenerProductoPorId() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        Producto resultado = productoDAO.obtenerProductoPorId(producto.getId());

        assertNotNull(resultado);
        assertEquals(producto.getId(), resultado.getId());
    }

    /**
     * Permite probar la obtención de todos los productos registrados.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener los
     * productos.
     */
    @Test
    public void testObtenerTodosLosProductos() throws PersistenciaException {
        productoDAO.agregarProducto(new Producto("Papel", "Higiene Personal", null, 6.0));
        productoDAO.agregarProducto(new Producto("Jabón", "Higiene Personal", null, 6.0));

        List<Producto> productos = productoDAO.obtenerTodosLosProductos();

        assertNotNull(productos);
        assertTrue(productos.size() >= 2);
    }

    /**
     * Permite probar la actualización de un producto.
     *
     * @throws PersistenciaException Se lanza en caso de error al actualizar el
     * producto.
     */
    @Test
    public void testActualizarProducto() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        producto.setNombre("Papel Nuevo");
        Producto resultado = productoDAO.actualizarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Papel Nuevo", resultado.getNombre());
    }

    /**
     * Permite probar la eliminación de un producto.
     *
     * @throws PersistenciaException Se lanza en caso de error al eliminar el
     * producto.
     */
    @Test
    public void testEliminarProducto() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        Producto eliminado = productoDAO.eliminarProducto(producto.getId());

        assertNotNull(eliminado);
        assertEquals(producto.getId(), eliminado.getId());
        assertNull(productoDAO.obtenerProductoPorId(producto.getId()));
    }

    /**
     * Permite probar el filtrado de productos por categoría en una compra.
     *
     * @throws PersistenciaException Se lanza en caso de error al filtrar los
     * productos.
     */
    @Test
    public void testFiltrarPorCategoria() throws PersistenciaException {
        Compra compra1 = new Compra();
        compraDAO.agregarCompra(compra1);
        productoDAO.agregarProducto(new Producto("Papel", "Higiene Personal", compra1, 6.0));
        productoDAO.agregarProducto(new Producto("Jabón", "Higiene Personal", compra1, 6.0));
        productoDAO.agregarProducto(new Producto("Leche", "Alimentos", compra1, 10.0));

        List<Producto> productosFiltrados = productoDAO.filtrarPorCategoriaYCompraId("Higiene Personal", compra1.getId());

        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
    }

}
