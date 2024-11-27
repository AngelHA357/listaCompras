package Subsistemas;

import Conversiones.CompraConversiones;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import Entidades.Cliente;
import Entidades.Compra;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorComprasTest {

    public GestorComprasTest() {
    }

    private IGestorCompras gestorCompras;
    private ICompraDAO compraDAOMock;
    private CompraConversiones conversionesMock;

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws PersistenciaException {
        compraDAOMock = mock(CompraDAO.class);
        conversionesMock = mock(CompraConversiones.class);

        gestorCompras = new GestorCompras(compraDAOMock, conversionesMock);
    }

    @AfterEach
    public void tearDown() throws PersistenciaException {

    }

    /**
     * Se verifica que el método agregarCompra agrega una compra correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si ocurre un error con los objetos de negocio.
     */
    @Test
    public void testAgregarCompra() throws PersistenciaException, NegocioException {
        ClienteDTO clienteDTO = new ClienteDTO("Nombre", "Apellido1", "Apellido2", "Usuario", "Contraseña");
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", clienteDTO);

        Cliente cliente = new Cliente();
        Compra compra = new Compra("Compra de Prueba", cliente);

        when(conversionesMock.dtoAEntidad(any(CompraDTO.class))).thenReturn(compra);
        when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
        when(conversionesMock.entidadADTO(any(Compra.class))).thenReturn(compraDTO);

        CompraDTO resultadoDTO = gestorCompras.agregarCompra(compraDTO);

        assertNotNull(resultadoDTO);
        assertEquals("Compra de Prueba", resultadoDTO.getNombreCompra());
        verify(conversionesMock).dtoAEntidad(compraDTO);
        verify(conversionesMock).entidadADTO(compra);
        verify(compraDAOMock).agregarCompra(compra);
    }

    /**
     * Se verifica que se lance una excepción cuando el nombre de la compra es
     * nulo.
     */
    @Test
    public void testAgregarCompra_NombreNulo() {
        CompraDTO compraDTO = new CompraDTO(null, null);

        assertThrows(NegocioException.class, () -> {
            gestorCompras.agregarCompra(compraDTO);
        });
    }

    /**
     * Se verifica que se lance una excepción cuando el nombre de la compra está
     * vacío.
     */
    @Test
    public void testAgregarCompra_NombreVacio() {
        CompraDTO compraDTO = new CompraDTO("", null);

        assertThrows(NegocioException.class, () -> {
            gestorCompras.agregarCompra(compraDTO);
        });
    }

    /**
     * Se verifica que el método obtenerCompraPorId retorne la compra correcta
     * si existe.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException, NegocioException {
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);
        Compra compra = new Compra("Compra de Prueba", null);

        when(conversionesMock.dtoAEntidad(compraDTO)).thenReturn(compra);
        when(compraDAOMock.obtenerCompraPorId(anyLong())).thenReturn(compra);
        when(conversionesMock.entidadADTO(compra)).thenReturn(compraDTO);

        CompraDTO resultado = gestorCompras.obtenerCompraPorId(1L);

        verify(conversionesMock, times(1)).entidadADTO(compra);
        verify(compraDAOMock, times(1)).obtenerCompraPorId(1L);
        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombreCompra());
    }

    /**
     * Se verifica que el método obtenerCompraPorId retorne null si la compra no
     * existe.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorId_Inexistente() throws PersistenciaException {
        long idInexistente = 9999L;

        // Configura el mock para lanzar una excepción al intentar obtener una compra inexistente
        when(compraDAOMock.obtenerCompraPorId(idInexistente))
                .thenThrow(new PersistenciaException("Compra no encontrada"));

        assertThrows(NegocioException.class, () -> gestorCompras.obtenerCompraPorId(idInexistente));
        verify(compraDAOMock, times(1)).obtenerCompraPorId(idInexistente);
    }

    /**
     * Se verifica que se obtengan todas las compras correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException, NegocioException {

        List<Compra> compras = Arrays.asList(
                new Compra("Compra 1", null),
                new Compra("Compra 2", null)
        );

        CompraDTO compraDTO1 = new CompraDTO("Compra 1", null);
        CompraDTO compraDTO2 = new CompraDTO("Compra 2", null);

        when(compraDAOMock.obtenerTodasLasCompras()).thenReturn(compras);
        when(conversionesMock.entidadADTO(compras.get(0))).thenReturn(compraDTO1);
        when(conversionesMock.entidadADTO(compras.get(1))).thenReturn(compraDTO2);

        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Compra 1", resultado.get(0).getNombreCompra());
        assertEquals("Compra 2", resultado.get(1).getNombreCompra());

        verify(compraDAOMock).obtenerTodasLasCompras();
        verify(conversionesMock, times(2)).entidadADTO(any(Compra.class));
    }

    /**
     * Se verifica que se retorne una lista vacía cuando no hay compras en el
     * DAO.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerTodasLasCompras_Vacio() throws PersistenciaException, NegocioException {
        // Se simula que no hay compras en el DAO
        when(compraDAOMock.obtenerTodasLasCompras()).thenReturn(Collections.emptyList());

        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(compraDAOMock, times(1)).obtenerTodasLasCompras();
    }

    /**
     * Se verifica que se elimine una compra correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testEliminarCompra() throws PersistenciaException, NegocioException {
        Long idValido = 1L;

        Compra compraMock = new Compra();
        compraMock.setId(idValido);
        when(compraDAOMock.obtenerCompraPorId(idValido)).thenReturn(compraMock);

        doNothing().when(compraDAOMock).eliminarCompra(idValido);

        gestorCompras.eliminarCompra(idValido);

        verify(compraDAOMock).obtenerCompraPorId(idValido);
        verify(compraDAOMock).eliminarCompra(idValido);
    }

    /**
     * Se verifica que se elimine correctamente una compra inexistente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testEliminarCompra_Inexistente() throws PersistenciaException {
        long idInexistente = 9999L;

        when(compraDAOMock.obtenerCompraPorId(idInexistente)).thenReturn(null);

        assertThrows(NegocioException.class, () -> gestorCompras.eliminarCompra(idInexistente));

        verify(compraDAOMock).obtenerCompraPorId(idInexistente);
        verify(compraDAOMock, never()).eliminarCompra(idInexistente);
    }

    /**
     * Se verifica que se obtenga una compra existente por su nombre y cliente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si hay un problema en la capa de negocio.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_Existente() throws PersistenciaException, NegocioException {
        String nombreCompra = "Compra Test";
        Long clienteId = 1L;
        Compra compra = new Compra();
        CompraDTO compraDTO = new CompraDTO();

        when(compraDAOMock.obtenerCompraPorNombreYCliente(nombreCompra, clienteId)).thenReturn(compra);
        when(conversionesMock.entidadADTO(compra)).thenReturn(compraDTO);

        CompraDTO resultado = gestorCompras.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);

        assertNotNull(resultado);
        verify(compraDAOMock).obtenerCompraPorNombreYCliente(nombreCompra, clienteId);
    }

    /**
     * Se verifica que se obtenga correctamente la lista completa de compras de
     * un cliente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     * @throws NegocioException Si hay un problema en la capa de negocio.
     */
    @Test
    public void testObtenerComprasPorCliente_ListaCompleta() throws PersistenciaException, NegocioException {
        Long clienteId = 1L;
        List<Compra> compras = Arrays.asList(new Compra(), new Compra());

        when(compraDAOMock.obtenerComprasPorCliente(clienteId)).thenReturn(compras);
        when(conversionesMock.entidadADTO(any(Compra.class))).thenReturn(new CompraDTO());

        List<CompraDTO> resultado = gestorCompras.obtenerComprasPorCliente(clienteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(compraDAOMock).obtenerComprasPorCliente(clienteId);
    }

    /**
     * Se verifica que no se pueda obtener una compra si los parámetros son
     * inválidos.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_ParametrosInvalidos() throws PersistenciaException {
        assertThrows(NegocioException.class, ()
                -> gestorCompras.obtenerCompraPorNombreYCliente(null, 1L));

        assertThrows(NegocioException.class, ()
                -> gestorCompras.obtenerCompraPorNombreYCliente("", 1L));

        assertThrows(NegocioException.class, ()
                -> gestorCompras.obtenerCompraPorNombreYCliente("Compra Test", -1L));

        verify(compraDAOMock, never()).obtenerCompraPorNombreYCliente(anyString(), anyLong());
    }

    /**
     * Se verifica el manejo de excepciones en caso de un error de persistencia
     * al obtener una compra.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerCompraPorNombreYCliente_ErrorPersistencia() throws PersistenciaException {
        when(compraDAOMock.obtenerCompraPorNombreYCliente(anyString(), anyLong()))
                .thenThrow(new PersistenciaException("Error de base de datos"));

        assertThrows(NegocioException.class, ()
                -> gestorCompras.obtenerCompraPorNombreYCliente("Compra Test", 1L));
    }
}
