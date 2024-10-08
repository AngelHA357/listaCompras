/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.ProductoDTO;
import java.util.List;

/**
 *
 * @author IJCF
 */
public interface IFiltroPorCategoria {

    public List<ProductoDTO> filtrarPorCategoriaYCompraId(String categoria, Long compraId);

}