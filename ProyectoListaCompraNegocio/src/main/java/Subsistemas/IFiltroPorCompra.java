/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.ProductoDTO;
import Exceptions.NegocioException;
import java.util.List;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public interface IFiltroPorCompra {

    /**
     * Método para obtener productos asociados a una compra específica.
     *
     * @param idCompra ID de la compra de la que se quieren obtener los
     * productos.
     * @return Lista de productos que pertenecen a la compra especificada.
     * @throws Exceptions.NegocioException
     */
    public List<ProductoDTO> obtenerProductosFiltrarPorCompra(Long idCompra) throws NegocioException;

}
