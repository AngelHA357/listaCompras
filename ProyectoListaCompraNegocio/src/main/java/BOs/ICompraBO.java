package BOs;

import DTOs.CompraDTO;
import Exceptions.PersistenciaException;

import java.util.List;

public interface ICompraBO {

    public CompraDTO agregarCompra(CompraDTO compraDTO);

    public CompraDTO obtenerCompraPorId(Long id);

    public List<CompraDTO> obtenerTodasLasCompras();

    public CompraDTO actualizarCompra(CompraDTO compraDTO);

    public void eliminarCompra(Long id);

    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId);
    
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId);

}
