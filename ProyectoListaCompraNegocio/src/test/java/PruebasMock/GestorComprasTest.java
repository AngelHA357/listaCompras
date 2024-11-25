/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

import Conversiones.CompraConversiones;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import Subsistemas.GestorCompras;
import Subsistemas.IGestorCompras;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author JoseH
 */
public class GestorComprasTest {

    private IGestorCompras gestorCompras;
    private ICompraDAO compraDAOMock;
    CompraConversiones conversionesMock;

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
     */
    @Test
    public void testAgregarCompra() throws PersistenciaException {
        try {
            // Se crea un CompraDTO de prueba
            CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);

            // Se simula la conversión de CompraDTO a Compra
            Compra compra = new Compra("Compra de Prueba", null);
            when(conversionesMock.dtoAEntidad(any(CompraDTO.class))).thenReturn(compra);

            // Se simula que el DAO guarda la compra y devuelve la entidad
            when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);

            // Se simula la conversión de Compra a CompraDTO para el retorno
            when(conversionesMock.entidadADTO(any(Compra.class))).thenReturn(compraDTO);

            // Se llama al método bajo prueba
            CompraDTO resultadoDTO = gestorCompras.agregarCompra(compraDTO);

            // Se verifican las interacciones con los mocks
            verify(conversionesMock, times(1)).dtoAEntidad(compraDTO);
            verify(conversionesMock, times(1)).entidadADTO(compra);
            verify(compraDAOMock, times(1)).agregarCompra(compra);

            // Se verifica que el resultado no sea nulo y sea correcto
            assertNotNull(resultadoDTO);
            assertEquals("Compra de Prueba", resultadoDTO.getNombreCompra());
        } catch (NegocioException ex) {
            Logger.getLogger(GestorComprasTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        // Se crea un CompraDTO de prueba
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);
        Compra compra = new Compra("Compra de Prueba", null);

        // Se simula la conversión de CompraDTO a Compra
        when(conversionesMock.dtoAEntidad(compraDTO)).thenReturn(compra);

        // Se simula que el DAO retorna la compra al buscar por ID
        when(compraDAOMock.obtenerCompraPorId(anyLong())).thenReturn(compra);

        // Se simula la conversión de Compra a CompraDTO para el retorno
        when(conversionesMock.entidadADTO(compra)).thenReturn(compraDTO);

        // Se llama al método bajo prueba
        CompraDTO resultado = gestorCompras.obtenerCompraPorId(1L);

        // Se verifican las conversiones y la consulta en el DAO
        verify(conversionesMock, times(1)).entidadADTO(compra);
        verify(compraDAOMock, times(1)).obtenerCompraPorId(1L);

        // Se verifica que el resultado no sea nulo y sea correcto
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

        // Verifica que se lanza la excepción esperada
        assertThrows(NegocioException.class, () -> gestorCompras.obtenerCompraPorId(idInexistente));

        // Verifica que se llamó al método obtenerCompraPorId del DAO
        verify(compraDAOMock, times(1)).obtenerCompraPorId(idInexistente);
    }

    /**
     * Se verifica que se obtengan todas las compras correctamente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException, NegocioException {
        // Se simulan varias Compras
        List<Compra> compras = Arrays.asList(
                new Compra("Compra 1", null),
                new Compra("Compra 2", null)
        );

        // Se simula que el DAO retorna las compras
        when(compraDAOMock.obtenerTodasLasCompras()).thenReturn(compras);

        // Se simulan las conversiones manualmente utilizando un ciclo
        List<CompraDTO> compraDTOs = new ArrayList<>();
        for (Compra compra : compras) {
            CompraDTO compraDTO = new CompraDTO(compra.getNombre(), null);
            compraDTOs.add(compraDTO);
        }

        // Se llama al método bajo prueba
        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        // Se verifica que la lista no sea nula y tenga el tamaño esperado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Se verifican las interacciones con el DAO
        verify(compraDAOMock, times(1)).obtenerTodasLasCompras();
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
        // Simula un ID válido
        Long idValido = 1L;

        // Simula una compra existente
        Compra compraMock = new Compra();
        compraMock.setId(idValido);
        when(compraDAOMock.obtenerCompraPorId(idValido)).thenReturn(compraMock);

        // Configura el mock para no lanzar excepción en la eliminación
        doNothing().when(compraDAOMock).eliminarCompra(idValido);

        // Llama al método eliminarCompra
        gestorCompras.eliminarCompra(idValido);

        // Verifica que se llamó a obtenerCompraPorId
        verify(compraDAOMock).obtenerCompraPorId(idValido);

        // Verifica que se llamó a eliminarCompra con el ID correcto
        verify(compraDAOMock).eliminarCompra(idValido);
    }

    /**
     * Se verifica que se elimine correctamente una compra inexistente.
     *
     * @throws PersistenciaException Si ocurre un error en la persistencia.
     */
    @Test
    public void testEliminarCompra_Inexistente() throws PersistenciaException {
        // Simula un ID inexistente
        long idInexistente = 9999L;

        // Configura el mock para devolver null al intentar obtener la compra
        when(compraDAOMock.obtenerCompraPorId(idInexistente)).thenReturn(null);

        // Verifica que se lanza la excepción esperada
        assertThrows(NegocioException.class, () -> gestorCompras.eliminarCompra(idInexistente));

        // Verifica que se llamó al método obtenerCompraPorId del DAO
        verify(compraDAOMock).obtenerCompraPorId(idInexistente);

        // Verifica que no se llamó al método eliminarCompra
        verify(compraDAOMock, never()).eliminarCompra(idInexistente);
    }

}
