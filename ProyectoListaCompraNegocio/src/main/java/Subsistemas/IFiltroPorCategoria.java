/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.ProductoDTO;
import java.util.List;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public interface IFiltroPorCategoria {

    /**
     * Método para filtrar productos de una compra específica por categoría.
     *
     * @param categoria Categoría de los productos a filtrar.
     * @param compraId ID de la compra de la que se quieren filtrar los
     * productos.
     * @return Lista de productos que pertenecen a la categoría y compra
     * especificada.
     */
    public List<ProductoDTO> filtrarPorCategoriaYCompraId(String categoria, Long compraId);

}
