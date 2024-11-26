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

    public GestorCompras() {
        conexion = Conexion.getInstance();
        this.compraDAO = new CompraDAO(conexion);
        this.compraConversiones = new CompraConversiones();
    }

    public GestorCompras(ICompraDAO compraDAO, CompraConversiones compraConversiones) {
        this.compraDAO = compraDAO;
        this.compraConversiones = compraConversiones;
    }

    @Override
    public CompraDTO agregarCompra(CompraDTO compraDTO) throws NegocioException {
        // Validar que el nombre de la compra no sea nulo o vacío
        if (compraDTO.getNombreCompra() == null || compraDTO.getNombreCompra().isBlank()) {
            throw new NegocioException("El nombre de la compra no puede ser nulo o estar en blanco");
        }

        // Validar que el cliente asociado no sea nulo
        if (compraDTO.getCliente() == null) {
            throw new NegocioException("El cliente asociado a la compra no puede ser nulo");
        }

        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        try {
            return compraConversiones.entidadADTO(compraDAO.agregarCompra(compra));
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al agregar la compra");
        }
    }

    @Override
    public CompraDTO obtenerCompraPorId(Long id) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID de la compra debe ser un valor positivo");
        }
        try {
            Compra compra = compraDAO.obtenerCompraPorId(id);
            if (compra == null) {
                throw new PersistenciaException("No se encontró la compra con el ID proporcionado");
            }
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener la compra: " + ex.getMessage());
        }
    }

    @Override
    public List<CompraDTO> obtenerTodasLasCompras() throws NegocioException {
        try {
            List<Compra> compras = compraDAO.obtenerTodasLasCompras();
            List<CompraDTO> comprasDTO = new ArrayList<>();
            for (Compra compra : compras) {
                comprasDTO.add(compraConversiones.entidadADTO(compra));
            }
            return comprasDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener las compras");
        }
    }


    @Override
    public void eliminarCompra(Long id) throws NegocioException {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID de la compra debe ser un valor positivo");
        }
        try {
            // Verificar si la compra existe antes de eliminarla
            Compra compra = compraDAO.obtenerCompraPorId(id);
            if (compra == null) {
                throw new PersistenciaException("No se puede eliminar porque no se encontró la compra con el ID proporcionado");
            }
            compraDAO.eliminarCompra(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al eliminar la compra: " + ex.getMessage());
        }
    }

    @Override
    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId) throws NegocioException {
        if (clienteId == null || clienteId <= 0) {
            throw new NegocioException("El ID del cliente debe ser un valor positivo");
        }
        try {
            List<Compra> compras = compraDAO.obtenerComprasPorCliente(clienteId);
            List<CompraDTO> comprasDTO = new ArrayList<>();
            for (Compra compra : compras) {
                comprasDTO.add(compraConversiones.entidadADTO(compra));
            }
            return comprasDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener las compras del cliente");
        }
    }

    @Override
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws NegocioException {
        if (nombre == null || nombre.isBlank()) {
            throw new NegocioException("El nombre de la compra no puede ser nulo o estar en blanco");
        }
        if (clienteId == null || clienteId <= 0) {
            throw new NegocioException("El ID del cliente debe ser un valor positivo");
        }
        try {
            Compra compra = compraDAO.obtenerCompraPorNombreYCliente(nombre, clienteId);
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorCompras.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener la compra por nombre y cliente");
        }
    }
    
    
}
