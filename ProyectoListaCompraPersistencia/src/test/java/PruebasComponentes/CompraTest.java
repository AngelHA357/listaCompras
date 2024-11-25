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
 * Esta clase permite realizar pruebas unitarias con el Producto al agregarlo a
 * una compra.
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
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

    /**
     * Permite probar la adición de un producto a una compra existente.
     */
    @Test
    public void agregarProductoACompra() {
        Compra compra = new Compra();

        Producto producto = new Producto("Papel", "Higiene Personal", compra, 6.0);
        compra.agregarProducto(producto);

        assertEquals(1, compra.getProductos().size());
        assertEquals(producto, compra.getProductos().get(0));
        assertEquals(compra, producto.getCompra());
    }

    /**
     * Permite probar la adición de un producto a una compra nula, lo que
     * debería lanzar una NullPointerException.
     */
    @Test
    public void agregarProductoACompraNula() {
        Compra compra = null;

        assertThrows(NullPointerException.class, () -> {
            Producto producto = new Producto("Papel", "Higiene Personal", compra, 6.0);
            compra.agregarProducto(producto);
        });
    }

    /**
     * Permite probar la adición de múltiples productos a una compra.
     */
    @Test
    public void agregarMultiplesProductosACompra() {
        Compra compra = new Compra();

        Producto producto1 = new Producto("Papel", "Higiene Personal", compra, 6.0);
        Producto producto2 = new Producto("Jabón", "Higiene Personal", compra, 6.0);

        compra.agregarProducto(producto1);
        compra.agregarProducto(producto2);

        assertEquals(2, compra.getProductos().size());
        assertEquals(producto1, compra.getProductos().get(0));
        assertEquals(producto2, compra.getProductos().get(1));
    }

}
