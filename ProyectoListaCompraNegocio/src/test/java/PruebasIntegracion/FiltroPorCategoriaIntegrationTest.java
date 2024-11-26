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
import Subsistemas.FiltroPorCategoria;
import Subsistemas.GestorClientes;
import Subsistemas.GestorCompras;
import Subsistemas.GestorProductos;
import Subsistemas.IFiltroPorCategoria;
import Subsistemas.IGestorClientes;
import Subsistemas.IGestorCompras;
import Subsistemas.IGestorProductos;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FiltroPorCategoriaIntegrationTest {

    private IFiltroPorCategoria filtroProducto;
    private IGestorCompras gestorCompras;
    private IGestorProductos gestorProductos;
    private IGestorClientes gestorClientes;
    private IConexion conexion;
    private ClienteDTO clientePrueba;

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
        filtroProducto = new FiltroPorCategoria();
        gestorCompras = new GestorCompras();
        gestorProductos = new GestorProductos();
        gestorClientes = new GestorClientes();
        
        String usuarioUnico = "toribio_test_" + System.currentTimeMillis();

        // Crear un cliente para las pruebas
        ClienteDTO clienteDTO = new ClienteDTO(
                "Victor Humberto",
                "Encinas",
                "Guzmán",
                usuarioUnico,
                "ABCD1234"
        );
        clientePrueba = gestorClientes.agregarCliente(clienteDTO);
        assertNotNull(clientePrueba.getId(), "El cliente debe haberse creado con un ID");
    }

    @AfterEach
    public void tearDown() {
        try {
            if (conexion != null) {
                conexion.rollback();
            }
        } catch (PersistenciaException ex) {
            Logger.getLogger(FiltroPorCategoriaIntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testFiltrarPorCategoriaYCompraId() throws PersistenciaException, NegocioException {
        // Crear una compra real en la base de datos asociada al cliente
        CompraDTO compraDTO = new CompraDTO("Compra Test", clientePrueba);
        compraDTO = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraDTO.getId(), "La compra debe haberse creado con un ID");

        // Crear productos reales asociados a la compra
        ProductoDTO producto1 = new ProductoDTO("Producto Test A", "Categoria Test", false, compraDTO, 15.0);
        ProductoDTO producto2 = new ProductoDTO("Producto Test B", "Categoria Test", false, compraDTO, 20.0);
        ProductoDTO producto3 = new ProductoDTO("Producto Test C", "Otra Categoria", false, compraDTO, 25.0);

        // Agregar los productos y verificar que se crearon correctamente
        gestorProductos.agregarProducto(producto1);
        gestorProductos.agregarProducto(producto2);
        gestorProductos.agregarProducto(producto3);

        // Ejecutar el filtro
        List<ProductoDTO> resultados = filtroProducto.filtrarPorCategoriaYCompraId("Categoria Test", compraDTO.getId());
        Long idCompra = compraDTO.getId();

        // Verificar resultados
        assertNotNull(resultados, "La lista de resultados no debe ser nula");
        assertEquals(2, resultados.size(), "Deben haber 2 productos en la categoría test");
        assertTrue(resultados.stream().allMatch(p -> p.getCategoria().equals("Categoria Test")),
                "Todos los productos deben ser de la categoría test");
        assertTrue(resultados.stream().allMatch(p -> p.getCompra().getId().equals(idCompra)),
                "Todos los productos deben pertenecer a la compra creada");
    }

    @Test
    public void testFiltrarPorCategoriaYCompraId_SinResultados() throws PersistenciaException, NegocioException {
        // Crear una compra real en la base de datos asociada al cliente
        CompraDTO compraDTO = new CompraDTO("Compra Test Vacía", clientePrueba);
        compraDTO = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraDTO.getId(), "La compra debe haberse creado con un ID");

        // Ejecutar el filtro con una categoría que sabemos que no existe
        List<ProductoDTO> resultados = filtroProducto.filtrarPorCategoriaYCompraId("Categoria Inexistente", compraDTO.getId());

        // Verificar que la lista está vacía
        assertNotNull(resultados, "La lista de resultados no debe ser nula");
        assertTrue(resultados.isEmpty(), "La lista debe estar vacía para una categoría inexistente");
    }
}
