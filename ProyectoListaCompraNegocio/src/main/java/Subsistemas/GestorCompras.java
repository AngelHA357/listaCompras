/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subsistemas;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.CompraConversiones;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorCompras implements IGestorCompras {

    private IConexion conexion;
    private final ICompraDAO compraDAO;
    private final CompraConversiones compraConversiones;

    /**
     * Constructor que inicializa la conexión a la base de datos, un objeto de
     * acceso a datos de compras y un objeto de conversión de compras.
     *
     * Este constructor obteniene la instancia de conexión, y se crea un
     * CompraDAO para interactuar con la base de datos. También se inicializa un
     * objeto CompraConversiones para realizar conversiones entre entidades y
     * DTOs.
     */
    public GestorCompras() {
        conexion = Conexion.getInstance();
        this.compraDAO = new CompraDAO(conexion);
        this.compraConversiones = new CompraConversiones();
    }

    /**
     * Incializa el objeto compraDAO y el objeto de Conversiones mediante la
     * inyeccion de dependencias, este constructor es útil para la elaboración
     * de pruebas unitarias.
     *
     * @param compraDAO Objeto que implementa la interfaz ICompraDAO.
     * @param compraConversiones Objeto de la clase CompraConversiones.
     */
    public GestorCompras(ICompraDAO compraDAO, CompraConversiones compraConversiones) {
        this.compraDAO = compraDAO;
        this.compraConversiones = compraConversiones;
    }

    /**
     * Método para agregar una nueva compra al sistema.
     *
     * @param compraDTO Objeto de tipo CompraDTO que contiene los datos de la
     * compra.
     * @return La compra agregada.
     * @throws NegocioException Si ocurre un error al agregar la compra.
     */
    @Override
    public CompraDTO agregarCompra(CompraDTO compraDTO) throws NegocioException {
        if (compraDTO.getNombreCompra() == null || compraDTO.getNombreCompra().isBlank()) {
            throw new NegocioException("El nombre no puede ser nulo o estar en blanco");
        }
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        try {
            return compraConversiones.entidadADTO(compraDAO.agregarCompra(compra));
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para obtener una compra por su ID.
     *
     * @param id ID de la compra.
     * @return La compra encontrada o null si no se encuentra.
     */
    @Override
    public CompraDTO obtenerCompraPorId(Long id) {
        try {
            Compra compra = compraDAO.obtenerCompraPorId(id);
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para obtener todas las compras registradas en el sistema.
     *
     * @return Lista de todas las compras.
     */
    @Override
    public List<CompraDTO> obtenerTodasLasCompras() {
        try {
            List<Compra> compras = compraDAO.obtenerTodasLasCompras();
            List<CompraDTO> comprasDTO = new ArrayList<>();

            for (Compra compra : compras) {
                CompraDTO compraDTO = compraConversiones.entidadADTO(compra);
                comprasDTO.add(compraDTO);
            }

            return comprasDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para actualizar una compra existente.
     *
     * @param compraDTO Objeto CompraDTO con los datos actualizados.
     * @return La compra actualizada.
     */
    @Override
    public CompraDTO actualizarCompra(CompraDTO compraDTO) {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        CompraDTO compraActualizada;
        try {
            return compraActualizada = compraConversiones.entidadADTO(compraDAO.actualizarCompra(compra));
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para eliminar una compra del sistema.
     *
     * @param id ID de la compra a eliminar.
     */
    @Override
    public void eliminarCompra(Long id) {
        try {
            compraDAO.eliminarCompra(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para obtener todas las compras realizadas por un cliente
     * específico.
     *
     * @param clienteId ID del cliente.
     * @return Lista de compras realizadas por el cliente.
     */
    @Override
    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId) {
        try {
            List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);
            List<CompraDTO> comprasDTO = new ArrayList<>();

            for (Compra compra : compras) {
                CompraDTO compraDTO = compraConversiones.entidadADTO(compra);
                comprasDTO.add(compraDTO);
            }

            return comprasDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para obtener una compra específica por su nombre y el ID del
     * cliente.
     *
     * @param nombre Nombre de la compra.
     * @param clienteId ID del cliente.
     * @return La compra correspondiente o null si no se encuentra.
     */
    @Override
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) {
        try {
            Compra compra = compraDAO.obtenerCompraPorNombreYCliente(nombre, clienteId);
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
