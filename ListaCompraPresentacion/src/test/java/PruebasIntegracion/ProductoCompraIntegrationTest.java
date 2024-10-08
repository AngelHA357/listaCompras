///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
// */
//package PruebasIntegracion;
//
//import BOs.CompraBO;
//import BOs.ICompraBO;
//import BOs.IProductoBO;
//import BOs.ProductoBO;
//import Conexion.Conexion;
//import Conexion.IConexion;
//import DAOs.CompraDAO;
//import DAOs.ICompraDAO;
//import DAOs.IProductoDAO;
//import DAOs.ProductoDAO;
//import DTOs.CompraDTO;
//import DTOs.ProductoDTO;
//import Entidades.Compra;
//import Entidades.Producto;
//import Exceptions.PersistenciaException;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// *
// * @author JoseH
// */
//public class ProductoCompraIntegrationTest {
//
//    private ICompraBO compraBO;
//    private IProductoBO productoBO;  
//    private ICompraDAO compraDAO;
//    private IConexion conexion;
//
//    public ProductoCompraIntegrationTest() {
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        System.setProperty("modoPrueba", "true");
//    }
//
//    @AfterAll
//    public static void tearDownClass() {
//        System.clearProperty("modoPrueba");
//    }
//
//    @BeforeEach
//    public void setUp() {
//        conexion = Conexion.getInstance();
//        productoBO = new ProductoBO();  
//        compraBO = new CompraBO();
//        compraDAO = new CompraDAO(conexion);
//    }
//
//    @AfterEach
//    public void tearDown() throws PersistenciaException {
//        limpiarBaseDeDatos();
//    }
//    
//    private void limpiarBaseDeDatos() throws PersistenciaException {
//         // Obtener todos los productos y eliminarlos
//        IProductoDAO productoDAO = new ProductoDAO(conexion);
//        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
//        for (Producto producto : productos) {
//            productoDAO.eliminarProducto(producto.getId());
//        }
//        
//        // Obtener todas las compras y eliminarlas
//        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
//        for (Compra compra : compras) {
//            compraDAO.eliminarCompra(compra.getId());
//        }
//
//       
//    }
//
//    @Test
//    public void testAgregarProductoYCompra() {
//        // Agregar un producto
//        ProductoDTO productoDTO = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
//        ProductoDTO productoAgregado = productoBO.agregarProducto(productoDTO);
//
//        // Crear una compra y asignar el producto
//        CompraDTO compraDTO = new CompraDTO("Compra de Papeles", null);
//        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
//        
//        compraAgregada.setProductos(new ArrayList<>());
//
//        // Agregar el producto a la lista de la compra
//        compraAgregada.getProductos().add(productoAgregado);
//
//        productoAgregado.setCompraDTO(compraAgregada);
//        productoAgregado = productoBO.actualizarProducto(productoAgregado);
//
//        compraAgregada = compraBO.actualizarCompra(compraAgregada);
//        assertNotNull(compraAgregada);
//
//        // Verificar que la compra contiene el producto agregado
//        assertTrue(compraAgregada.getProductos().getFirst().getNombre().equals(productoAgregado.getNombre()));
//    }
//
//    @Test
//    public void testObtenerProductosPorCompra() {
//        // Agregar un producto
//        ProductoDTO productoDTO = new ProductoDTO("Jabón", "Higiene Personal", false, null, 6.0);
//        ProductoDTO productoAgregado = productoBO.agregarProducto(productoDTO);
//
//        // Crear una compra y agregar el producto
//        CompraDTO compraDTO = new CompraDTO("Compra de Jabones", null);
//        compraDTO.setProductos(new ArrayList<>());
//        compraDTO.getProductos().add(productoAgregado);
//        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
//        assertNotNull(compraAgregada);
//
//        // Obtener los productos de la compra
//        List<ProductoDTO> productos = compraAgregada.getProductos();
//
//        // Verificar los productos asociados
//        assertNotNull(productos);
//        assertEquals(1, productos.size());
//        assertEquals(productoAgregado.getId(), productos.get(0).getId());
//    }
//
//    @Test
//    public void testActualizarProductoYVerificarEnCompras() {
//        // Agregar un producto
//        ProductoDTO productoDTO = new ProductoDTO("Jabón de barra", "Higiene Personal", false, null, 6.0);
//        ProductoDTO productoAgregado = productoBO.agregarProducto(productoDTO);
//
//        // Crear una compra y agregar el producto
//        CompraDTO compraDTO = new CompraDTO("Compra de Jabón", null);
//        compraDTO.setProductos(new ArrayList<>());
//        compraDTO.getProductos().add(productoAgregado);
//        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
//        assertNotNull(compraAgregada);
//
//        // Actualizar el producto
//        productoAgregado.setNombre("Jabón Nuevo");
//        ProductoDTO productoActualizado = productoBO.actualizarProducto(productoAgregado);
//        assertEquals("Jabón Nuevo", productoActualizado.getNombre());
//
//        // Verificar que la compra contiene el producto actualizado
//        CompraDTO compraObtenida = compraBO.obtenerCompraPorId(compraAgregada.getId());
//        assertEquals("Jabón Nuevo", compraObtenida.getProductos().get(0).getNombre());
//    }
//
//    @Test
//    public void testEliminarProductoYVerificarEnCompras() {
//        // Agregar un producto
//        ProductoDTO productoDTO = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
//        ProductoDTO productoAgregado = productoBO.agregarProducto(productoDTO);
//
//        // Crear una compra y agregar el producto
//        CompraDTO compraDTO = new CompraDTO("Compra de Papeles", null);
//        compraDTO.setProductos(new ArrayList<>());
//        compraDTO.getProductos().add(productoAgregado);
//        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
//        assertNotNull(compraAgregada);
//
//        // Obtener la lista de productos de la compra
//        List<ProductoDTO> productos = compraAgregada.getProductos();
//
//        // Crear una lista temporal para almacenar los productos a eliminar
//        List<ProductoDTO> productosAEliminar = new ArrayList<>();
//
//        for (ProductoDTO producto : productos) {
//            if (producto.getId().equals(productoAgregado.getId())) {
//                productosAEliminar.add(producto);
//            }
//        }
//
//        // Eliminar los productos que se encuentran en la lista de eliminación de la lista original
//        productos.removeAll(productosAEliminar);
//
//        compraBO.actualizarCompra(compraAgregada);
//        productoBO.eliminarProducto(productoAgregado.getId());
//        
//
//        // Verificar que el producto no esté en la compra
//        CompraDTO compraResultado = compraBO.obtenerCompraPorId(compraAgregada.getId());
//        assertFalse(compraResultado.getProductos().stream()
//                .anyMatch(producto -> producto.getId().equals(productoAgregado.getId())));
//    }
//
////    @Test
////    public void testObtenerProductosPorCompraId() {
////        // Crear una compra
////        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
////        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
////        assertNotNull(compraAgregada); // Verificar que la compra fue agregada correctamente
////
////        // Agregar productos a la compra
////        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, compraAgregada, 6.0);
////        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, compraAgregada, 6.0);
////
////        producto1.setCompraDTO(compraDTO);
////        producto2.setCompraDTO(compraDTO);
////        
////        producto1 = productoBO.agregarProducto(producto1);
////        producto2 = productoBO.agregarProducto(producto2);
////
////        // Actualizar la compra con los productos
////        compraAgregada.setProductos(new ArrayList<>());
////        compraAgregada.getProductos().add(producto1);
////        compraAgregada.getProductos().add(producto2);
////
////        compraAgregada = compraBO.actualizarCompra(compraAgregada);
////        productoBO.actualizarProducto(producto1);
////        productoBO.actualizarProducto(producto2);
////
////        // Obtener los productos por ID de compra
////        List<ProductoDTO> productos = productoBO.filtrarPorCompra(compraAgregada.getId());
////
////        // Verificar los productos
////        assertNotNull(productos);
////        assertEquals(2, productos.size());
////        Long compraId = compraAgregada.getId();
////        assertTrue(productos.stream().allMatch(p -> p.getCompraDTO().getId().equals(compraId)));
////    }
//
////    @Test
////    public void testConsistenciaDeEstado() {
////        // Agregar productos
////        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
////        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, null, 6.0);
////        ProductoDTO productoAgregado1 = productoBO.agregarProducto(producto1);
////        ProductoDTO productoAgregado2 = productoBO.agregarProducto(producto2);
////        assertNotNull(productoAgregado1);
////        assertNotNull(productoAgregado2);
////
////        // Crear compra y agregar productos
////        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
////        compraDTO.setProductos(new ArrayList<>());
////        compraDTO.getProductos().add(productoAgregado1);
////        compraDTO.getProductos().add(productoAgregado2);
////        CompraDTO compraAgregada = compraBO.agregarCompra(compraDTO);
////        assertNotNull(compraAgregada);
////
////        // Actualizar el producto
////        productoAgregado1.setNombre("Papel Nuevo");
////        productoBO.actualizarProducto(productoAgregado1);
////
////        // Eliminar el segundo producto
////        productoBO.eliminarProducto(productoAgregado2.getId());
////
////        // Verificar el estado final
////        CompraDTO compraObtenida = compraBO.obtenerCompraPorId(compraAgregada.getId());
////        assertEquals("Papel Nuevo", compraObtenida.getProductos().get(0).getNombre());
////        assertFalse(compraObtenida.getProductos().stream()
////                .anyMatch(p -> p.getId().equals(productoAgregado2.getId())));
////    }
//    
//    
//    
//}
//    