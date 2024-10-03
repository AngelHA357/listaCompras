/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
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
 *
 * @author JoseH
 */
public class CompraDAOTest {
    ICompraDAO compraDAO;
    IConexion conexion;
    private static Long clienteIdCounter = 1000L; 
    
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
    
    private void limpiarBaseDeDatos() throws PersistenciaException {
         // Obtener todos los productos y eliminarlos
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }
        
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for(Cliente cliente : clientes){
            clienteDAO.eliminarCliente(cliente.getId());
        }
        
        // Obtener todas las compras y eliminarlas
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }

       
    }
    
    @Test
    public void agregarCompra() throws PersistenciaException{
        Compra compra = new Compra("Cosas para el GYM", null);
        
        Compra resultado = compraDAO.agregarCompra(compra);
        
        
        assertNotNull(resultado.getId()); 
        assertEquals("Cosas para el GYM", resultado.getNombre());
        
    }
    
    @Test
    public void eliminarCompra() throws PersistenciaException{
        Compra compra = new Compra("EjemploCompra", null);
        compraDAO.agregarCompra(compra);
        Compra resultado = compraDAO.eliminarCompra(compra.getId());
        
        assertEquals("EjemploCompra", resultado.getNombre());
    }
    
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        Compra compra = new Compra("Compra Test", null);
        compraDAO.agregarCompra(compra);

        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());

        assertNotNull(resultado);
        assertEquals(compra.getId(), resultado.getId());
    }
    
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        compraDAO.agregarCompra(new Compra("Compra 1", null));
        compraDAO.agregarCompra(new Compra("Compra 2", null));

        List<Compra> compras = compraDAO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.size() >= 2); // Verificar que al menos hay dos compras
    }
    
    @Test
    public void testEliminarCompraInexistente() throws PersistenciaException {
        Compra resultado = compraDAO.eliminarCompra(999L); // ID que no existe
        assertNull(resultado); // Debería retornar null
    }
    
   

    
}
