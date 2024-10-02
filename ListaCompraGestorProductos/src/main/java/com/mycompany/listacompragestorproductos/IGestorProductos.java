/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.listacompragestorproductos;

import DTOs.ProductoDTO;

/**
 *
 * @author PC
 */
public interface IGestorProductos {

    public void agregarProducto(ProductoDTO productoDTO);
    
    public void eliminarProducto(Long id);
    
    public void actualizarProducto(ProductoDTO productoDTO);

}
