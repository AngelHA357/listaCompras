/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.listacompragestorproductos;

import BOs.IProductoBO;
import DTOs.ProductoDTO;

/**
 *
 * @author PC
 */
public class GestorProductos implements IGestorProductos {

    private IProductoBO productoBO;

    public GestorProductos(IProductoBO productoBO) {
        this.productoBO = productoBO;
    }
    
    

    @Override
    public void agregarProducto(ProductoDTO productoDTO) {
        productoBO.agregarProducto(productoDTO);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoBO.eliminarProducto(id);
    }

    @Override
    public void actualizarProducto(ProductoDTO productoDTO) {
        productoBO.actualizarProducto(productoDTO);
    }

}
