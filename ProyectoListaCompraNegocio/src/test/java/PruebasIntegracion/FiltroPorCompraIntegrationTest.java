/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import Conexion.Conexion;
import Conexion.IConexion;
import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.FiltroPorCompra;
import Subsistemas.GestorClientes;
import Subsistemas.GestorCompras;
import Subsistemas.GestorProductos;
import Subsistemas.IFiltroPorCompra;
import Subsistemas.IGestorClientes;
import Subsistemas.IGestorCompras;
import Subsistemas.IGestorProductos;
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
public class FiltroPorCompraIntegrationTest {

    private IFiltroPorCompra filtroCompra;
    private IGestorCompras gestorCompras;
    private IGestorProductos gestorProductos;
    private IGestorClientes gestorClientes;
    private IConexion conexion;
    private ClienteDTO clientePrueba;
    private CompraDTO compraPrueba;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("modoPrueba", "true");
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty("modoPrueba");
    }

    @BeforeEach
    public void setUp() throws PersistenciaException, NegocioException {
        // Inicializar las implementaciones reales 
        conexion = Conexion.getInstance();
        filtroCompra = new FiltroPorCompra();
        gestorCompras = new GestorCompras();
        gestorProductos = new GestorProductos();
        gestorClientes = new GestorClientes();

        // Crear un cliente para las pruebas
        ClienteDTO clienteDTO = new ClienteDTO(
                "Victor Humberto",
                "Encinas",
                "Guzmán",
                "toribio_test",
                "ABCD1234"
        );
        clientePrueba = gestorClientes.agregarCliente(clienteDTO);
        assertNotNull(clientePrueba.getId(), "El cliente debe haberse creado con un ID");

        // Crear una compra para las pruebas
        CompraDTO compraDTO = new CompraDTO("Compra Test", clientePrueba);
        compraPrueba = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraPrueba.getId(), "La compra debe haberse creado con un ID");
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {
        if (conexion != null) {
            conexion.rollback();
        }
    }

    @Test
    public void testObtenerProductosFiltrarPorCompra() throws PersistenciaException, NegocioException {
        // Crear y agregar varios productos asociados a la compra
        ProductoDTO producto1 = new ProductoDTO(
                "Producto Test A",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );
        ProductoDTO producto2 = new ProductoDTO(
                "Producto Test B",
                "Categoria Test",
                false,
                compraPrueba,
                20.0
        );

        // Agregar los productos y verificar que se crearon correctamente
        ProductoDTO productoGuardado1 = gestorProductos.agregarProducto(producto1);
        ProductoDTO productoGuardado2 = gestorProductos.agregarProducto(producto2);

        assertNotNull(productoGuardado1.getId(), "El producto 1 debe haberse creado con un ID");
        assertNotNull(productoGuardado2.getId(), "El producto 2 debe haberse creado con un ID");

        // Obtener los productos filtrados por compra
        List<ProductoDTO> resultados = filtroCompra.obtenerProductosFiltrarPorCompra(compraPrueba.getId());

        // Verificar resultados
        assertNotNull(resultados, "La lista de resultados no debe ser nula");
        assertEquals(2, resultados.size(), "Deben haber 2 productos en la compra");

        // Verificar que todos los productos pertenecen a la compra correcta
        assertTrue(resultados.stream()
                .allMatch(p -> p.getCompra().getId().equals(compraPrueba.getId())),
                "Todos los productos deben pertenecer a la compra de prueba");

        // Verificar que los productos tienen todos sus datos
        for (ProductoDTO producto : resultados) {
            assertNotNull(producto.getId(), "Cada producto debe tener ID");
            assertNotNull(producto.getNombre(), "Cada producto debe tener nombre");
            assertNotNull(producto.getCategoria(), "Cada producto debe tener categoría");
            assertNotNull(producto.getCantidad(), "Cada producto debe tener cantidad");
            assertNotNull(producto.getCompra(), "Cada producto debe tener compra asociada");
            assertNotNull(producto.getCompra().getCliente(), "La compra debe tener cliente asociado");
        }
    }

    @Test
    public void testObtenerProductosFiltrarPorCompra_CompraInexistente() {
        Long idCompraInexistente = 9999L;

        assertThrows(NegocioException.class,
                () -> filtroCompra.obtenerProductosFiltrarPorCompra(idCompraInexistente),
                "Debe lanzar NegocioException cuando la compra no existe"
        );
    }

    @Test
    public void testObtenerProductosFiltrarPorCompra_CompraSinProductos() throws PersistenciaException, NegocioException {
        // Crear una nueva compra sin productos
        CompraDTO nuevaCompra = new CompraDTO("Compra Sin Productos", clientePrueba);
        CompraDTO compraGuardada = gestorCompras.agregarCompra(nuevaCompra);

        // Intentar obtener productos de esta compra
        assertThrows(NegocioException.class,
                () -> filtroCompra.obtenerProductosFiltrarPorCompra(compraGuardada.getId()),
                "Debe lanzar NegocioException cuando la compra no tiene productos"
        );
    }

    @Test
    public void testObtenerProductosFiltrarPorCompra_IdCompraNull() {
        assertThrows(NegocioException.class,
                () -> filtroCompra.obtenerProductosFiltrarPorCompra(null),
                "Debe lanzar NegocioException cuando el ID de compra es null"
        );
    }

    @Test
    public void testObtenerProductosFiltrarPorCompra_MultiplesCategorias() throws PersistenciaException, NegocioException {
        // Crear productos de diferentes categorías en la misma compra
        ProductoDTO producto1 = new ProductoDTO(
                "Producto Test A",
                "Categoria 1",
                false,
                compraPrueba,
                15.0
        );
        ProductoDTO producto2 = new ProductoDTO(
                "Producto Test B",
                "Categoria 2",
                false,
                compraPrueba,
                20.0
        );
        ProductoDTO producto3 = new ProductoDTO(
                "Producto Test C",
                "Categoria 1",
                false,
                compraPrueba,
                25.0
        );

        // Agregar los productos
        gestorProductos.agregarProducto(producto1);
        gestorProductos.agregarProducto(producto2);
        gestorProductos.agregarProducto(producto3);

        // Obtener todos los productos de la compra
        List<ProductoDTO> resultados = filtroCompra.obtenerProductosFiltrarPorCompra(compraPrueba.getId());

        // Verificaciones
        assertNotNull(resultados, "La lista de resultados no debe ser nula");
        assertEquals(3, resultados.size(), "Deben haber 3 productos en total");

        // Verificar que hay productos de diferentes categorías
        long categoriasUnicas = resultados.stream()
                .map(ProductoDTO::getCategoria)
                .distinct()
                .count();
        assertEquals(2, categoriasUnicas, "Debe haber productos de 2 categorías diferentes");

        // Verificar que todos los productos pertenecen a la misma compra
        assertTrue(resultados.stream()
                .allMatch(p -> p.getCompra().getId().equals(compraPrueba.getId())),
                "Todos los productos deben pertenecer a la misma compra");
    }
}
