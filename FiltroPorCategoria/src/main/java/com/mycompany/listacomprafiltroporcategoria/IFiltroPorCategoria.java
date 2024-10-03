/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.listacomprafiltroporcategoria;

import DTOs.ProductoDTO;
import java.util.List;

/**
 *
 * @author JoseH
 */
public interface IFiltroPorCategoria {
    public List<ProductoDTO> filtrarPorCategoria(String categoria);
}
