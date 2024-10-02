package BOs;

import Conversiones.CompraConversiones;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import DAOs.ICompraDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompraBO implements ICompraBO {

    private final ICompraDAO compraDAO;
    private final CompraConversiones compraConversiones;

    public CompraBO(ICompraDAO compraDAO) {
        this.compraDAO = compraDAO;
        this.compraConversiones = new CompraConversiones();
    }

    @Override
    public void agregarCompra(CompraDTO compraDTO) {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        try {
            compraDAO.agregarCompra(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void actualizarCompra(CompraDTO compraDTO) {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        try {
            compraDAO.actualizarCompra(compra);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminarCompra(Long id) {
        try {
            compraDAO.eliminarCompra(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(CompraBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
