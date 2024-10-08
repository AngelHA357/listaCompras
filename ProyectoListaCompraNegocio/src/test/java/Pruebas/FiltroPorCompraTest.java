/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

import Conversiones.CompraConversiones;
import Conversiones.ProductosConversiones;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import Subsistemas.FiltroPorCompra;
import Subsistemas.GestorCompras;
import Subsistemas.IFiltroPorCompra;
import Subsistemas.IGestorCompras;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author victo
 */
public class FiltroPorCompraTest {
    
    private IFiltroPorCompra filtroCompra;
    private IProductoDAO productoDAOMock;
    private ProductosConversiones conversionesMock;
    
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
        productoDAOMock = mock(ProductoDAO.class);
        conversionesMock = mock(ProductosConversiones.class);
        
        
        filtroCompra = new FiltroPorCompra(productoDAOMock, conversionesMock);
    }
    
    @AfterEach
    public void tearDown() {
    }    
    
    @Test
    public void testFiltrarPorCompra() throws PersistenciaException {
        // Crear mocks necesarios
        ICompraDAO compraDAOMock = mock(ICompraDAO.class);
        CompraConversiones conversionesCompra = mock(CompraConversiones.class);
        IGestorCompras compraBO = new GestorCompras(compraDAOMock, conversionesCompra);

        // Creamos una CompraDTO de prueba
        CompraDTO compraDTO = new CompraDTO("Compra", null);
        Compra compra = new Compra("Compra", null);
        when(conversionesCompra.dtoAEntidad(compraDTO)).thenReturn(compra);
        when(compraDAOMock.agregarCompra(any(Compra.class))).thenReturn(compra);
        when(conversionesCompra.entidadADTO(compra)).thenReturn(compraDTO);
        compraDTO = compraBO.agregarCompra(compraDTO);

        // Creamos los productos asociados a la compra
        ProductoDTO productoDTO1 = new ProductoDTO("Producto C", "Categoria D", false, compraDTO, 25.0);
        ProductoDTO productoDTO2 = new ProductoDTO("Producto D", "Categoria D", false, compraDTO, 30.0);
        Producto producto1 = new Producto("Producto C", "Categoria D", false, compra, 25.0);
        Producto producto2 = new Producto("Producto D", "Categoria D", false, compra, 30.0);

        when(conversionesMock.dtoAEntidad(productoDTO1)).thenReturn(producto1);
        when(conversionesMock.dtoAEntidad(productoDTO2)).thenReturn(producto2);

        // Simulamos el filtro por compra
        when(productoDAOMock.obtenerProductosPorCompraId(anyLong())).thenReturn(Arrays.asList(producto1, producto2));

        List<ProductoDTO> resultado = filtroCompra.obtenerProductosFiltrarPorCompra(1L);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductosPorCompraId(1L);
    }
    
    @Test
    public void testFiltrarPorCompraInexistente() throws PersistenciaException {
        Long compraIdInexistente = 999L;

        // Simulamos que no se encuentran productos para la compra inexistente
        when(productoDAOMock.obtenerProductosPorCompraId(compraIdInexistente)).thenReturn(Collections.emptyList());

        List<ProductoDTO> resultado = filtroCompra.obtenerProductosFiltrarPorCompra(compraIdInexistente);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        // Verificación de que el método fue invocado correctamente
        verify(productoDAOMock, times(1)).obtenerProductosPorCompraId(compraIdInexistente);
    }

}
