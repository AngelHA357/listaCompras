/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasComponentes;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
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
public class ProductoDAOTest {
    IProductoDAO productoDAO;
    IConexion conexion;
    
    public ProductoDAOTest() {
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
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAgregarProducto() throws PersistenciaException {
        
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);

        Producto resultado = productoDAO.agregarProducto(producto);

        assertNotNull(resultado); // Asegurarse de que el producto no sea nulo
        assertEquals("Papel", resultado.getNombre()); // Verificar el nombre
    }
    
    public void testObtenerProductoPorId() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto); // Asegurarse de que el producto está en la base de datos

        Producto resultado = productoDAO.obtenerProductoPorId(producto.getId());

        assertNotNull(resultado); // Asegurarse de que el resultado no sea nulo
        assertEquals(producto.getId(), resultado.getId()); // Verificar que el ID coincida
    }
    
    @Test
    public void testObtenerTodosLosProductos() throws PersistenciaException {
        productoDAO.agregarProducto(new Producto("Papel", "Higiene Personal", null, 6.0));
        productoDAO.agregarProducto(new Producto("Jabón", "Higiene Personal", null, 6.0));

        List<Producto> productos = productoDAO.obtenerTodosLosProductos();

        assertNotNull(productos); // Asegurarse de que la lista no sea nula
        assertTrue(productos.size() >= 2); // Verificar que al menos haya dos productos
    }
    
    @Test
    public void testActualizarProducto() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        producto.setNombre("Papel Nuevo"); // Cambiar el nombre
        Producto resultado = productoDAO.actualizarProducto(producto);

        assertNotNull(resultado); // Asegurarse de que el resultado no sea nulo
        assertEquals("Papel Nuevo", resultado.getNombre()); // Verificar el nuevo nombre
    }
    
    @Test
    public void testEliminarProducto() throws PersistenciaException {
        Producto producto = new Producto("Papel", "Higiene Personal", null, 6.0);
        productoDAO.agregarProducto(producto);

        Producto eliminado = productoDAO.eliminarProducto(producto.getId());

        assertNotNull(eliminado); // Asegurarse de que el producto eliminado no sea nulo
        assertEquals(producto.getId(), eliminado.getId()); // Verificar que el ID coincida
        assertNull(productoDAO.obtenerProductoPorId(producto.getId())); // Verificar que el producto ya no esté en la base de datos
    }
    
     @Test
    public void testFiltrarPorCategoria() throws PersistenciaException {
        productoDAO.agregarProducto(new Producto("Papel", "Higiene Personal", null, 6.0));
        productoDAO.agregarProducto(new Producto("Jabón", "Higiene Personal", null, 6.0));
        productoDAO.agregarProducto(new Producto("Leche", "Alimentos", null, 10.0));

        List<Producto> productosFiltrados = productoDAO.filtrarPorCategoria("Higiene Personal");

        assertNotNull(productosFiltrados); // Asegurarse de que la lista no sea nula
        assertEquals(2, productosFiltrados.size()); // Debería devolver solo 2 productos
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

}
