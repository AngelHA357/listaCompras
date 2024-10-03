/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import BOs.IProductoBO;
import BOs.ProductoBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import DAOs.CompraDAO;
import DAOs.IClienteDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
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
public class ProductoBOTest {
    private IConexion conexion;
    private IProductoBO productoBO;
    
    public ProductoBOTest() {
    }
    
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
        conexion = Conexion.getInstance();
        productoBO = new ProductoBO();
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
    public void testAgregarProducto() {
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);

        productoBO.agregarProducto(productoDTO);

        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto de Prueba")));
    }
    
//    @Test
//    public void testObtenerProductoPorId() {
//        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
//        productoDTO = productoBO.agregarProducto(productoDTO);
//
//        List<ProductoDTO> productos = productoBO.obtenerTodosLosProductos();
//        Long id = productos.get(0).getId(); // Obtener el ID del producto agregado
//
//        ProductoDTO resultado = productoBO.obtenerProductoPorId(id);
//
//        assertNotNull(resultado);
//        assertEquals(id, resultado.getId());
//    }
}
