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
import Subsistemas.GestorClientes;
import Subsistemas.GestorCompras;
import Subsistemas.GestorProductos;
import Subsistemas.IGestorClientes;
import Subsistemas.IGestorCompras;
import Subsistemas.IGestorProductos;
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
public class GestorProductosIntegrationTest {

    private IGestorProductos gestorProductos;
    private IGestorCompras gestorCompras;
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
        conexion = Conexion.getInstance();
        gestorProductos = new GestorProductos();
        gestorCompras = new GestorCompras();
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
    public void testAgregarProducto() throws PersistenciaException, NegocioException {
        // Crear un producto de prueba
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Test",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );

        // Agregar el producto
        ProductoDTO resultadoDTO = gestorProductos.agregarProducto(productoDTO);

        // Verificar que se agregó correctamente
        assertNotNull(resultadoDTO, "El producto retornado no debe ser nulo");
        assertNotNull(resultadoDTO.getId(), "El producto debe tener un ID asignado");
        assertEquals("Producto Test", resultadoDTO.getNombre());
        assertEquals("Categoria Test", resultadoDTO.getCategoria());
        assertEquals(15.0, resultadoDTO.getCantidad());
        assertNotNull(resultadoDTO.getCompra(), "El producto debe tener una compra asociada");
        assertEquals(compraPrueba.getId(), resultadoDTO.getCompra().getId());
    }

    @Test
    public void testAgregarProducto_NombreNulo() {
        ProductoDTO productoDTO = new ProductoDTO(
                null,
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );

        assertThrows(NegocioException.class,
                () -> gestorProductos.agregarProducto(productoDTO),
                "Debe lanzar NegocioException cuando el nombre es nulo"
        );
    }

    @Test
    public void testAgregarProducto_CategoriaNula() {
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Test",
                null,
                false,
                compraPrueba,
                15.0
        );

        assertThrows(NegocioException.class,
                () -> gestorProductos.agregarProducto(productoDTO),
                "Debe lanzar NegocioException cuando la categoría es nula"
        );
    }

    @Test
    public void testAgregarProducto_CantidadNula() {
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Test",
                "Categoria Test",
                false,
                compraPrueba,
                null
        );

        assertThrows(NegocioException.class,
                () -> gestorProductos.agregarProducto(productoDTO),
                "Debe lanzar NegocioException cuando la cantidad es nula"
        );
    }

    @Test
    public void testAgregarProducto_CompraNula() {
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Test",
                "Categoria Test",
                false,
                null,
                15.0
        );

        assertThrows(NegocioException.class,
                () -> gestorProductos.agregarProducto(productoDTO),
                "Debe lanzar NegocioException cuando la compra es nula"
        );
    }

    @Test
    public void testObtenerProductoPorId() throws PersistenciaException, NegocioException {
        // Primero agregar un producto
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Test",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);
        assertNotNull(productoAgregado.getId(), "El producto debe haberse creado con un ID");

        // Obtener el producto por ID
        ProductoDTO resultado = gestorProductos.obtenerProductoPorId(productoAgregado.getId());

        // Verificaciones
        assertNotNull(resultado, "El producto recuperado no debe ser nulo");
        assertEquals(productoAgregado.getId(), resultado.getId());
        assertEquals("Producto Test", resultado.getNombre());
        assertNotNull(resultado.getCompra(), "El producto debe tener una compra asociada");
        assertEquals(compraPrueba.getId(), resultado.getCompra().getId());
    }

    @Test
    public void testObtenerProductoPorId_Inexistente() {
        Long idInexistente = 9999L;

        assertThrows(NegocioException.class,
                () -> gestorProductos.obtenerProductoPorId(idInexistente),
                "Debe lanzar NegocioException cuando el producto no existe"
        );
    }

    @Test
    public void testActualizarProducto() throws PersistenciaException, NegocioException {
        // Primero agregar un producto
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Original",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        // Modificar el producto
        productoAgregado.setNombre("Producto Actualizado");
        productoAgregado.setCantidad(20.0);

        // Actualizar el producto
        ProductoDTO productoActualizado = gestorProductos.actualizarProducto(productoAgregado);

        // Verificaciones
        assertNotNull(productoActualizado);
        assertEquals("Producto Actualizado", productoActualizado.getNombre());
        assertEquals(20.0, productoActualizado.getCantidad());
        assertEquals(compraPrueba.getId(), productoActualizado.getCompra().getId());
    }

    @Test
    public void testActualizarProducto_Inexistente() {
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Inexistente",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );
        productoDTO.setId(9999L); // ID inexistente

        assertThrows(NegocioException.class,
                () -> gestorProductos.actualizarProducto(productoDTO),
                "Debe lanzar NegocioException cuando se intenta actualizar un producto inexistente"
        );
    }

    @Test
    public void testEliminarProducto() throws PersistenciaException, NegocioException {
        // Agregar un producto
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto a Eliminar",
                "Categoria Test",
                false,
                compraPrueba,
                15.0
        );
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        // Eliminar el producto
        gestorProductos.eliminarProducto(productoAgregado.getId());

        // Verificar que el producto fue eliminado
        assertThrows(NegocioException.class,
                () -> gestorProductos.obtenerProductoPorId(productoAgregado.getId()),
                "Debe lanzar NegocioException al intentar obtener un producto eliminado"
        );
    }

    @Test
    public void testEliminarProducto_Inexistente() {
        Long idInexistente = 9999L;

        assertThrows(NegocioException.class,
                () -> gestorProductos.eliminarProducto(idInexistente),
                "Debe lanzar NegocioException al intentar eliminar un producto inexistente"
        );
    }

    @Test
    public void testObtenerProductoPorCaracteristicas() throws PersistenciaException, NegocioException {
        // Agregar un producto con características específicas
        ProductoDTO productoDTO = new ProductoDTO(
                "Producto Específico",
                "Categoria Específica",
                true,
                compraPrueba,
                25.0
        );
        ProductoDTO productoAgregado = gestorProductos.agregarProducto(productoDTO);

        // Buscar el producto por sus características
        ProductoDTO resultado = gestorProductos.obtenerProductoPorCaracteristicas(
                "Producto Específico",
                "Categoria Específica",
                true,
                25.0,
                compraPrueba.getId()
        );

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(productoAgregado.getId(), resultado.getId());
        assertEquals("Producto Específico", resultado.getNombre());
        assertEquals("Categoria Específica", resultado.getCategoria());
        assertEquals(25.0, resultado.getCantidad());
        assertTrue(resultado.isComprado());
        assertEquals(compraPrueba.getId(), resultado.getCompra().getId());
    }

    @Test
    public void testObtenerProductoPorCaracteristicas_NoEncontrado() {
        assertThrows(NegocioException.class,
                () -> gestorProductos.obtenerProductoPorCaracteristicas(
                        "Producto Inexistente",
                        "Categoria Inexistente",
                        false,
                        10.0,
                        compraPrueba.getId()
                ),
                "Debe lanzar NegocioException cuando no se encuentra el producto con las características especificadas"
        );
    }
}
