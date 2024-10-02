package BOs;

import Conversiones.CompraConversiones;
import DTOs.CompraDTO;
import Entidades.Compra;
import Exceptions.PersistenciaException;
import DAOs.ICompraDAO;
import java.util.ArrayList;
import java.util.List;

public class CompraBO implements ICompraBO {

    private final ICompraDAO compraDAO;
    private final CompraConversiones compraConversiones;

    public CompraBO(ICompraDAO compraDAO) {
        this.compraDAO = compraDAO;
        this.compraConversiones = new CompraConversiones();
    }

    @Override
    public void agregarCompra(CompraDTO compraDTO) throws PersistenciaException {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        compraDAO.agregarCompra(compra);
    }

    @Override
    public CompraDTO obtenerCompraPorId(Long id) throws PersistenciaException {
        Compra compra = compraDAO.obtenerCompraPorId(id);
        return compraConversiones.entidadADTO(compra);
    }

    @Override
    public List<CompraDTO> obtenerTodasLasCompras() throws PersistenciaException {
        List<Compra> compras = compraDAO.obtenerTodasLasCompras();
        List<CompraDTO> comprasDTO = new ArrayList<>();

        for (Compra compra : compras) {
            CompraDTO compraDTO = compraConversiones.entidadADTO(compra);
            comprasDTO.add(compraDTO);
        }

        return comprasDTO;
    }

    @Override
    public void actualizarCompra(CompraDTO compraDTO) throws PersistenciaException {
        Compra compra = compraConversiones.dtoAEntidad(compraDTO);
        compraDAO.actualizarCompra(compra);
    }

    @Override
    public void eliminarCompra(Long id) throws PersistenciaException {
        compraDAO.eliminarCompra(id);
    }
}
