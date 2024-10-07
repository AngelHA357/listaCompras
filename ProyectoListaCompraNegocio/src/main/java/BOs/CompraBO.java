package BOs;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.CompraConversiones;
import DAOs.CompraDAO;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import DAOs.ICompraDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompraBO implements ICompraBO {

    private IConexion conexion;
    private final ICompraDAO compraDAO;
    private final CompraConversiones compraConversiones;

    public CompraBO(ICompraDAO compraDAO, CompraConversiones compraConversiones){
        this.compraDAO = compraDAO;
        this.compraConversiones = compraConversiones;
    }
    
    public CompraBO() {
        conexion = Conexion.getInstance();
        this.compraDAO = new CompraDAO(conexion);
        this.compraConversiones = new CompraConversiones();
    }

    @Override
    public CompraDTO agregarCompra(CompraDTO compraDTO) {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        try {
            return compraConversiones.entidadADTO(compraDAO.agregarCompra(compra));
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public CompraDTO obtenerCompraPorId(Long id) {
        try {
            Compra compra = compraDAO.obtenerCompraPorId(id);
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public CompraDTO actualizarCompra(CompraDTO compraDTO) {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        CompraDTO compraActualizada;
        try {
            return compraActualizada = compraConversiones.entidadADTO(compraDAO.actualizarCompra(compra));
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void eliminarCompra(Long id) {
        try {
            compraDAO.eliminarCompra(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) {
        try {
            Compra compra = compraDAO.obtenerCompraPorNombreYCliente(nombre, clienteId);
            return compraConversiones.entidadADTO(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
