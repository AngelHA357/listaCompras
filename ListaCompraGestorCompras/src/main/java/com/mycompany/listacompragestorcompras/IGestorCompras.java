/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.listacompragestorcompras;

import DTOs.CompraDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public interface IGestorCompras {

    public void agregarCompra(CompraDTO compraDTO);

    public void eliminarCompra(Long id);

    public void actualizarCompra(CompraDTO compraDTO);

    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId);

    public List<CompraDTO> obtenerTodasLasCompras();

}
