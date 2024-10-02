/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasComponentes;

import Entidades.Compra;
import Entidades.Producto;
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
public class CompraTest {
    
    public CompraTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void agregarProductoACompra() {
        // Crear una nueva instancia de Compra
        Compra compra = new Compra();

        // Crear un nuevo producto y agregarlo a la compra
        Producto producto = new Producto("Papel", "Higiene Personal", compra);
        compra.agregarProducto(producto);
        
        // Verificar que el producto fue agregado correctamente
        assertEquals(1, compra.getProductos().size());
        assertEquals(producto, compra.getProductos().get(0));

         // Asegurar que la compra asociada es la correcta
        assertEquals(compra, producto.getCompra()); 
    }
    
    @Test
    public void agregarProductoACompraNula() {
        Compra compra = null;

        // Crear un producto con una compra nula
        assertThrows(NullPointerException.class, () -> {
            Producto producto = new Producto("Papel", "Higiene Personal", compra);
            compra.agregarProducto(producto);
        });
    }
    
    @Test
    public void agregarMultiplesProductosACompra() {
        Compra compra = new Compra();

        Producto producto1 = new Producto("Papel", "Higiene Personal", compra);
        Producto producto2 = new Producto("Jabón", "Higiene Personal", compra);

        compra.agregarProducto(producto1);
        compra.agregarProducto(producto2);

        // Verificar que se han agregado dos productos
        assertEquals(2, compra.getProductos().size());
        assertEquals(producto1, compra.getProductos().get(0));
        assertEquals(producto2, compra.getProductos().get(1));
    }
    
    
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
