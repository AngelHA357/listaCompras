/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.CompraDTO;
import Exceptions.NegocioException;
import java.util.List;

/**
 *
 * @author IJCF
 */
public interface IGestorCompras {

    public CompraDTO agregarCompra(CompraDTO compraDTO) throws NegocioException;

    public CompraDTO obtenerCompraPorId(Long id) throws NegocioException;

    public List<CompraDTO> obtenerTodasLasCompras() throws NegocioException;

    public CompraDTO actualizarCompra(CompraDTO compraDTO)throws NegocioException;

    public void eliminarCompra(Long id ) throws NegocioException;

    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId) throws NegocioException;

    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws NegocioException;
}
