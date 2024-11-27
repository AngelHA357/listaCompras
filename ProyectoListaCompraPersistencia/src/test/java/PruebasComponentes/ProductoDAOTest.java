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

    private IProductoDAO productoDAO;
    private ICompraDAO compraDAO;
    private IClienteDAO clienteDAO;
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
    public void setUp() throws PersistenciaException {
        conexion = Conexion.getInstance();
        productoDAO = new ProductoDAO(conexion);
        compraDAO = new CompraDAO(conexion);
        clienteDAO = new ClienteDAO(conexion);
        limpiarBaseDeDatos();
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
     * Se verifica que el método agregarProducto agregue un producto
     * correctamente.
     *
     * @throws PersistenciaException Se lanza en caso de error al agregar el
     * producto.
     */
    @Test
    public void testAgregarProducto() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Papel", resultado.getNombre());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());
        assertEquals(compra.getId(), resultado.getCompra().getId());
    }

     /**
     * Se verifica que se maneje correctamente un intento de agregar un producto con nombre nulo.
     */
    @Test
    public void testAgregarProducto_NombreNulo() throws PersistenciaException {
        Producto producto = new Producto(null, "Higiene Personal", false, null, 6.0);
        Producto resultado = productoDAO.agregarProducto(producto);
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());
    }

    /**
     * Se verifica que se maneje correctamente un intento de agregar un producto con nombre vacío.
     */
    @Test
    public void testAgregarProducto_NombreVacio() throws PersistenciaException {
        Producto producto = new Producto("", "Higiene Personal", false, null, 6.0);
        Producto resultado = productoDAO.agregarProducto(producto);
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Higiene Personal", resultado.getCategoria());
        assertEquals(6.0, resultado.getCantidad());
    }

    /**
     * Se verifica que el método obtenerProductoPorId retorne el producto
     * correcto.
     *
     * @throws PersistenciaException Se lanza en caso de error al obtener el
     * producto.
     */
    @Test
    public void testObtenerProductoPorId() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        producto = productoDAO.agregarProducto(producto);

        Producto resultado = productoDAO.obtenerProductoPorId(producto.getId());

        assertNotNull(resultado);
        assertEquals(producto.getId(), resultado.getId());
        assertEquals(producto.getNombre(), resultado.getNombre());
        assertEquals(producto.getCategoria(), resultado.getCategoria());
        assertEquals(producto.getCantidad(), resultado.getCantidad());
    }

    /**
     * Se verifica que obtenerProductoPorId retorne null para un ID inexistente.
     */
    @Test
    public void testObtenerProductoPorId_NoExiste() throws PersistenciaException {
        Producto resultado = productoDAO.obtenerProductoPorId(99999L);
        assertNull(resultado);
    }

    /**
     * Se verifica que el método actualizarProducto actualice correctamente.
     */
    @Test
    public void testActualizarProducto() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        producto = productoDAO.agregarProducto(producto);

        producto.setNombre("Papel Premium");
        producto.setCantidad(8.0);
        producto.setComprado(true);

        Producto actualizado = productoDAO.actualizarProducto(producto);

        assertNotNull(actualizado);
        assertEquals("Papel Premium", actualizado.getNombre());
        assertEquals(8.0, actualizado.getCantidad());
        assertTrue(actualizado.isComprado());
    }

    /**
     * Se verifica que el método eliminarProducto elimine correctamente.
     */
    @Test
    public void testEliminarProducto() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        producto = productoDAO.agregarProducto(producto);

        productoDAO.eliminarProducto(producto.getId());

        Producto eliminado = productoDAO.obtenerProductoPorId(producto.getId());
        assertNull(eliminado);
    }

    /**
     * Se verifica que se lance excepción al eliminar un producto inexistente.
     */
    @Test
    public void testEliminarProducto_NoExiste() throws PersistenciaException {
        assertNull(productoDAO.eliminarProducto(99999L));
    }

    /**
     * Se verifica que el método filtrarPorCategoriaYCompraId filtre
     * correctamente.
     */
    @Test
    public void testFiltrarPorCategoriaYCompraId() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        productoDAO.agregarProducto(new Producto("Papel", "Higiene Personal", false, compra, 6.0));
        productoDAO.agregarProducto(new Producto("Jabón", "Higiene Personal", false, compra, 3.0));
        productoDAO.agregarProducto(new Producto("Leche", "Alimentos", false, compra, 1.0));

        List<Producto> filtrados = productoDAO.filtrarPorCategoriaYCompraId("Higiene Personal", compra.getId());
        Long compraID = compra.getId();

        assertNotNull(filtrados);
        assertEquals(2, filtrados.size());
        assertTrue(filtrados.stream().allMatch(p -> p.getCategoria().equals("Higiene Personal")));
        assertTrue(filtrados.stream().allMatch(p -> p.getCompra().getId().equals(compraID)));
    }

    /**
     * Se verifica que se retorne una lista vacía al filtrar por una categoría
     * inexistente.
     */
    @Test
    public void testFiltrarPorCategoriaYCompraId_CategoriaInexistente() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        List<Producto> filtrados = productoDAO.filtrarPorCategoriaYCompraId("Categoria Inexistente", compra.getId());

        assertNotNull(filtrados);
        assertTrue(filtrados.isEmpty());
    }

    /**
     * Se verifica que se maneje correctamente un intento de filtrar productos con categoría nula.
     */
    @Test
    public void testFiltrarPorCategoriaYCompraId_CategoriaNula() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);
        
        List<Producto> resultado = productoDAO.filtrarPorCategoriaYCompraId(null, compra.getId());
        assertTrue(resultado.isEmpty());
    }
    
    /**
     * Se verifica que el método obtenerProductosPorCompraId retorne la lista correcta.
     */
    @Test
    public void testObtenerProductosPorCompraId() throws PersistenciaException {
        // Preparar datos
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);
        
        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto1 = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        Producto producto2 = new Producto("Jabón", "Higiene Personal", false, compra, 3.0);
        productoDAO.agregarProducto(producto1);
        productoDAO.agregarProducto(producto2);

        // Ejecutar prueba
        List<Producto> productos = productoDAO.obtenerProductosPorCompraId(compra.getId());

        // Verificar resultados
        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equals("Papel")));
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equals("Jabón")));
    }

    /**
     * Se verifica que se retorne lista vacía al buscar productos de una compra
     * inexistente.
     */
    @Test
    public void testObtenerProductosPorCompraId_CompraInexistente() throws PersistenciaException {
        List<Producto> productos = productoDAO.obtenerProductosPorCompraId(99999L);
        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    /**
     * Se verifica que el método obtenerProductoPorCaracteristicas retorne el
     * producto correcto.
     */
    @Test
    public void testObtenerProductoPorCaracteristicas() throws PersistenciaException {
        // Preparar datos
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        Producto producto = new Producto("Papel", "Higiene Personal", false, compra, 6.0);
        productoDAO.agregarProducto(producto);

        // Ejecutar prueba
        Producto encontrado = productoDAO.obtenerProductoPorCaracteristicas(
                "Papel", "Higiene Personal", false, 6.0, compra.getId());

        // Verificar resultados
        assertNotNull(encontrado);
        assertEquals("Papel", encontrado.getNombre());
        assertEquals("Higiene Personal", encontrado.getCategoria());
        assertEquals(6.0, encontrado.getCantidad());
        assertFalse(encontrado.isComprado());
        assertEquals(compra.getId(), encontrado.getCompra().getId());
    }

    /**
     * Se verifica que se retorne null al buscar un producto con características
     * que no coinciden.
     */
    @Test
    public void testObtenerProductoPorCaracteristicas_NoCoincide() throws PersistenciaException {
        // Preparar datos
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);

        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);

        // Ejecutar prueba
        Producto encontrado = productoDAO.obtenerProductoPorCaracteristicas(
                "Producto Inexistente", "Categoria", false, 1.0, compra.getId());

        // Verificar resultados
        assertNull(encontrado);
    }

    /**
     * Se verifica que se lance PersistenciaException al buscar con nombre nulo.
     */
    @Test
    public void testObtenerProductoPorCaracteristicas_NombreNulo() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);
        Long compraID = compra.getId();

        assertNull(productoDAO.obtenerProductoPorCaracteristicas(
                null, "Categoria", false, 1.0, compraID));

    }

    /**
     * Se verifica que se lance PersistenciaException al buscar con categoría
     * nula.
     */
    @Test
    public void testObtenerProductoPorCaracteristicas_CategoriaNula() throws PersistenciaException {
        Cliente cliente = new Cliente("Juan", "Pérez", "López", "juanpl", "pass123");
        cliente = clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra Semanal", cliente);
        compra = compraDAO.agregarCompra(compra);
        Long compraID = compra.getId();

        assertNull(productoDAO.obtenerProductoPorCaracteristicas(
                null, "Categoria", false, 1.0, compraID));

    }

}
