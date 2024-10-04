/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import BOs.CompraBO;
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
import DTOs.CompraDTO;
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
    public void setUp() throws PersistenciaException {
        conexion = Conexion.getInstance();
        productoBO = new ProductoBO();
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
//    public void testAgregarProductoConDatosNulos() {
//        ProductoDTO productoDTO = new ProductoDTO(null, null, false, null, 0.0);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            productoBO.agregarProducto(productoDTO);
//        });
//
//        assertEquals("Los datos del producto no pueden ser nulos excepto la categoría", exception.getMessage());
//    }
    
    @Test
    public void testActualizarProducto() {
        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
        productoDTO = productoBO.agregarProducto(productoDTO);

        assertNotNull(productoDTO);
        assertNotNull(productoDTO.getId()); 

        productoDTO = productoBO.obtenerProductoPorId(productoDTO.getId());

        productoDTO.setNombre("Producto Actualizado");

        ProductoDTO resultado = productoBO.actualizarProducto(productoDTO);

        assertNotNull(resultado); 
        assertEquals(productoDTO.getId(), resultado.getId());
        assertEquals("Producto Actualizado", resultado.getNombre()); 
        assertEquals(productoDTO.getCategoria(), resultado.getCategoria()); 
    }
    
    @Test
    public void testActualizarProductoQueNoExiste() {
        ProductoDTO productoDTO = new ProductoDTO("Producto Inexistente", "Categoria X", false, null, 5.0);

        ProductoDTO resultado = productoBO.actualizarProducto(productoDTO);

        assertNull(resultado); 
    }
    
    @Test
    public void testEliminarProducto() {
        ProductoDTO productoDTO = new ProductoDTO("Producto a Eliminar", "Categoria B", false, null, 10.0);
        productoDTO = productoBO.agregarProducto(productoDTO);

        assertNotNull(productoDTO);
        assertNotNull(productoDTO.getId());

        productoBO.eliminarProducto(productoDTO.getId());

        ProductoDTO productoEliminado = productoBO.obtenerProductoPorId(productoDTO.getId());
        assertNull(productoEliminado);
    }
    
    @Test
    public void testEliminarProductoQueNoExiste() {
        Long productoIdInexistente = 999L;
        productoBO.eliminarProducto(productoIdInexistente);

        ProductoDTO productoEliminado = productoBO.obtenerProductoPorId(productoIdInexistente);
        assertNull(productoEliminado);
    }
    
    @Test
    public void testObtenerProductoPorId() {
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
        productoDTO = productoBO.agregarProducto(productoDTO);

        ProductoDTO resultado = productoBO.obtenerProductoPorId(productoDTO.getId());

        assertNotNull(resultado);
        assertEquals(productoDTO.getId(), resultado.getId());
    }
    
    @Test
    public void testObtenerProductoPorIdInexistente() {
        Long productoIdInexistente = 999L; 

        ProductoDTO resultado = productoBO.obtenerProductoPorId(productoIdInexistente);

        assertNull(resultado);
    }
    
    @Test
    public void testFiltrarPorCategoriaYCompraId() {
        CompraDTO compraDTO1 = new CompraDTO("Compra 1", null);
        CompraDTO compraDTO2 = new CompraDTO("Compra 2", null);
        CompraBO compraBO = new CompraBO();
        compraDTO1 = compraBO.agregarCompra(compraDTO1);
        compraDTO2 = compraBO.agregarCompra(compraDTO2);
        
        ProductoDTO productoDTO1 = new ProductoDTO("Producto A", "Categoria C", false, compraDTO1, 15.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto B", "Categoria C", false, compraDTO2, 20.0);
        productoBO.agregarProducto(productoDTO1);
        productoBO.agregarProducto(productoDTO2);

        List<ProductoDTO> resultado = productoBO.filtrarPorCategoriaYCompraId("Categoria C", compraDTO1.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto A", resultado.get(0).getNombre());
    }
    
    @Test
    public void testFiltrarPorCategoriaYCompraIdSinResultados() {
        // Intentar filtrar sin productos asociados
        List<ProductoDTO> resultado = productoBO.filtrarPorCategoriaYCompraId("Categoria No Existente", 999L); 

        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); 
    }
    
    
    
    @Test
    public void testFiltrarPorCompra() {
        CompraDTO compraDTO = new CompraDTO("Compra", null);
        CompraBO compraBO = new CompraBO();
        compraDTO = compraBO.agregarCompra(compraDTO);
        ProductoDTO productoDTO1 = new ProductoDTO("Producto C", "Categoria D", false, compraDTO, 25.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto D", "Categoria D", false, compraDTO, 30.0);
        productoBO.agregarProducto(productoDTO1);
        productoBO.agregarProducto(productoDTO2);

        List<ProductoDTO> resultado = productoBO.filtrarPorCompra(compraDTO.getId());

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto C")));
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto D")));
    }
    
    @Test
    public void testFiltrarPorCompraInexistente() {
        Long compraIdInexistente = 999L; 

        List<ProductoDTO> resultado = productoBO.filtrarPorCompra(compraIdInexistente);

        assertNotNull(resultado); 
        assertEquals(0, resultado.size()); 
    }
    
    @Test
    public void testObtenerProductoPorCaracteristicas() {
        CompraDTO compraDTO = new CompraDTO("Compra", null);
        CompraBO compraBO = new CompraBO();
        compraDTO = compraBO.agregarCompra(compraDTO);
        ProductoDTO productoDTO = new ProductoDTO("Producto E", "Categoria E", true, compraDTO, 35.0);
        productoBO.agregarProducto(productoDTO);

        ProductoDTO resultado = productoBO.obtenerProductoPorCaracteristicas("Producto E", "Categoria E", true, 35.0, compraDTO.getId());

        assertNotNull(resultado);
        assertEquals("Producto E", resultado.getNombre());
        assertEquals("Categoria E", resultado.getCategoria());
        assertEquals(35.0, resultado.getCantidad());
    }
    
    @Test
    public void testObtenerProductoPorCaracteristicasInexistentes() {
        ProductoDTO resultado = productoBO.obtenerProductoPorCaracteristicas("Producto Inexistente", "Categoria Inexistente", true, 100.0, 999L);

        assertNull(resultado); 
    }
    
    public void testObtenerTodosLosProductos() {
        ProductoDTO productoDTO1 = new ProductoDTO("Producto F", "Categoria F", false, null, 40.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto G", "Categoria G", false, null, 45.0);
        productoBO.agregarProducto(productoDTO1);
        productoBO.agregarProducto(productoDTO2);

        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto F")));
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto G")));
    }
    
    @Test
    public void testObtenerTodosLosProductosSinProductos() {
        // Obtener todos los productos sin agregar ninguno
        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); 
    }
    
    
    @Test
    public void testAgregarYObtenerProducto() {
        // Agregar un producto
        ProductoDTO productoDTO = new ProductoDTO("Producto de Prueba", "Categoria A", false, null, 5.0);
        productoDTO = productoBO.agregarProducto(productoDTO); // Asumimos que el método devuelve el producto agregado con su ID

        // Obtener todos los productos y verificar que el producto agregado está en la lista
        List<ProductoDTO> resultado = productoBO.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().anyMatch(producto -> producto.getNombre().equals("Producto de Prueba")));

        // Obtener el producto por ID y verificar que los datos son correctos
        ProductoDTO productoPorId = productoBO.obtenerProductoPorId(productoDTO.getId());

        assertNotNull(productoPorId);
        assertEquals(productoDTO.getId(), productoPorId.getId());
    }
   
    @Test
    public void testActualizarYObtenerProducto() {
        // Agregar un producto
        ProductoDTO productoDTO = new ProductoDTO("Producto Original", "Categoria A", false, null, 5.0);
        productoDTO = productoBO.agregarProducto(productoDTO);

        // Actualizar el producto
        productoDTO.setNombre("Producto Actualizado");
        ProductoDTO resultado = productoBO.actualizarProducto(productoDTO);

        // Verificar que se actualizó correctamente
        assertNotNull(resultado);
        assertEquals("Producto Actualizado", resultado.getNombre());

        // Obtener el producto actualizado por ID
        ProductoDTO productoActualizado = productoBO.obtenerProductoPorId(resultado.getId());
        assertNotNull(productoActualizado);
        assertEquals("Producto Actualizado", productoActualizado.getNombre());
    }
    
    @Test
    public void testEliminarYObtenerProducto() {
        // Agregar un producto
        ProductoDTO productoDTO = new ProductoDTO("Producto a Eliminar", "Categoria B", false, null, 10.0);
        productoDTO = productoBO.agregarProducto(productoDTO);

        // Eliminar el producto
        productoBO.eliminarProducto(productoDTO.getId());

        // Verificar que el producto fue eliminado
        ProductoDTO productoEliminado = productoBO.obtenerProductoPorId(productoDTO.getId());
        assertNull(productoEliminado);
    }

}
