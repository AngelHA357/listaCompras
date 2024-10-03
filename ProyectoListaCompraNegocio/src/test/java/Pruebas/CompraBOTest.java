/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import BOs.ClienteBO;
import BOs.CompraBO;
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
import Exceptions.PersistenciaException;
import java.util.ArrayList;
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
public class CompraBOTest {

    private ICompraBO compraBO;
    private IConexion conexion;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true"); // Activar modo prueba si es necesario
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba"); // Limpiar propiedades al finalizar
    }

    @BeforeEach
    public void setUp() {
        conexion = Conexion.getInstance(); // Obtener instancia de conexión
        compraBO = new CompraBO(); // Inicializar instancia de CompraBO
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos(); // Limpiar la base de datos después de cada prueba
    }

    private void limpiarBaseDeDatos() throws PersistenciaException {
        // Limpiar productos, clientes y compras
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        ICompraDAO compraDAO = new CompraDAO(conexion);

        if (!productoDAO.obtenerTodosLosProductos().isEmpty()) {
            for (Producto producto : productoDAO.obtenerTodosLosProductos()) {
                productoDAO.eliminarProducto(producto.getId());
            }
        }

        if (!clienteDAO.obtenerTodosLosClientes().isEmpty()) {
            for (Cliente cliente : clienteDAO.obtenerTodosLosClientes()) {
                clienteDAO.eliminarCliente(cliente.getId());
            }
        }

        if (!compraDAO.obtenerTodasLasCompras().isEmpty()) {
            for (Compra compra : compraDAO.obtenerTodasLasCompras()) {
                compraDAO.eliminarCompra(compra.getId());
            }
        }
    }

    @Test
    public void testAgregarCompra() {
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);

        compraBO.agregarCompra(compraDTO);

        // Obtener la compra recién agregada desde la base de datos
        List<CompraDTO> resultado = compraBO.obtenerTodasLasCompras();

        assertNotNull(resultado);
        assertTrue(resultado.stream().anyMatch(compra -> compra.getNombreCompra().equals("Compra de Prueba")));
    }

    @Test
    public void testObtenerCompraPorId() {
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);
        compraDTO = compraBO.agregarCompra(compraDTO);

        CompraDTO resultado = compraBO.obtenerCompraPorId(compraDTO.getId());

        assertNotNull(resultado);
        assertEquals(compraDTO.getId(), resultado.getId());
    }

    @Test
    public void testObtenerTodasLasCompras() {
        compraBO.agregarCompra(new CompraDTO("Compra 1", null));
        compraBO.agregarCompra(new CompraDTO("Compra 2", null));

        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.size() >= 2); // Verificar que al menos hay dos compras
    }

    @Test
    public void testActualizarCompra() {
        CompraDTO compraDTO = new CompraDTO("Compra Original", null);
        compraDTO = compraBO.agregarCompra(compraDTO);
        
        compraDTO = compraBO.obtenerCompraPorId(compraDTO.getId());

        compraDTO.setNombreCompra("Compra Actualizada");
        CompraDTO resultado = compraBO.actualizarCompra(compraDTO);

        assertNotNull(resultado);
        assertEquals(compraDTO.getId(), resultado.getId());
        assertEquals("Compra Actualizada", resultado.getNombreCompra());
    }

    @Test
    public void testEliminarCompra() {
        CompraDTO compraDTO = new CompraDTO("Compra a Eliminar", null);
        compraDTO = compraBO.agregarCompra(compraDTO);

        compraBO.eliminarCompra(compraDTO.getId());

        CompraDTO resultado = compraBO.obtenerCompraPorId(compraDTO.getId());

        assertNull(resultado); // Verificar que la compra fue eliminada correctamente
    }
}
