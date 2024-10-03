/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasComponentes;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import DAOs.CompraDAO;
import DAOs.IClienteDAO;
import DAOs.ICompraDAO;
import Entidades.Cliente;
import Entidades.Compra;
import Exceptions.PersistenciaException;
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
public class CompraDAOTest {
    ICompraDAO compraDAO;
    IConexion conexion;
    private static Long clienteIdCounter = 1000L; 
    
    public CompraDAOTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        conexion = Conexion.getInstance();
        compraDAO = new CompraDAO(conexion);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void agregarCompra() throws PersistenciaException{
        Compra compra = new Compra("Cosas para el GYM", null);
        
        Compra resultado = compraDAO.agregarCompra(compra);
        
        
        assertNotNull(resultado.getId()); 
        assertEquals("Cosas para el GYM", resultado.getNombre());
        
    }
    
    @Test
    public void eliminarCompra() throws PersistenciaException{
        Compra compra = new Compra("EjemploCompra", null);
        compraDAO.agregarCompra(compra);
        Compra resultado = compraDAO.eliminarCompra(compra.getId());
        
        assertEquals("EjemploCompra", resultado.getNombre());
    }
    
    @Test
    public void testObtenerCompraPorId() throws PersistenciaException {
        Compra compra = new Compra("Compra Test", null);
        compraDAO.agregarCompra(compra);

        Compra resultado = compraDAO.obtenerCompraPorId(compra.getId());

        assertNotNull(resultado);
        assertEquals(compra.getId(), resultado.getId());
    }
    
    public void testObtenerTodasLasCompras() throws PersistenciaException {
        compraDAO.agregarCompra(new Compra("Compra 1", null));
        compraDAO.agregarCompra(new Compra("Compra 2", null));

        List<Compra> compras = compraDAO.obtenerTodasLasCompras();

        assertNotNull(compras);
        assertTrue(compras.size() >= 2); // Verificar que al menos hay dos compras
    }
    
    @Test
    public void testEliminarCompraInexistente() throws PersistenciaException {
        Compra resultado = compraDAO.eliminarCompra(999L); // ID que no existe
        assertNull(resultado); // Debería retornar null
    }
    
    @Test
    public void testObtenerComprasPorCliente_ClienteExistente() throws PersistenciaException {
        // Crear un cliente y agregarlo a la base de datos
        Cliente cliente = new Cliente();
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        clienteDAO.agregarCliente(cliente); // Se guarda el cliente en la base de datos

        // Obtener el ID del cliente generado automáticamente
        Long clienteId = cliente.getId(); // Asegúrate de que el método getId() obtenga el ID correcto

        // Crear y agregar compras asociadas al cliente
        Compra compra1 = new Compra("Compra 1", cliente);
        Compra compra2 = new Compra("Compra 2", cliente);

        compraDAO.agregarCompra(compra1);
        compraDAO.agregarCompra(compra2);

        // Obtener las compras por cliente
        List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);

        // Verificar las compras
        assertNotNull(compras);
        assertTrue(compras.size() >= 2);
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraExistente() throws PersistenciaException {
        // Crear un cliente y una compra de prueba
        Cliente cliente = new Cliente();
        // No establecer manualmente el ID del cliente, ya que es autogenerado
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        clienteDAO.agregarCliente(cliente); // Se guarda el cliente en la base de datos

        // Obtener el ID del cliente generado automáticamente
        Long clienteId = cliente.getId(); // Asegúrate de que el método getId() obtenga el ID correcto

        String nombreCompra = "Compra 1" + System.currentTimeMillis();

        Compra compra = new Compra();
        compra.setNombre(nombreCompra);
        compra.setCliente(cliente);
        compraDAO.agregarCompra(compra); // Guardar la compra en la base de datos

        // Obtener la compra por nombre y cliente
        Compra compraObtenida = compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);

        // Verificar la compra
        assertNotNull(compraObtenida);
        assertEquals(nombreCompra, compraObtenida.getNombre());
        assertEquals(clienteId, compraObtenida.getCliente().getId());
    }

    @Test
    public void testObtenerComprasPorCliente_ClienteSinCompras() throws PersistenciaException {
        // Generar un ID dinámico para el cliente sin compras
        Long clienteId = clienteIdCounter++;

        // Obtener las compras para este cliente (sin compras)
        List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);

        // Verificar que no haya compras
        assertNotNull(compras);
        assertTrue(compras.isEmpty());
    }

    @Test
    public void testObtenerCompraPorNombreYCliente_CompraInexistente() {
        // Generar un ID dinámico para el cliente
        Long clienteId = clienteIdCounter++;
        String nombreCompra = "Compra Inexistente";

        // Intentar obtener una compra que no existe y verificar la excepción
        PersistenciaException exception = assertThrows(PersistenciaException.class, () -> {
            compraDAO.obtenerCompraPorNombreYCliente(nombreCompra, clienteId);
        });
    }

    
}
