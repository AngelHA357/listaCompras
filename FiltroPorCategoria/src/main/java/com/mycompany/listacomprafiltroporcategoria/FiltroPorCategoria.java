/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.listacomprafiltroporcategoria;

import BOs.IProductoBO;

/**
 *
 * @author JoseH
 */
public class FiltroPorCategoria implements IFiltroPorCategoria{
    private IProductoBO productoBO;

    @Override
    public void filtrarPorCategoria(String categoria){
        productoBO.filtrarPorCategoría(categoria);
    }
}
