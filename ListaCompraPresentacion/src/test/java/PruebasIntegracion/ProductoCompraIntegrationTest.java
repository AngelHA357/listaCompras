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
import DTOs.ProductoDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.FiltroPorCategoria;
import Subsistemas.FiltroPorCompra;
import Subsistemas.GestorCompras;
import Subsistemas.GestorProductos;
import Subsistemas.IFiltroPorCategoria;
import Subsistemas.IFiltroPorCompra;
import Subsistemas.IGestorCompras;
import Subsistemas.IGestorProductos;
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

    private IGestorCompras gestorCompras;
    private IGestorProductos gestorProductos;
    private IFiltroPorCompra filtroPorCompra;
    private IFiltroPorCategoria filtroPorCategoria;

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
        gestorCompras = new GestorCompras();
        gestorProductos = new GestorProductos();
        filtroPorCompra = new FiltroPorCompra();
        filtroPorCategoria = new FiltroPorCategoria();
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        limpiarBaseDeDatos();
    }

    /**
     * Limpia la base de datos eliminando todos los productos, compras y
     * clientes.
     *
     * @throws PersistenciaException Si ocurre un error al acceder a la base de
     * datos.
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
     * Prueba que permite agregar un producto y asociarlo a una compra.
     *
     * Se verifica que la compra y el producto sean correctamente agregados y
     * asociados entre sí.
     */
    @Test
    public void testAgregarProductoYCompra() throws NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        CompraDTO compraDTO = new CompraDTO("Compra de Papeles", null);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);

        compraAgregada.setProductos(new ArrayList<>());
        compraAgregada.getProductos().add(productoAgregado);

        productoAgregado.setCompraDTO(compraAgregada);
        productoAgregado = gestorProductos.actualizarProducto(productoAgregado);

        compraAgregada = gestorCompras.actualizarCompra(compraAgregada);
        assertNotNull(compraAgregada);
        assertTrue(compraAgregada.getProductos().get(0).getNombre().equals(productoAgregado.getNombre()));
    }

    /**
     * Prueba que permite obtener los productos asociados a una compra
     * específica.
     *
     * Se asegura que los productos agregados estén correctamente asociados a la
     * compra y se verifica que la cantidad de productos sea la esperada.
     */
    @Test
    public void testObtenerProductosPorCompra() throws NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Jabón", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        CompraDTO compraDTO = new CompraDTO("Compra de Jabones", null);
        compraDTO.setProductos(new ArrayList<>());
        compraDTO.getProductos().add(productoAgregado);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada);

        List<ProductoDTO> productos = compraAgregada.getProductos();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(productoAgregado.getId(), productos.get(0).getId());
    }

    /**
     * Prueba que permite actualizar un producto y verificar que la
     * actualización se refleje correctamente en las compras asociadas.
     *
     * Se asegura que el nombre del producto actualizado sea correctamente
     * modificado en las compras que lo contienen.
     */
    @Test
    public void testActualizarProductoYVerificarEnCompras() throws NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Jabón de barra", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        CompraDTO compraDTO = new CompraDTO("Compra de Jabón", null);
        compraDTO.setProductos(new ArrayList<>());
        compraDTO.getProductos().add(productoAgregado);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada);

        productoAgregado.setNombre("Jabón Nuevo");
        ProductoDTO productoActualizado = gestorProductos.actualizarProducto(productoAgregado);
        assertEquals("Jabón Nuevo", productoActualizado.getNombre());

        CompraDTO compraObtenida = gestorCompras.obtenerCompraPorId(compraAgregada.getId());
        assertEquals("Jabón Nuevo", compraObtenida.getProductos().get(0).getNombre());
    }

    /**
     * Prueba que permite eliminar un producto y verificar que la eliminación se
     * refleje correctamente en las compras asociadas.
     *
     * Se asegura que el producto eliminado no esté presente en la lista de
     * productos de la compra.
     */
    @Test
    public void testEliminarProductoYVerificarEnCompras() throws NegocioException {
        ProductoDTO productoDTO = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        CompraDTO compraDTO = new CompraDTO("Compra de Papeles", null);
        compraDTO.setProductos(new ArrayList<>());
        compraDTO.getProductos().add(productoAgregado);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada);

        List<ProductoDTO> productos = compraAgregada.getProductos();
        List<ProductoDTO> productosAEliminar = new ArrayList<>();

        for (ProductoDTO producto : productos) {
            if (producto.getId().equals(productoAgregado.getId())) {
                productosAEliminar.add(producto);
            }
        }

        productos.removeAll(productosAEliminar);

        gestorCompras.actualizarCompra(compraAgregada);
        gestorProductos.eliminarProducto(productoAgregado.getId());

        CompraDTO compraResultado = gestorCompras.obtenerCompraPorId(compraAgregada.getId());
        assertFalse(compraResultado.getProductos().stream()
                .anyMatch(producto -> producto.getId().equals(productoAgregado.getId())));
    }

    /**
     * Prueba que permite obtener los productos asociados a una compra por su
     * ID.
     *
     * Se asegura que todos los productos agregados a la compra estén
     * correctamente asociados a la misma.
     */
    @Test
    public void testObtenerProductosPorCompraId() throws NegocioException {
        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        
        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, compraAgregada, 6.0);
        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, compraAgregada, 6.0);
        gestorProductos.agregarProducto(producto1);
        gestorProductos.agregarProducto(producto2);
        
       
        List<ProductoDTO> productos = filtroPorCompra.obtenerProductosFiltrarPorCompra(compraAgregada.getId());

        assertNotNull(productos);
        assertEquals(2, productos.size());
    }

    /**
     * Prueba que permite cambiar el estado de un producto a "comprado" y
     * verificar que se refleje correctamente en las compras asociadas.
     *
     * Se asegura que el estado del producto cambie correctamente y que el
     * producto eliminado no aparezca en las compras.
     */
    @Test
    public void testCambiarEstadoProducto() throws NegocioException {
        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, null, 6.0);
        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, null, 6.0);
        ProductoDTO productoAgregado1 = gestorProductos.agregarProducto(producto1);
        ProductoDTO productoAgregado2 = gestorProductos.agregarProducto(producto2);
        assertNotNull(productoAgregado1);
        assertNotNull(productoAgregado2);

        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
        compraDTO.setProductos(new ArrayList<>());
        compraDTO.getProductos().add(productoAgregado1);
        compraDTO.getProductos().add(productoAgregado2);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada);

        productoAgregado1.setComprado(true);
        gestorProductos.actualizarProducto(productoAgregado1);

        gestorProductos.eliminarProducto(productoAgregado2.getId());

        CompraDTO compraObtenida = gestorCompras.obtenerCompraPorId(compraAgregada.getId());
        assertEquals(true, compraObtenida.getProductos().get(0).isComprado());
    }

    /**
     * Permite que se comprueben que articulos añadidos, editados o eliminados
     * se actualicen correctamente al filtrarlos por categorías.
     *
     * Se asegura que el producto cambie correctamente y que el producto
     * eliminado no aparezca en las compras por categoria.
     *
     */
    @Test
    public void testComprobacionProductosListaComprasYCategorias() throws NegocioException {
        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, compraAgregada, 6.0);
        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, compraAgregada, 6.0);

        gestorProductos.agregarProducto(producto1);
        ProductoDTO productoAgregado2 = gestorProductos.agregarProducto(producto2);
        
        producto1 = gestorProductos.obtenerProductoPorCaracteristicas(producto1.getNombre(), producto1.getCategoria(), producto1.isComprado()
                , producto1.getCantidad() ,producto1.getCompra().getId());

        producto1.setNombre("Papel Higiénico");
        gestorProductos.actualizarProducto(producto1);

        gestorProductos.eliminarProducto(productoAgregado2.getId());

        List<ProductoDTO> productosCategoria = filtroPorCategoria.filtrarPorCategoriaYCompraId("Higiene Personal", producto1.getCompra().getId());
        assertTrue(productosCategoria.size() == 1);
        
        
        assertTrue(productosCategoria.stream()
            .anyMatch(p -> p.getNombre().equals("Papel Higiénico")));

        assertFalse(productosCategoria.stream()
                .anyMatch(p -> p.getId().equals(productoAgregado2.getId())));
    }

    /**
     * Permite validar que los articulos marcados como comprados se muestran en
     * la categoría correspondiente y cambian de estado.
     *
     * Se asegura que el estado del producto cambie correctamente y que se
     * muestre correctamente por categoría.
     */
    @Test
    public void testValidarArticulosMarcadosComoComprados() throws NegocioException {
        CompraDTO compraDTO = new CompraDTO("Compra Variada", null);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        ProductoDTO producto1 = new ProductoDTO("Papel", "Higiene Personal", false, compraAgregada, 6.0);
        ProductoDTO producto2 = new ProductoDTO("Jabón", "Higiene Personal", false, compraAgregada, 6.0);
        
        gestorProductos.agregarProducto(producto1);
        gestorProductos.agregarProducto(producto2);
        producto1 = gestorProductos.obtenerProductoPorCaracteristicas(producto1.getNombre(), producto1.getCategoria(), producto1.isComprado()
                , producto1.getCantidad() ,producto1.getCompra().getId());
        
        producto1.setComprado(true);
        gestorProductos.actualizarProducto(producto1);

        List<ProductoDTO> productosCategoria = filtroPorCategoria.filtrarPorCategoriaYCompraId("Higiene Personal", producto1.getCompra().getId());
        assertTrue(productosCategoria.size() > 0);

        assertTrue(productosCategoria.stream()
                .anyMatch(p -> p.getNombre().equals("Papel") && p.isComprado()));
    }

   
}
