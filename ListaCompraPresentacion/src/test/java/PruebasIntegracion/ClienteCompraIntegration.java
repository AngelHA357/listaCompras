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
import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorClientes;
import Subsistemas.GestorCompras;
import Subsistemas.IGestorClientes;
import Subsistemas.IGestorCompras;
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

    IGestorClientes gestorClientes;
    IGestorCompras gestorCompras;

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
        gestorClientes = new GestorClientes();
        gestorCompras = new GestorCompras();
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }

    /**
     * Permite limpiar la base de datos eliminando todos los productos, compras
     * y clientes.
     */
    private void limpiarBaseDeDatos() throws PersistenciaException {
        IConexion conexion = Conexion.getInstance();
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            productoDAO.eliminarProducto(producto.getId());
        }

        ICompraDAO compraDAO = new CompraDAO(conexion);
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        for (Compra compra : compras) {
            compraDAO.eliminarCompra(compra.getId());
        }

        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            clienteDAO.eliminarCliente(cliente.getId());
        }
    }

    /**
     * Permite agregar un cliente y una compra, verificando que ambos se hayan
     * agregado correctamente.
     */
    @Test
    public void testAgregarClienteYCompra() throws NegocioException  {
        ClienteDTO clienteDTO = new ClienteDTO("Juan", "Pérez", "Gómez", "juanp", "pass123");
        ClienteDTO clienteAgregado = gestorClientes.agregarCliente(clienteDTO);
        CompraDTO compraDTO = new CompraDTO("Compra de Ejemplo", clienteAgregado);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(clienteAgregado.getId());
        assertNotNull(compraAgregada.getId());
        assertEquals(clienteAgregado.getId(), compraAgregada.getCliente().getId());
    }

    /**
     * Permite eliminar una compra y verificar que ha sido eliminada
     * correctamente del cliente asociado.
     */
    @Test
    public void testEliminarCompraYVerificarCliente() throws NegocioException  {
        ClienteDTO clienteDTO = new ClienteDTO("Adriana", "López", "Santiago", "adriana", "pass141");
        clienteDTO = gestorClientes.agregarCliente(clienteDTO);
        CompraDTO compraDTO = new CompraDTO("Compra única", clienteDTO);
        compraDTO = gestorCompras.agregarCompra(compraDTO);
        gestorCompras.eliminarCompra(compraDTO.getId());
        assertNull(gestorCompras.obtenerCompraPorId(compraDTO.getId()));
    }

    /**
     * Permite agregar varias compras a un cliente y verificar que se hayan
     * registrado todas correctamente.
     */
    @Test
    public void testAgregarVariasComprasYVerificarConsistencia() throws NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO("Natalia", "Reyes", "Pérez", "natalia", "pass121");
        clienteDTO = gestorClientes.agregarCliente(clienteDTO);
        for (int i = 1; i <= 5; i++) {
            CompraDTO compraDTO = new CompraDTO("Compra " + i, clienteDTO);
            gestorCompras.agregarCompra(compraDTO);
        }
        List<CompraDTO> compras = gestorCompras.obtenerTodasLasCompras();
        assertNotNull(compras);
        assertTrue(compras.size() == 5);
    }

    /**
     * Permite obtener todas las compras realizadas por un cliente existente y
     * verificar su consistencia.
     */
    @Test
    public void testObtenerComprasPorCliente_ClienteExistente() throws NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Test", "Apellido", "Segundo", "test", "pass");
        clienteDTO = gestorClientes.agregarCliente(clienteDTO);
        CompraDTO compra1 = new CompraDTO("Compra 1", clienteDTO);
        CompraDTO compra2 = new CompraDTO("Compra 2", clienteDTO);
        gestorCompras.agregarCompra(compra1);
        gestorCompras.agregarCompra(compra2);
        List<CompraDTO> compras = gestorCompras.obtenerComprasPorCliente(clienteDTO.getId());
        assertNotNull(compras);
        assertTrue(compras.size() >= 2);
        assertFalse(compras.isEmpty());
        assertTrue(compras.stream().anyMatch(compra -> compra.getNombreCompra().equals("Compra 1")));
    }

    /**
     * Permite obtener una compra existente por su nombre y cliente asociado,
     * verificando la consistencia de los datos.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_CompraExistente() throws NegocioException  {
        ClienteDTO clienteDTO = new ClienteDTO("Cliente Test", "Apellido", "Segundo", "test", "pass");
        clienteDTO = gestorClientes.agregarCliente(clienteDTO);
        String nombreCompra = "Compra 1" + System.currentTimeMillis();
        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setNombreCompra(nombreCompra);
        compraDTO.setCliente(clienteDTO);
        gestorCompras.agregarCompra(compraDTO);
        CompraDTO compraObtenida = gestorCompras.obtenerCompraPorNombreYCliente(nombreCompra, clienteDTO.getId());
        assertNotNull(compraObtenida);
        assertEquals(nombreCompra, compraObtenida.getNombreCompra());
        assertEquals(clienteDTO.getId(), compraObtenida.getCliente().getId());
    }

    /**
     * Permite verificar que un cliente sin compras retorna una lista vacía al
     * consultar sus compras.
     */
    @Test
    public void testObtenerComprasPorCliente_ClienteSinCompras(){
        Long clienteId = 999L;
        List<CompraDTO> compras = gestorCompras.obtenerComprasPorCliente(clienteId);
        assertNotNull(compras);
        assertTrue(compras.isEmpty());
    }

    /**
     * Permite verificar que una compra inexistente por nombre y cliente no se
     * encuentra en la base de datos.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_CompraInexistente(){
        Long clienteId = 999L;
        String nombreCompra = "Compra Inexistente";
        assertNull(gestorCompras.obtenerCompraPorNombreYCliente(nombreCompra, clienteId));
    }

}
