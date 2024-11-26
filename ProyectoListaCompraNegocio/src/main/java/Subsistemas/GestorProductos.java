/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subsistemas;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ProductosConversiones;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class GestorProductos implements IGestorProductos {

    private IConexion conexion;
    private final IProductoDAO productoDAO;
    private final ProductosConversiones conversiones;

    public GestorProductos() {
        conexion = Conexion.getInstance();
        this.productoDAO = new ProductoDAO(conexion);
        this.conversiones = new ProductosConversiones();
    }

    public GestorProductos(IProductoDAO productoDAO, ProductosConversiones conversiones) {
        this.productoDAO = productoDAO;
        this.conversiones = conversiones;
    }

    @Override
    public ProductoDTO agregarProducto(ProductoDTO productoDTO) throws NegocioException {
        if (productoDTO.getNombre() == null || productoDTO.getNombre().isBlank()) {
            throw new NegocioException("El nombre del producto no puede ser nulo o estar en blanco");
        }
        if (productoDTO.getCategoria() == null || productoDTO.getCategoria().isBlank()) {
            throw new NegocioException("La categoría del producto no puede ser nula o estar en blanco");
        }
        if (productoDTO.getCantidad() == null || productoDTO.getCantidad() <= 0) {
            throw new NegocioException("La cantidad del producto no puede ser nula o menor o igual a cero");
        }
        if (productoDTO.getCompra() == null) {
            throw new NegocioException("El producto debe estar asociado a una compra válida");
        }

        Producto producto = conversiones.dtoAEntidad(productoDTO);

        try {
            Producto productoAgregado = productoDAO.agregarProducto(producto);
            return conversiones.entidadADTO(productoAgregado, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al agregar el producto");
        }
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) throws NegocioException {
        try {
            Producto producto = productoDAO.obtenerProductoPorId(id);
            if (producto == null) {
                throw new NegocioException("No se encontró el producto con ID: " + id);
            }
            return conversiones.entidadADTO(producto, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener el producto");
        }
    }


    @Override
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) throws NegocioException {
        try {
            Producto productoExistente = productoDAO.obtenerProductoPorId(productoDTO.getId());
            if (productoExistente == null) {
                throw new NegocioException("No se puede actualizar un producto inexistente con ID: " + productoDTO.getId());
            }

            Producto producto = conversiones.dtoAEntidad(productoDTO);
            Producto productoActualizado = productoDAO.actualizarProducto(producto);
            return conversiones.entidadADTO(productoActualizado, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al actualizar el producto");
        }
    }

    @Override
    public void eliminarProducto(Long id) throws NegocioException {
        try {
            Producto productoExistente = productoDAO.obtenerProductoPorId(id);
            if (productoExistente == null) {
                throw new NegocioException("No se puede eliminar un producto inexistente con ID: " + id);
            }

            productoDAO.eliminarProducto(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al eliminar el producto");
        }
    }

    @Override
    public ProductoDTO obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) throws NegocioException {
        try {
            Producto producto = productoDAO.obtenerProductoPorCaracteristicas(nombre, categoria, comprado, cantidad, compraId);
            if (producto == null) {
                throw new NegocioException("No se encontró el producto con las características especificadas");
            }
            return conversiones.entidadADTO(producto, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener el producto");
        }
    }
}
