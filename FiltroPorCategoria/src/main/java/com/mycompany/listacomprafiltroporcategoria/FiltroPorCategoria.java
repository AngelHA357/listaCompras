/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.listacomprafiltroporcategoria;

import BOs.IProductoBO;
import BOs.ProductoBO;
import DTOs.ProductoDTO;
import java.util.List;

/**
 *
 * @author JoseH
 */
public class FiltroPorCategoria implements IFiltroPorCategoria {

    private IProductoBO productoBO;

    public FiltroPorCategoria() {
        this.productoBO = new ProductoBO();
    }

    @Override
     public List<ProductoDTO> filtrarPorCategoriaYCompraId(String categoria, Long compraId){
        return productoBO.filtrarPorCategoriaYCompraId(categoria, compraId);
    }
}
