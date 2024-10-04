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
public class ClienteCompraIntegrationTest {
    IClienteDAO clienteDAO;
    ICompraDAO compraDAO;
    IConexion conexion;
    
    public ClienteCompraIntegrationTest() {
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
    }
    
    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }
    
    private void limpiarBaseDeDatos() throws PersistenciaException {
         // Obtener todos los productos y eliminarlos
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }
        
        // Obtener todas las compras y eliminarlas
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }
        
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for(Cliente cliente : clientes){
            clienteDAO.eliminarCliente(cliente.getId());
        }

       
    }
    
    @Test
    public void testAgregarClienteYCompra() throws PersistenciaException {
        // Agregar un cliente
        Cliente cliente = new Cliente("Juan", "Pérez", "Gómez", "juanp", "pass123");
        Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);

        // Agregar una compra para el cliente
        Compra compra = new Compra("Compra de Ejemplo", clienteAgregado);
        Compra compraAgregada = compraDAO.agregarCompra(compra);

        // Verificar que tanto el cliente como la compra se hayan agregado correctamente
        assertNotNull(clienteAgregado.getId());
        assertNotNull(compraAgregada.getId());
        assertEquals(clienteAgregado.getId(), compraAgregada.getCliente().getId());
    }
    
    @Test
    public void testEliminarCompraYVerificarCliente() throws PersistenciaException {
        // Agregar un cliente
        Cliente cliente = new Cliente("Adriana", "López", "Santiago", "adriana", "pass141");
        clienteDAO.agregarCliente(cliente);

        // Agregar una compra
        Compra compra = new Compra("Compra única", cliente);
        compraDAO.agregarCompra(compra);

        // Eliminar la compra
        compraDAO.eliminarCompra(compra.getId());

        // Verificar que la compra ha sido eliminada
        assertNull(compraDAO.obtenerCompraPorId(compra.getId()));
    }
    
    @Test
    public void testAgregarVariasComprasYVerificarConsistencia() throws PersistenciaException {
        // Agregar un cliente
        Cliente cliente = new Cliente("Natalia", "Reyes", "Pérez", "natalia", "pass121");
        clienteDAO.agregarCliente(cliente);

        // Agregar varias compras
        for (int i = 1; i <= 5; i++) {
            Compra compra = new Compra("Compra " + i, cliente);
            compraDAO.agregarCompra(compra);
        }

        // Verificar que se han agregado todas las compras
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        assertNotNull(compras);
        assertTrue(compras.size() >= 5); // Asegúrate de que hay al menos 5 compras
    }
    
     @Test
    public void testObtenerComprasPorCliente_ClienteExistente() throws PersistenciaException {
        // Crear un cliente y agregarlo a la base de datos
        Cliente cliente = new Cliente();
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        clienteDAO.agregarCliente(cliente); // Se guarda el cliente en la base de datos

        // Obtener el ID del cliente generado automáticamente
        Long clienteId = cliente.getId(); // Asegúrate de que el método getId() obtenga el ID correcto

        // Crear y agregar compras asociadas al cliente
        Compra compra1 = new Compra("Compra 1", cliente);
        Compra compra2 = new Compra("Compra 2", cliente);

        compraDAO.agregarCompra(compra1);
        compraDAO.agregarCompra(compra2);

        // Obtener las compras por cliente
        List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);

        // Verificar las compras
        assertNotNull(compras);
        assertTrue(compras.size() >= 2);
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraExistente() throws PersistenciaException {
        // Crear un cliente y una compra de prueba
        Cliente cliente = new Cliente();
        // No establecer manualmente el ID del cliente, ya que es autogenerado
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        clienteDAO.agregarCliente(cliente); // Se guarda el cliente en la base de datos

        // Obtener el ID del cliente generado automáticamente
        Long clienteId = cliente.getId(); // Asegúrate de que el método getId() obtenga el ID correcto

        String nombreCompra = "Compra 1" + System.currentTimeMillis();

        Compra compra = new Compra();
        compra.setNombre(nombreCompra);
        compra.setCliente(cliente);
        compraDAO.agregarCompra(compra); // Guardar la compra en la base de datos

        // Obtener la compra por nombre y cliente
        Compra compraObtenida = compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);

        // Verificar la compra
        assertNotNull(compraObtenida);
        assertEquals(nombreCompra, compraObtenida.getNombre());
        assertEquals(clienteId, compraObtenida.getCliente().getId());
    }

    @Test
    public void testObtenerComprasPorCliente_ClienteSinCompras() throws PersistenciaException {
        // Generar un ID dinámico para el cliente sin compras
        Long clienteId = 999L;

        // Obtener las compras para este cliente (sin compras)
        List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);

        // Verificar que no haya compras
        assertNotNull(compras);
        assertTrue(compras.isEmpty());
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraInexistente() throws PersistenciaException {
        // Generar un ID dinámico para el cliente
        Long clienteId = 999L;
        String nombreCompra = "Compra Inexistente";

        assertNull(compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId));
        
    }
    
}
