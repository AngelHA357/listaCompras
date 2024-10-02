package BOs;

import DTOs.CompraDTO;
import Exceptions.PersistenciaException;

import java.util.List;

public interface ICompraBO {

    public void agregarCompra(CompraDTO compraDTO);

    public CompraDTO obtenerCompraPorId(Long id);

    public List<CompraDTO> obtenerTodasLasCompras();

    public void actualizarCompra(CompraDTO compraDTO);

    public void eliminarCompra(Long id);

    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId);

}
