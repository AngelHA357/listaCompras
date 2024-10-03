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
    @Test
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }
    
    private void limpiarBaseDeDatos() throws PersistenciaException {
         // Obtener todos los productos y eliminarlos
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }
        
        // Obtener todas las compras y eliminarlas
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }

       
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
    
    @Test
    public void testEliminarProductoYVerificarEnCompras() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        Producto productoAgregado = productoDAO.agregarProducto(producto);

        Compra compra = new Compra("Compra de Papeles", null);
        compra.agregarProducto(productoAgregado);
        compraDAO.agregarCompra(compra);

        productoDAO.eliminarProducto(productoAgregado.getId());

        // Verificar que el producto no esté en la compra
        Compra compraResultado = compraDAO.obtenerCompraPorId(compra.getId());
        assertFalse(compraResultado.getProductos().contains(productoAgregado));
    }
    
     @Test
    public void testObtenerProductosPorCompraId() throws PersistenciaException {
        // Primero se necesita crear una compra y asociarle productos.
        // Supongamos que ya tienes una clase Compra y métodos adecuados para manejarlo.

        Compra compra = new Compra(); // Aquí se crearían los atributos necesarios
        ICompraDAO compraDAO = new CompraDAO(conexion);
        compraDAO.agregarCompra(compra);

        // Crear y asociar productos a la compra
        Producto producto1 = new Producto("Papel", "Higiene Personal", compra, 6.0);
        Producto producto2 = new Producto("Jabón", "Higiene Personal", compra, 6.0);
        productoDAO.agregarProducto(producto1);
        productoDAO.agregarProducto(producto2);

        List<Producto> productosPorCompra = productoDAO.obtenerProductosPorCompraId(compra.getId());

        assertNotNull(productosPorCompra); // Asegurarse de que la lista no sea nula
        assertEquals(2, productosPorCompra.size()); // Debería devolver los 2 productos asociados
        for (Producto p : productosPorCompra) {
            assertEquals(compra.getId(), p.getCompra().getId()); // Verificar que todos los productos sean de la misma compra
        }
    }
    
    @Test
    public void testConsistenciaDeEstado() throws PersistenciaException {
        // Agregar productos
        Producto producto1 = new Producto("Papel", "Higiene Personal", null, 6.0);
        Producto producto2 = new Producto("Jabón", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto1);
        productoDAO.agregarProducto(producto2);

        // Crear compra
        Compra compra = new Compra("Compra Variada", null);
        compra.agregarProducto(producto1);
        compra.agregarProducto(producto2);
        compraDAO.agregarCompra(compra);

        // Actualizar producto
        producto1.setNombre("Papel Nuevo");
        productoDAO.actualizarProducto(producto1);

        // Eliminar un producto
        productoDAO.eliminarProducto(producto2.getId());

        // Verificar el estado final
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        assertEquals(1, compras.size()); // Debe haber solo una compra
        assertEquals("Papel Nuevo", compras.get(0).getProductos().get(0).getNombre());
        assertFalse(compras.get(0).getProductos().contains(producto2));
    }
    
    
    
}


