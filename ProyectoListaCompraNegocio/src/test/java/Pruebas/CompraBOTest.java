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
import Exceptions.NegocioException;
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
        System.setProperty("modoPrueba", "true"); 
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba"); 
    }

    @BeforeEach
    public void setUp() throws PersistenciaException {
        conexion = Conexion.getInstance(); 
        compraBO = new CompraBO(); 
        limpiarBaseDeDatos();
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
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
        
        if (!compraDAO.obtenerTodasLasCompras().isEmpty()) {
            for (Compra compra : compraDAO.obtenerTodasLasCompras()) {
                compraDAO.eliminarCompra(compra.getId());
            }
        }

        if (!clienteDAO.obtenerTodosLosClientes().isEmpty()) {
            for (Cliente cliente : clienteDAO.obtenerTodosLosClientes()) {
                clienteDAO.eliminarCliente(cliente.getId());
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
    
//     @Test
//    public void testAgregarCompra_NombreNulo() {
//        CompraDTO compraDTO = new CompraDTO(null, null);
//
//        assertThrows(NegocioException.class, () -> {
//            compraBO.agregarCompra(compraDTO);
//        });
//    }
    
//    @Test
//    public void testAgregarCompra_NombreVacio() {
//        CompraDTO compraDTO = new CompraDTO("", null);
//
//        assertThrows(NegocioException.class, () -> {
//            compraBO.agregarCompra(compraDTO);
//        });
//    }
    
    @Test
    public void testObtenerCompraPorId() {
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);
        compraDTO = compraBO.agregarCompra(compraDTO);

        CompraDTO resultado = compraBO.obtenerCompraPorId(compraDTO.getId());

        assertNotNull(resultado);
        assertEquals(compraDTO.getId(), resultado.getId());
    }
    
    @Test
    public void testObtenerCompraPorId_Inexistente() {
        long idInexistente = 9999L; 

        CompraDTO resultado = compraBO.obtenerCompraPorId(idInexistente);

        assertNull(resultado); 
    }

    @Test
    public void testObtenerTodasLasCompras() {
        compraBO.agregarCompra(new CompraDTO("Compra 1", null));
        compraBO.agregarCompra(new CompraDTO("Compra 2", null));

        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.size() == 2); 
    }
    
    @Test
    public void testObtenerTodasLasCompras_Vacio() {
        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.isEmpty());
    }
    
    @Test
    public void testEliminarCompra() {
        CompraDTO compraDTO = new CompraDTO("Compra a Eliminar", null);
        compraDTO = compraBO.agregarCompra(compraDTO);

        compraBO.eliminarCompra(compraDTO.getId());

        CompraDTO resultado = compraBO.obtenerCompraPorId(compraDTO.getId());

        assertNull(resultado); 
    }
    
    @Test
    public void testEliminarCompra_Inexistente() throws PersistenciaException {
        CompraDTO compraDTO = new CompraDTO("Compra Existente", null);
        compraBO.agregarCompra(compraDTO);

        List<CompraDTO> comprasAntes = compraBO.obtenerTodasLasCompras();
        int cantidadAntes = comprasAntes.size();

        long idInexistente = 9999L;
        compraBO.eliminarCompra(idInexistente); 

        List<CompraDTO> comprasDespues = compraBO.obtenerTodasLasCompras();
        int cantidadDespues = comprasDespues.size();


        assertEquals(cantidadAntes, cantidadDespues);
    }
    
    @Test
    public void testAgregarObtenerYEliminarCompra() {
        // Agregar una compra
        CompraDTO compraDTO = new CompraDTO("Compra para Integración", null);
        compraBO.agregarCompra(compraDTO);

        // Obtener la compra recién agregada
        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();
        assertNotNull(compras);
        assertTrue(compras.stream().anyMatch(compra -> compra.getNombreCompra().equals("Compra para Integración")));

        // Eliminar la compra
        CompraDTO compraEliminada = compras.stream().filter(compra -> compra.getNombreCompra().equals("Compra para Integración")).findFirst().get();
        compraBO.eliminarCompra(compraEliminada.getId());

        // Verificar que la compra fue eliminada
        CompraDTO compraObtenida = compraBO.obtenerCompraPorId(compraEliminada.getId());
        assertNull(compraObtenida);
    }
    
    @Test
    public void testAgregarMultiplesComprasYVerificarEliminacion() {
        // Agregar varias compras
        CompraDTO compraDTO1 = new CompraDTO("Compra 1", null);
        CompraDTO compraDTO2 = new CompraDTO("Compra 2", null);
        compraBO.agregarCompra(compraDTO1);
        compraBO.agregarCompra(compraDTO2);

        // Obtener todas las compras y verificar que ambas fueron agregadas
        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();
        assertNotNull(compras);
        assertEquals(2, compras.size());

        // Eliminar una de las compras
        CompraDTO compraEliminar = compras.stream().filter(compra -> compra.getNombreCompra().equals("Compra 1")).findFirst().get();
        compraBO.eliminarCompra(compraEliminar.getId());

        // Verificar que la compra fue eliminada y la otra permanece
        compras = compraBO.obtenerTodasLasCompras();
        assertEquals(1, compras.size());
        assertTrue(compras.stream().anyMatch(compra -> compra.getNombreCompra().equals("Compra 2")));
    }
    
    @Test
    public void testAgregarYObtenerCompraPorNombre() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Cliente Prueba");
        ClienteBO clienteBO = new ClienteBO();
        clienteDTO = clienteBO.agregarCliente(clienteDTO); 
        
        // Agregar una compra
        CompraDTO compraDTO = new CompraDTO("Compra para Obtener", clienteDTO);
        compraBO.agregarCompra(compraDTO);

        // Obtener la compra por nombre
        CompraDTO resultado = compraBO.obtenerCompraPorNombreYCliente("Compra para Obtener", clienteDTO.getId());

        // Verificar que la compra fue obtenida correctamente
        assertNotNull(resultado);
        assertEquals("Compra para Obtener", resultado.getNombreCompra());
    }
    
    @Test
    public void testAgregarObtenerYEliminarTodasLasCompras() {
        // Agregar varias compras
        CompraDTO compraDTO1 = new CompraDTO("Compra A", null);
        CompraDTO compraDTO2 = new CompraDTO("Compra B", null);
        compraBO.agregarCompra(compraDTO1);
        compraBO.agregarCompra(compraDTO2);

        // Verificar que ambas compras fueron agregadas
        List<CompraDTO> compras = compraBO.obtenerTodasLasCompras();
        assertEquals(2, compras.size());

        // Eliminar todas las compras
        for (CompraDTO compra : compras) {
            compraBO.eliminarCompra(compra.getId());
        }

        // Verificar que no queda ninguna compra
        compras = compraBO.obtenerTodasLasCompras();
        assertTrue(compras.isEmpty());
    }

}
