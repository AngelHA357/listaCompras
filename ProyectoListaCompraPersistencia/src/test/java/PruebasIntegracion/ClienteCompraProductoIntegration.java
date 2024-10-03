/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

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
 *
 * @author JoseH
 */
public class ClienteCompraProductoIntegration {
    IClienteDAO clienteDAO;
    ICompraDAO compraDAO;
    IConexion conexion;
    IProductoDAO productoDAO;
    
    public ClienteCompraProductoIntegration() {
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
        clienteDAO = new ClienteDAO(conexion);
        compraDAO = new CompraDAO(conexion);
        productoDAO = new ProductoDAO(conexion);
    }
    
    @AfterEach
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
    public void testAgregarProductoYAsociarloACompra() throws PersistenciaException {
        // Crear un cliente y una compra
        Cliente cliente = new Cliente("Ana", "Martínez", "Lopez", "anam", "pass456");
        clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra de Productos", cliente);
        compraDAO.agregarCompra(compra);
        
        // Agregar un producto
        Producto producto = new Producto("Mesa", "Mobiliario", compra, 6.0);
        Producto productoGuardado = productoDAO.agregarProducto(producto);
        
        // Verificar que el producto esté en la compra
        Compra compraObtenida = compraDAO.obtenerCompraPorId(compra.getId());
        assertTrue(compraObtenida.getProductos().contains(productoGuardado));
    }
    
    @Test
    public void testAgregarMultiplesProductosASolucionDeCompra() throws PersistenciaException {
        // Crear cliente
        Cliente cliente = new Cliente("Carlos", "Jiménez", "Pérez", "carlosp", "pass654");
        clienteDAO.agregarCliente(cliente);

        // Crear compra
        Compra compra = new Compra("Compra Carlos", cliente);
        compraDAO.agregarCompra(compra);

        // Agregar múltiples productos
        Producto producto1 = new Producto("Libro", "Educación", compra, 6.0);
        Producto producto2 = new Producto("Lápiz", "Escritura", compra, 6.0);
        productoDAO.agregarProducto(producto1);
        productoDAO.agregarProducto(producto2);

        // Verificar que ambos productos estén en la compra
        Compra compraObtenida = compraDAO.obtenerCompraPorId(compra.getId());
        assertTrue(compraObtenida.getProductos().contains(producto1));
        assertTrue(compraObtenida.getProductos().contains(producto2));
    }
    
     @Test
    public void testActualizarProductoYVerificarEnCompra() throws PersistenciaException {
        // Crear cliente y compra
        Cliente cliente = new Cliente("Sofía", "López", "Cruz", "sofia", "pass321");
        clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra Sofía", cliente);
        compraDAO.agregarCompra(compra);
        
        // Agregar un producto
        Producto producto = new Producto("Silla", "Mobiliario", compra, 6.0);
        Producto productoGuardado = productoDAO.agregarProducto(producto);
        
        // Actualizar el producto
        productoGuardado.setNombre("Silla Nueva");
        Producto productoActualizado = productoDAO.actualizarProducto(productoGuardado);
        
        // Verificar que el producto actualizado esté en la compra
        Compra compraObtenida = compraDAO.obtenerCompraPorId(compra.getId());
        assertTrue(compraObtenida.getProductos().contains(productoActualizado));
        assertEquals("Silla Nueva", productoActualizado.getNombre());
    }
    
    @Test
    public void testEliminarProductoDeCompra() throws PersistenciaException {
        // Crear un cliente y una compra
        Cliente cliente = new Cliente("Laura", "Fernández", "Gómez", "lauraf", "pass789");
        clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra de Laura", cliente);
        compraDAO.agregarCompra(compra);

        // Agregar un producto
        Producto producto = new Producto("Cama", "Mobiliario", compra, 6.0);
        Producto productoGuardado = productoDAO.agregarProducto(producto);

        // Eliminar el producto
        productoDAO.eliminarProducto(productoGuardado.getId());

        // Verificar que el producto no esté en la compra
        Compra compraObtenida = compraDAO.obtenerCompraPorId(compra.getId());
        assertFalse(compraObtenida.getProductos().contains(productoGuardado));
    }
    
    @Test
    public void testEliminarCompra() throws PersistenciaException {
        // Crear cliente y compra
        Cliente cliente = new Cliente("Luis", "García", "Martínez", "luism", "pass888");
        clienteDAO.agregarCliente(cliente);
        Compra compra = new Compra("Compra de Luis", cliente);
        compraDAO.agregarCompra(compra);

        // Agregar un producto
        Producto producto = new Producto("Escritorio", "Mobiliario", compra, 6.0);
        productoDAO.agregarProducto(producto);

        // Eliminar la compra
        compraDAO.eliminarCompra(compra.getId());

        // Verificar que la compra ya no existe
        assertThrows(PersistenciaException.class, () -> compraDAO.obtenerCompraPorId(compra.getId()));
    }
    
    
}

