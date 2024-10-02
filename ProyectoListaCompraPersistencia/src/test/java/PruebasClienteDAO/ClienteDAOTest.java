/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasClienteDAO;

import Conexion.Conexion;
import Conexion.IConexion;
import DAOs.ClienteDAO;
import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
public class ClienteDAOTest {
    private ClienteDAO clienteDAO;
    private IConexion conexion;
    
    public ClienteDAOTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws PersistenciaException {
        conexion = Conexion.getInstance();
        clienteDAO = new ClienteDAO(conexion);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAgregarCliente() throws PersistenciaException{
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        
        Cliente resultado = clienteDAO.agregarCliente(cliente);
        
        
        assertNotNull(resultado.getId()); 
        assertEquals("Victor Humberto", resultado.getNombre());
        
    }
    
     @Test
    public void obtenerClienteExistente() throws PersistenciaException {
        Cliente cliente = clienteDAO.obtenerClientePorId(1l);

        
        assertEquals(1L, cliente.getId());
    }
 
    @Test
    public void obtenerClienteInexistente() throws PersistenciaException {
        Cliente cliente = clienteDAO.obtenerClientePorId(Long.MAX_VALUE);
        assertNull(cliente);

    }
   
}
