/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.listacompragestorcompras;

import BOs.CompraBO;
import BOs.ICompraBO;
import DTOs.CompraDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public class GestorCompras implements IGestorCompras {

    private ICompraBO compraBO;

    public GestorCompras() {
        this.compraBO = new CompraBO();
    }

    @Override
    public void agregarCompra(CompraDTO compraDTO) {
        compraBO.agregarCompra(compraDTO);
    }

    @Override
    public void eliminarCompra(Long id) {
        compraBO.eliminarCompra(id);
    }

    @Override
    public void actualizarCompra(CompraDTO compraDTO) {
        compraBO.actualizarCompra(compraDTO);
    }

    @Override
    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId) {
        return compraBO.obtenerComprasPorCliente(clienteId);
    }

    @Override
    public List<CompraDTO> obtenerTodasLasCompras() {
        return compraBO.obtenerTodasLasCompras();
    }

    @Override
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) {
        return compraBO.obtenerCompraPorNombreYCliente(nombre, clienteId);
    }

}
