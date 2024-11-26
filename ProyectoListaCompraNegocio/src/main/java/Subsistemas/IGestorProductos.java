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
public interface IGestorProductos {

    /**
     * Método para agregar un nuevo producto al sistema.
     *
     * @param productoDTO Objeto ProductoDTO que contiene los datos del
     * producto.
     * @return El producto agregado.
     * @throws NegocioException Si ocurre un error durante la operación.
     */
    public ProductoDTO agregarProducto(ProductoDTO productoDTO) throws NegocioException;

    /**
     * Método para obtener un producto por su ID.
     *
     * @param id ID del producto.
     * @return El producto encontrado o null si no se encuentra.
     */
    public ProductoDTO obtenerProductoPorId(Long id) throws NegocioException;

    /**
     * Método para actualizar un producto existente.
     *
     * @param productoDTO Objeto ProductoDTO con los datos actualizados del
     * producto.
     * @return El producto actualizado.
     */
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) throws NegocioException;

    /**
     * Método para eliminar un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     */
    public void eliminarProducto(Long id) throws NegocioException;

    /**
     * Método para obtener un producto basado en sus características
     * específicas.
     *
     * @param nombre Nombre del producto.
     * @param categoria Categoría del producto.
     * @param comprado Estado de si el producto ha sido comprado o no.
     * @param cantidad Cantidad del producto.
     * @param compraId ID de la compra a la que pertenece el producto.
     * @return El producto que coincide con las características dadas o null si
     * no se encuentra.
     */
    public ProductoDTO obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) throws NegocioException;

}
