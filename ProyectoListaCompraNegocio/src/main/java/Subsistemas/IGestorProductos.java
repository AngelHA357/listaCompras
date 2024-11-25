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
 * @author IJCF
 */
public interface IGestorProductos {

    public ProductoDTO agregarProducto(ProductoDTO productoDTO) throws NegocioException;

    public ProductoDTO obtenerProductoPorId(Long id) throws NegocioException;

    public List<ProductoDTO> obtenerTodosLosProductos() throws NegocioException;

    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) throws NegocioException;

    public void eliminarProducto(Long id) throws NegocioException;

    public ProductoDTO obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) throws NegocioException;

}
