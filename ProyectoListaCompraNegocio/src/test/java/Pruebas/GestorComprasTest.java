/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Pruebas;

import Conversiones.CompraConversiones;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import Subsistemas.GestorCompras;
import Subsistemas.IGestorCompras;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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

    @Test
    public void testAgregarCompra() throws PersistenciaException {
        // Creamos un CompraDTO de prueba
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);

        // Simulamos la conversión de CompraDTO a Compra usando el mock de CompraConversiones
        Compra compra = new Compra("Compra de Prueba", null);
        when(conversionesMock.dtoAEntidad(any(CompraDTO.class))).thenReturn(compra);
        
        // Simulamos que el DAO guarda la compra y devuelve la entidad
        when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
        
        // Simulamos la conversión de Compra a CompraDTO para el retorno
        when(conversionesMock.entidadADTO(any(Compra.class))).thenReturn(compraDTO);
        
        // Llamamos al método bajo prueba
        CompraDTO resultadoDTO = gestorCompras.agregarCompra(compraDTO);

        // Verificamos las interacciones con los mocks
        verify(conversionesMock, times(1)).dtoAEntidad(compraDTO);
        verify(conversionesMock, times(1)).entidadADTO(compra);
        verify(compraDAOMock, times(1)).agregarCompra(compra);

        // Verificamos que el resultado no sea nulo y sea correcto
        assertNotNull(resultadoDTO);
        assertEquals("Compra de Prueba", resultadoDTO.getNombreCompra());
    }
    
//     @Test
//    public void testAgregarCompra_NombreNulo() {
//        CompraDTO compraDTO = new CompraDTO(null, null);
//
//        assertThrows(NegocioException.class, () -> {
//            compraBO.agregarCompra(compraDTO);
//        });
//    }
    
//    @Test
//    public void testAgregarCompra_NombreVacio() {
//        CompraDTO compraDTO = new CompraDTO("", null);
//
//        assertThrows(NegocioException.class, () -> {
//            compraBO.agregarCompra(compraDTO);
//        });
//    }
    
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        // Creamos un CompraDTO de prueba
        CompraDTO compraDTO = new CompraDTO("Compra de Prueba", null);
        Compra compra = new Compra("Compra de Prueba", null);

        // Simulamos la conversión de CompraDTO a Compra
        when(conversionesMock.dtoAEntidad(compraDTO)).thenReturn(compra);

        // Simulamos que el DAO retorna la compra al buscar por ID
        when(compraDAOMock.obtenerCompraPorId(anyLong())).thenReturn(compra);

        // Simulamos la conversión de Compra a CompraDTO para el retorno
        when(conversionesMock.entidadADTO(compra)).thenReturn(compraDTO);

        // Llamamos al método bajo prueba
        CompraDTO resultado = gestorCompras.obtenerCompraPorId(1L);

        // Verificamos que las conversiones y la consulta en el DAO se realicen correctamente
        verify(conversionesMock, times(1)).entidadADTO(compra);
        verify(compraDAOMock, times(1)).obtenerCompraPorId(1L);

        // Verificamos que el resultado no sea nulo y sea correcto
        assertNotNull(resultado);
        assertEquals("Compra de Prueba", resultado.getNombreCompra());
}
    
    @Test
    public void testObtenerCompraPorId_Inexistente() throws PersistenciaException {
        long idInexistente = 9999L;

        // Simulamos que el DAO no encuentra la compra
        when(compraDAOMock.obtenerCompraPorId(idInexistente)).thenReturn(null);

        // Llamamos al método bajo prueba
        CompraDTO resultado = gestorCompras.obtenerCompraPorId(idInexistente);

        // Verificamos que se retorne null si no existe la compra
        assertNull(resultado);

        // Verificamos que el DAO fue invocado correctamente
        verify(compraDAOMock, times(1)).obtenerCompraPorId(idInexistente);
    }
    
    @Test
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        // Simulamos una lista de Compras
        List<Compra> compras = Arrays.asList(
                new Compra("Compra 1", null),
                new Compra("Compra 2", null)
        );

        // Simulamos que el DAO retorna las compras
        when(compraDAOMock.obtenerTodasLasCompras()).thenReturn(compras);

        // Creamos una lista para almacenar los resultados de la conversión manual
        List<CompraDTO> compraDTOs = new ArrayList<>();

        // Simulamos las conversiones manualmente utilizando un ciclo
        for (Compra compra : compras) {
            CompraDTO compraDTO = new CompraDTO(compra.getNombre(), null);
            compraDTOs.add(compraDTO);
        }

        /// Llamamos al método bajo prueba
        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        // Verificamos que la lista no sea nula y tenga el tamaño esperado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificamos que las conversiones y el DAO fueron llamados correctamente
        verify(compraDAOMock, times(1)).obtenerTodasLasCompras();
        verify(conversionesMock, times(2)).entidadADTO(any(Compra.class));
    }
    
    @Test
    public void testObtenerTodasLasCompras_Vacio() throws PersistenciaException {
        // Simulamos que no hay compras en el DAO
        when(compraDAOMock.obtenerTodasLasCompras()).thenReturn(Collections.emptyList());

        // Llamamos al método bajo prueba
        List<CompraDTO> resultado = gestorCompras.obtenerTodasLasCompras();

        // Verificamos que la lista sea vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificamos que el DAO fue llamado correctamente
        verify(compraDAOMock, times(1)).obtenerTodasLasCompras();
    }
    
    @Test
    public void testEliminarCompra() throws PersistenciaException {
        // Simulamos una Compra
        Long id = 1L;

        gestorCompras.eliminarCompra(id);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // Verificamos que el DAO fue invocado correctamente
        verify(compraDAOMock).eliminarCompra(anyLong());
        verify(compraDAOMock).eliminarCompra(longArgumentCaptor.capture());
        assertEquals(1L, longArgumentCaptor.getValue());
    }
    
    @Test
    public void testEliminarCompra_Inexistente() throws PersistenciaException {
        // Simulamos el ID de una compra inexistente
        long idInexistente = 9999L;

        // Llamamos al método eliminarCompra con un ID inexistente
        gestorCompras.eliminarCompra(idInexistente);

        // Verificamos que el DAO fue invocado correctamente
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(compraDAOMock).eliminarCompra(anyLong());
        verify(compraDAOMock).eliminarCompra(longArgumentCaptor.capture());
        assertEquals(idInexistente, longArgumentCaptor.getValue());
    }

}
