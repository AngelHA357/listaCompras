/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.listacomprafiltroporcompra;

import BOs.IProductoBO;
import DTOs.ProductoDTO;
import java.util.List;

/**
 *
 * @author PC
 */
public class FiltroPorCompra implements IFiltroPorCompra {

    private IProductoBO productoBO;

    public FiltroPorCompra(IProductoBO productoBO) {
        this.productoBO = productoBO;
    }

    @Override
    public List<ProductoDTO> obtenerProductosFiltrarPorCompra(Long idCompra) {
        return productoBO.filtrarPorCompra(idCompra);
    }
}
