package BOs;

import DTOs.CompraDTO;
import Exceptions.PersistenciaException;

import java.util.List;

public interface ICompraBO {

    void agregarCompra(CompraDTO compraDTO) throws PersistenciaException;

    CompraDTO obtenerCompraPorId(Long id) throws PersistenciaException;

    List<CompraDTO> obtenerTodasLasCompras() throws PersistenciaException;

    void actualizarCompra(CompraDTO compraDTO) throws PersistenciaException;

    void eliminarCompra(Long id) throws PersistenciaException;
}
