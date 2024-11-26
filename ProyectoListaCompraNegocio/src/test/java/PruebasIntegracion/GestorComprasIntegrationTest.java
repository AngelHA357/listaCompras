/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

import Conexion.Conexion;
import Conexion.IConexion;
import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorClientes;
import Subsistemas.GestorCompras;
import Subsistemas.IGestorClientes;
import Subsistemas.IGestorCompras;
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
public class GestorComprasIntegrationTest {

    private IGestorCompras gestorCompras;
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
        conexion = Conexion.getInstance();
        gestorCompras = new GestorCompras();
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
    public void tearDown() throws PersistenciaException {
        if (conexion != null) {
            conexion.rollback();
        }
    }

    @Test
    public void testAgregarCompra() throws PersistenciaException, NegocioException {
        // Crear una compra de prueba con cliente asociado
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", clientePrueba);

        // Agregar la compra
        CompraDTO resultadoDTO = gestorCompras.agregarCompra(compraDTO);

        // Verificar que se agregó correctamente
        assertNotNull(resultadoDTO, "La compra retornada no debe ser nula");
        assertNotNull(resultadoDTO.getId(), "La compra debe tener un ID asignado");
        assertEquals("Compra de Prueba", resultadoDTO.getNombreCompra());
        assertNotNull(resultadoDTO.getCliente(), "La compra debe tener un cliente asociado");
        assertEquals(clientePrueba.getId(), resultadoDTO.getCliente().getId(), "El cliente asociado debe ser el correcto");
    }

    @Test
    public void testAgregarCompra_NombreNulo() {
        // Crear una compra con nombre nulo pero cliente válido
        CompraDTO compraDTO = new CompraDTO(null, clientePrueba);

        assertThrows(NegocioException.class,
                () -> gestorCompras.agregarCompra(compraDTO),
                "Debe lanzar NegocioException cuando el nombre es nulo"
        );
    }

    @Test
    public void testAgregarCompra_ClienteNulo() {
        // Crear una compra con nombre válido pero cliente nulo
        CompraDTO compraDTO = new CompraDTO("Compra Test", null);

        assertThrows(NegocioException.class,
                () -> gestorCompras.agregarCompra(compraDTO),
                "Debe lanzar NegocioException cuando el cliente es nulo"
        );
    }

    @Test
    public void testObtenerCompraPorId() throws PersistenciaException, NegocioException {
        // Primero agregar una compra
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", clientePrueba);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada.getId(), "La compra debe haberse creado con un ID");

        // Obtener la compra por ID
        CompraDTO resultado = gestorCompras.obtenerCompraPorId(compraAgregada.getId());

        // Verificaciones
        assertNotNull(resultado, "La compra recuperada no debe ser nula");
        assertEquals(compraAgregada.getId(), resultado.getId());
        assertEquals("Compra de Prueba", resultado.getNombreCompra());
        assertNotNull(resultado.getCliente(), "La compra debe tener un cliente asociado");
        assertEquals(clientePrueba.getId(), resultado.getCliente().getId());
    }

    @Test
    public void testObtenerCompraPorId_Inexistente() {
        // Intentar obtener una compra con ID inexistente
        Long idInexistente = 9999L;

        assertThrows(NegocioException.class,
                () -> gestorCompras.obtenerCompraPorId(idInexistente),
                "Debe lanzar NegocioException cuando la compra no existe"
        );
    }

    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException, NegocioException {
        // Agregar varias compras
        CompraDTO compra1 = new CompraDTO("Compra 1", clientePrueba);
        CompraDTO compra2 = new CompraDTO("Compra 2", clientePrueba);

        CompraDTO compraAgregada1 = gestorCompras.agregarCompra(compra1);
        CompraDTO compraAgregada2 = gestorCompras.agregarCompra(compra2);

        assertNotNull(compraAgregada1.getId(), "La primera compra debe haberse creado con un ID");
        assertNotNull(compraAgregada2.getId(), "La segunda compra debe haberse creado con un ID");

        // Obtener todas las compras
        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        // Verificaciones
        assertNotNull(resultado, "La lista de compras no debe ser nula");
        assertTrue(resultado.size() >= 2, "Debe haber al menos 2 compras");
        assertTrue(resultado.stream().anyMatch(c -> c.getNombreCompra().equals("Compra 1")));
        assertTrue(resultado.stream().anyMatch(c -> c.getNombreCompra().equals("Compra 2")));

        // Verificar que todas las compras tienen cliente asociado
        assertTrue(resultado.stream().allMatch(c -> c.getCliente() != null),
                "Todas las compras deben tener un cliente asociado");
    }

    @Test
    public void testEliminarCompra() throws PersistenciaException, NegocioException {
        // Agregar una compra
        CompraDTO compraDTO = new CompraDTO("Compra a Eliminar", clientePrueba);
        CompraDTO compraAgregada = gestorCompras.agregarCompra(compraDTO);
        assertNotNull(compraAgregada.getId(), "La compra debe haberse creado con un ID");

        // Eliminar la compra
        gestorCompras.eliminarCompra(compraAgregada.getId());

        // Verificar que la compra fue eliminada
        assertThrows(NegocioException.class,
                () -> gestorCompras.obtenerCompraPorId(compraAgregada.getId()),
                "Debe lanzar NegocioException al intentar obtener una compra eliminada"
        );
    }

    @Test
    public void testEliminarCompra_Inexistente() {
        Long idInexistente = 9999L;

        assertThrows(NegocioException.class,
                () -> gestorCompras.eliminarCompra(idInexistente),
                "Debe lanzar NegocioException al intentar eliminar una compra inexistente"
        );
    }

    @Test
    public void testObtenerComprasPorCliente() throws PersistenciaException, NegocioException {
        // Agregar varias compras para el mismo cliente
        CompraDTO compra1 = new CompraDTO("Compra Cliente 1", clientePrueba);
        CompraDTO compra2 = new CompraDTO("Compra Cliente 2", clientePrueba);

        gestorCompras.agregarCompra(compra1);
        gestorCompras.agregarCompra(compra2);

        // Obtener las compras del cliente
        List<CompraDTO> comprasCliente = gestorCompras.obtenerComprasPorCliente(clientePrueba.getId());

        // Verificaciones
        assertNotNull(comprasCliente, "La lista de compras no debe ser nula");
        assertTrue(comprasCliente.size() >= 2, "El cliente debe tener al menos 2 compras");
        assertTrue(comprasCliente.stream()
                .allMatch(c -> c.getCliente().getId().equals(clientePrueba.getId())),
                "Todas las compras deben pertenecer al cliente correcto");
    }
}
