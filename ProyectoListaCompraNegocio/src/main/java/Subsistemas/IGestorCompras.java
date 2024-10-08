/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.CompraDTO;
import java.util.List;

/**
 *
 * @author IJCF
 */
public interface IGestorCompras {

    public CompraDTO agregarCompra(CompraDTO compraDTO);

    public CompraDTO obtenerCompraPorId(Long id);

    public List<CompraDTO> obtenerTodasLasCompras();

    public CompraDTO actualizarCompra(CompraDTO compraDTO);

    public void eliminarCompra(Long id);

    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId);

    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId);
}
