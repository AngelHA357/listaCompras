/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasIntegracion;

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
public class ClienteCompraIntegrationTest {
    IClienteDAO clienteDAO;
    ICompraDAO compraDAO;
    IConexion conexion;
    
    public ClienteCompraIntegrationTest() {
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
        clienteDAO = new ClienteDAO(conexion);
        compraDAO = new CompraDAO(conexion);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAgregarClienteYCompra() throws PersistenciaException {
        // Agregar un cliente
        Cliente cliente = new Cliente("Juan", "Pérez", "Gómez", "juanp", "pass123");
        Cliente clienteAgregado = clienteDAO.agregarCliente(cliente);

        // Agregar una compra para el cliente
        Compra compra = new Compra("Compra de Ejemplo", clienteAgregado);
        Compra compraAgregada = compraDAO.agregarCompra(compra);

        // Verificar que tanto el cliente como la compra se hayan agregado correctamente
        assertNotNull(clienteAgregado.getId());
        assertNotNull(compraAgregada.getId());
        assertEquals(clienteAgregado.getId(), compraAgregada.getCliente().getId());
    }
}
