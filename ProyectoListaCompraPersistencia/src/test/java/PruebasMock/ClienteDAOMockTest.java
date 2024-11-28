/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package PruebasMock;

import Conexion.IConexion;
import DAOs.ClienteDAO;
import Entidades.Cliente;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author JoseH
 */
public class ClienteDAOMockTest {

    @Mock
    private IConexion mockConexion;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    @Mock
    private TypedQuery<Cliente> mockTypedQuery;

    @InjectMocks
    private ClienteDAO clienteDAO;

    @BeforeEach
    public void setUp() throws PersistenciaException {
        MockitoAnnotations.openMocks(this);

        when(mockConexion.crearConexion()).thenReturn(mockEntityManager);
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);

        doNothing().when(mockTransaction).begin();
        doNothing().when(mockTransaction).commit();
        when(mockTransaction.isActive()).thenReturn(true);

        clienteDAO = new ClienteDAO(mockConexion);
    }

    @Test
    public void testAgregarCliente() throws PersistenciaException {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

        doAnswer(invocation -> {
            Cliente clienteArgumento = invocation.getArgument(0);
            clienteArgumento.setId(1L);
            return null;
        }).when(mockEntityManager).persist(any(Cliente.class));

        when(mockEntityManager.find(eq(Cliente.class), anyLong())).thenReturn(cliente);

        Cliente resultado = clienteDAO.agregarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("Encinas", resultado.getApellidoPaterno());
        assertEquals("Guzmán", resultado.getApellidoMaterno());
        assertEquals("toribio", resultado.getUsuario());
        assertEquals("ABCD1234", resultado.getContrasenia());

        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(any(Cliente.class));
        verify(mockEntityManager).find(eq(Cliente.class), anyLong());
        verify(mockTransaction).commit();
    }

    @Test
    public void testAgregarCliente_UsuarioNulo() {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", null, "ABCD1234");

        doThrow(new IllegalArgumentException("Usuario no puede ser nulo"))
                .when(mockEntityManager).persist(any(Cliente.class));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.agregarCliente(cliente);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testAgregarCliente_UsuarioVacio() {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "", "ABCD1234");

        doThrow(new IllegalArgumentException("Usuario no puede estar vacío"))
                .when(mockEntityManager).persist(any(Cliente.class));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.agregarCliente(cliente);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testAgregarCliente_ErrorPersistencia() {
        Cliente cliente = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");

        doThrow(new PersistenceException("Error al persistir"))
                .when(mockEntityManager).persist(any(Cliente.class));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.agregarCliente(cliente);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testObtenerClientePorId() throws PersistenciaException {
        Long clienteId = 1L;
        Cliente clienteEsperado = new Cliente("Victor Humberto", "Encinas", "Guzmán", "toribio", "ABCD1234");
        clienteEsperado.setId(clienteId);

        when(mockEntityManager.find(Cliente.class, clienteId)).thenReturn(clienteEsperado);

        Cliente resultado = clienteDAO.obtenerClientePorId(clienteId);

        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getId());
        assertEquals("Victor Humberto", resultado.getNombre());
        assertEquals("toribio", resultado.getUsuario());

        verify(mockEntityManager).find(Cliente.class, clienteId);
    }

    @Test
    public void testObtenerClientePorId_NoExiste() throws PersistenciaException {
        Long clienteId = 999L;
        when(mockEntityManager.find(Cliente.class, clienteId)).thenReturn(null);

        Cliente resultado = clienteDAO.obtenerClientePorId(clienteId);

        assertNull(resultado);
        verify(mockEntityManager).find(Cliente.class, clienteId);
    }

    @Test
    public void testObtenerTodosLosClientes() throws PersistenciaException {
        List<Cliente> clientesEsperados = Arrays.asList(
                new Cliente("Cliente 1", "Apellido 1", "Apellido 1", "usuario1", "pass1"),
                new Cliente("Cliente 2", "Apellido 2", "Apellido 2", "usuario2", "pass2")
        );
        clientesEsperados.get(0).setId(1L);
        clientesEsperados.get(1).setId(2L);

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Cliente c")).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(clientesEsperados);

        List<Cliente> resultado = clienteDAO.obtenerTodosLosClientes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(c -> c.getUsuario().equals("usuario1")));
        assertTrue(resultado.stream().anyMatch(c -> c.getUsuario().equals("usuario2")));

        verify(mockEntityManager).createQuery("SELECT c FROM Cliente c");
        verify(mockQuery).getResultList();
    }

    @Test
    public void testObtenerTodosLosClientes_ListaVacia() throws PersistenciaException {
        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Cliente c")).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        List<Cliente> resultado = clienteDAO.obtenerTodosLosClientes();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(mockEntityManager).createQuery("SELECT c FROM Cliente c");
        verify(mockQuery).getResultList();
    }

    @Test
    public void testObtenerTodosLosClientes_ErrorConsulta() {
        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Cliente c")).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenThrow(new PersistenceException("Error en consulta"));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerTodosLosClientes();
        });
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_ClienteExistente() throws PersistenciaException {
        String usuario = "toribio";
        String contrasenia = "ABCD1234";
        Cliente clienteEsperado = new Cliente("Victor", "Encinas", "Guzmán", usuario, contrasenia);
        clienteEsperado.setId(1L);

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Cliente c WHERE c.usuario = :usuario AND c.contrasenia = :contrasenia"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("usuario", usuario)).thenReturn(mockQuery);
        when(mockQuery.setParameter("contrasenia", contrasenia)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(clienteEsperado);

        Cliente resultado = clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(contrasenia, resultado.getContrasenia());

        verify(mockEntityManager).createQuery("SELECT c FROM Cliente c WHERE c.usuario = :usuario AND c.contrasenia = :contrasenia");
        verify(mockQuery).setParameter("usuario", usuario);
        verify(mockQuery).setParameter("contrasenia", contrasenia);
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_ClienteInexistente() {
        String usuario = "usuarioInexistente";
        String contrasenia = "contraseñaErronea";

        Query mockQuery = mock(Query.class);
        when(mockEntityManager.createQuery("SELECT c FROM Cliente c WHERE c.usuario = :usuario AND c.contrasenia = :contrasenia"))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("usuario", usuario)).thenReturn(mockQuery);
        when(mockQuery.setParameter("contrasenia", contrasenia)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(new NoResultException("No client found"));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena(usuario, contrasenia);
        });
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_UsuarioNulo() {
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena(null, "ABCD1234");
        });
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_UsuarioVacio() {
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("", "ABCD1234");
        });
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_ContraseniaNula() {
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("usuario", null);
        });
    }

    @Test
    public void testObtenerClientePorUsuarioYContrasena_ContraseniaVacia() {
        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.obtenerClientePorUsuarioYContrasena("usuario", "");
        });
    }

    @Test
    public void testActualizarCliente() throws PersistenciaException {
        Cliente cliente = new Cliente("Pedro", "Ruiz", "Díaz", "pedro", "789XYZ");
        cliente.setId(1L);

        when(mockEntityManager.merge(any(Cliente.class))).thenReturn(cliente);
        when(mockEntityManager.find(eq(Cliente.class), anyLong())).thenReturn(cliente);

        Cliente resultado = clienteDAO.actualizarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());
        assertEquals("Ruiz", resultado.getApellidoPaterno());

        verify(mockTransaction).begin();
        verify(mockEntityManager).merge(any(Cliente.class));
        verify(mockEntityManager).find(eq(Cliente.class), anyLong());
        verify(mockTransaction).commit();
    }

    @Test
    public void testActualizarCliente_ErrorPersistencia() {
        Cliente cliente = new Cliente("Luis", "Torres", "Gómez", "luis", "ABC987");
        cliente.setId(1L);

        when(mockEntityManager.merge(any(Cliente.class)))
                .thenThrow(new PersistenceException("Error al actualizar"));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.actualizarCliente(cliente);
        });

        verify(mockTransaction).rollback();
    }

    @Test
    public void testEliminarCliente() throws PersistenciaException {
        Long clienteId = 1L;
        Cliente cliente = new Cliente("Sofia", "Vega", "Castro", "sofia", "123SOF");
        cliente.setId(clienteId);

        when(mockEntityManager.find(Cliente.class, clienteId)).thenReturn(cliente);

        Cliente resultado = clienteDAO.eliminarCliente(clienteId);

        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getId());
        assertEquals("Sofia", resultado.getNombre());

        verify(mockTransaction).begin();
        verify(mockEntityManager).remove(cliente);
        verify(mockTransaction).commit();
    }

    @Test
    public void testEliminarCliente_NoExiste() throws PersistenciaException {
        Long clienteId = 999L;
        when(mockEntityManager.find(Cliente.class, clienteId)).thenReturn(null);

        Cliente resultado = clienteDAO.eliminarCliente(clienteId);

        assertNull(resultado);
        verify(mockTransaction).commit();
    }

    @Test
    public void testEliminarCliente_ErrorPersistencia() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente("Maria", "López", "Ruiz", "maria", "456MLR");
        cliente.setId(clienteId);

        when(mockEntityManager.find(Cliente.class, clienteId)).thenReturn(cliente);
        doThrow(new PersistenceException("Error al eliminar"))
                .when(mockEntityManager).remove(any(Cliente.class));

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.eliminarCliente(clienteId);
        });

        verify(mockTransaction).rollback();
    }
}
