/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import BOs.ClienteBO;
import BOs.CompraBO;
import BOs.IClienteBO;
import BOs.ICompraBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import DAOs.CompraDAO;
import DAOs.IClienteDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.NegocioException;
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
public class ClienteCompraIntegration {
    IClienteBO clienteBO;
    ICompraBO compraBO;
    
    public ClienteCompraIntegration() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        clienteBO = new ClienteBO();
        compraBO = new CompraBO();
    }
    
    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }
    
    private void limpiarBaseDeDatos() throws PersistenciaException {
        IConexion conexion = Conexion.getInstance();
         // Obtener todos los productos y eliminarlos
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }
        
        // Obtener todas las compras y eliminarlas
        ICompraDAO compraDAO = new CompraDAO(conexion);
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }
        
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for(Cliente cliente : clientes){
            clienteDAO.eliminarCliente(cliente.getId());
        }

       
    }

    @Test
    public void testAgregarClienteYCompra() throws NegocioException {
        // Agregar un cliente
        ClienteDTO clienteDTO = new ClienteDTO("Juan", "Pérez", "Gómez", "juanp", "pass123");
        ClienteDTO clienteAgregado = clienteBO.agregarCliente(clienteDTO);

        // Agregar una compra para el cliente
        CompraDTO compraDTO = new CompraDTO("Compra de Ejemplo", clienteAgregado);
        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);

        // Verificar que tanto el cliente como la compra se hayan agregado correctamente
        assertNotNull(clienteAgregado.getId());
        assertNotNull(compraAgregada.getId());
        assertEquals(clienteAgregado.getId(), compraAgregada.getCliente().getId());
    }

    @Test
    public void testEliminarCompraYVerificarCliente() throws NegocioException {
        // Agregar un cliente
        ClienteDTO clienteDTO = new ClienteDTO("Adriana", "López", "Santiago", "adriana", "pass141");
        clienteDTO = clienteBO.agregarCliente(clienteDTO);

        // Agregar una compra
        CompraDTO compraDTO = new CompraDTO("Compra única", clienteDTO);
        compraDTO = compraBO.agregarCompra(compraDTO);

        // Eliminar la compra
        compraBO.eliminarCompra(compraDTO.getId());

        // Verificar que la compra ha sido eliminada
        assertNull(compraBO.obtenerCompraPorId(compraDTO.getId()));
    }

    @Test
    public void testAgregarVariasComprasYVerificarConsistencia() throws NegocioException {
        // Agregar un cliente
        ClienteDTO clienteDTO = new ClienteDTO("Natalia", "Reyes", "Pérez", "natalia", "pass121");
        clienteDTO = clienteBO.agregarCliente(clienteDTO);

        // Agregar varias compras
        for (int i = 1; i <= 5; i++) {
            CompraDTO compraDTO = new CompraDTO("Compra " + i, clienteDTO);
            compraBO.agregarCompra(compraDTO);
        }

        // Verificar que se han agregado todas las compras
        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();
        assertNotNull(compras);
        assertTrue(compras.size() == 5); 
    }

    @Test
    public void testObtenerComprasPorCliente_ClienteExistente() throws NegocioException {
        // Crear un cliente y agregarlo a la base de datos
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Test", "Apellido", "Segundo", "test", "pass");
        clienteDTO = clienteBO.agregarCliente(clienteDTO);

       

        // Crear y agregar compras asociadas al cliente
        CompraDTO compra1 = new CompraDTO("Compra 1", clienteDTO);
        CompraDTO compra2 = new CompraDTO("Compra 2", clienteDTO);

        compraBO.agregarCompra(compra1);
        compraBO.agregarCompra(compra2);

        // Obtener las compras por cliente
        List<CompraDTO> compras = compraBO.obtenerComprasPorCliente(clienteDTO.getId());

        // Verificar las compras
        assertNotNull(compras);
        assertTrue(compras.size() >= 2);
        assertFalse(compras.isEmpty());
        assertTrue(compras.stream().anyMatch(compra -> compra.getNombreCompra().equals("Compra 1")));
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraExistente() throws NegocioException {
        // Crear un cliente y una compra de prueba
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Test", "Apellido", "Segundo", "test", "pass");
        clienteDTO = clienteBO.agregarCliente(clienteDTO);


        String nombreCompra = "Compra 1" + System.currentTimeMillis();

        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setNombreCompra(nombreCompra);
        compraDTO.setCliente(clienteDTO);
        compraBO.agregarCompra(compraDTO); // Guardar la compra en la base de datos

        // Obtener la compra por nombre y cliente
        CompraDTO compraObtenida = compraBO.obtenerCompraPorNombreYCliente(nombreCompra, clienteDTO.getId());

        // Verificar la compra
        assertNotNull(compraObtenida);
        assertEquals(nombreCompra, compraObtenida.getNombreCompra());
        assertEquals(clienteDTO.getId(), compraObtenida.getCliente().getId());
    }

    @Test
    public void testObtenerComprasPorCliente_ClienteSinCompras() throws NegocioException {
        // Generar un ID dinámico para el cliente sin compras
        Long clienteId = 999L;

        // Obtener las compras para este cliente (sin compras)
        List<CompraDTO> compras = compraBO.obtenerComprasPorCliente(clienteId);

        // Verificar que no haya compras
        assertNotNull(compras);
        assertTrue(compras.isEmpty());
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraInexistente() throws NegocioException {
        // Generar un ID dinámico para el cliente
        Long clienteId = 999L;
        String nombreCompra = "Compra Inexistente";

        assertNull(compraBO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId));
    }
}
