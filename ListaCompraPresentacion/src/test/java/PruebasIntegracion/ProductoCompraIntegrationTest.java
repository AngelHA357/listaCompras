/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import BOs.CompraBO;
import BOs.ICompraBO;
import BOs.IProductoBO;
import BOs.ProductoBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
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
public class ProductoCompraIntegrationTest {

    private ICompraBO compraBO;
    private IProductoBO productoBO;  
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
        productoBO = new ProductoBO();  
        compraBO = new CompraBO();
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

       
    }

    @Test
public void testAgregarProductoYCompra() {
        // Agregar un producto
        ProductoDTO productoDTO = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado = productoBO.agregarProducto(productoDTO);


        // Crear una compra con el producto
        CompraDTO compraDTO = new CompraDTO("Compra de Papeles", null);
        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
        assertNotNull(compraAgregada); // Asegurar que la compra se agregó correctamente

        // Agregar el producto a la compra
        productoAgregado.setCompraDTO(compraAgregada);
        productoAgregado = productoBO.actualizarProducto(productoAgregado);
        System.out.println(productoAgregado.getCompraDTO().getNombreCompra());
        assertNotNull(productoAgregado); // Asegurar que el producto se actualizó correctamente con la compra

        // Actualizar la lista de productos de la compra
        if (compraAgregada.getProductos() == null) {
            compraAgregada.setProductos(new ArrayList<>());
        }
        compraAgregada.getProductos().add(productoAgregado);
        compraAgregada = compraBO.actualizarCompra(compraAgregada);
        assertNotNull(compraAgregada); // Asegurar que la compra se actualizó correctamente con el producto

        // Verificar que el producto se haya agregado a la compra
        assertTrue(compraAgregada.getProductos().contains(productoAgregado));
    }
    
    
    
}
    