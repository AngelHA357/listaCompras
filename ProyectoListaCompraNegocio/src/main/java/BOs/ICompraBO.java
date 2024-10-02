package BOs;

import DTOs.CompraDTO;
import Exceptions.PersistenciaException;

import java.util.List;

public interface ICompraBO {

    void agregarCompra(CompraDTO compraDTO);

    CompraDTO obtenerCompraPorId(Long id);

    List<CompraDTO> obtenerTodasLasCompras();

    void actualizarCompra(CompraDTO compraDTO);

    void eliminarCompra(Long id);
}
