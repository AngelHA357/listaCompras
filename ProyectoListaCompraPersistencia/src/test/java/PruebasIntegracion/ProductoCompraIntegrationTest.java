/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.CompraDAO;
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
 *
 * @author JoseH
 */
public class ProductoCompraIntegrationTest {

    private IProductoDAO productoDAO;
    private ICompraDAO compraDAO;
    private IConexion conexion;

    public ProductoCompraIntegrationTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        conexion = Conexion.getInstance();
        productoDAO = new ProductoDAO(conexion);
        compraDAO = new CompraDAO(conexion);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAgregarProductoYCompra() throws PersistenciaException {
        // Agregar un producto
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        Producto productoAgregado = productoDAO.agregarProducto(producto);

        // Crear una compra con el producto
        Compra compra = new Compra("Compra de Papeles", null);
        compra.agregarProducto(productoAgregado);
        Compra compraAgregada = compraDAO.agregarCompra(compra);

        // Verificar que tanto el producto como la compra se hayan agregado correctamente
        assertNotNull(productoAgregado.getId());
        assertNotNull(compraAgregada.getId());
        assertTrue(compraAgregada.getProductos().contains(productoAgregado));
    }

    @Test
    public void testObtenerProductosPorCompra() throws PersistenciaException {
        // Crear un producto y agregarlo a una compra
        Producto producto = new Producto("Jabón", "Higiene Personal", null, 6.0);
        Producto productoAgregado = productoDAO.agregarProducto(producto);

        Compra compra = new Compra("Compra de Jabones", null);
        compra.agregarProducto(productoAgregado);
        compraDAO.agregarCompra(compra);

        // Obtener los productos de la compra
        List<Producto> productos = compra.getProductos();

        // Verificar que los productos son correctos
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(productoAgregado, productos.get(0));
    }
    
    @Test
    public void testActualizarProductoYVerificarEnCompras() throws PersistenciaException {
        Producto producto = new Producto("Jabón de barra", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        Compra compra = new Compra("Compra de Jabón", null);
        compra.agregarProducto(producto);
        compraDAO.agregarCompra(compra);

        producto.setNombre("Jabón Nuevo");
        productoDAO.actualizarProducto(producto);

        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());
        assertEquals("Jabón Nuevo", resultado.getProductos().get(0).getNombre());
    }
    
    
}


