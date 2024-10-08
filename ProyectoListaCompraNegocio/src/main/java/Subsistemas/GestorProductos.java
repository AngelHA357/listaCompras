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
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IJCF
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

    @Override
    public ProductoDTO agregarProducto(ProductoDTO productoDTO) {
        Producto producto = conversiones.dtoAEntidad(productoDTO);

        try {
            Producto productoAgregado = productoDAO.agregarProducto(producto);
            return conversiones.entidadADTO(productoAgregado, false);

        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            Producto producto = productoDAO.obtenerProductoPorId(id);
            return conversiones.entidadADTO(producto, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() {
        try {
            List<Producto> productos = productoDAO.obtenerTodosLosProductos();
            List<ProductoDTO> productosDTO = new ArrayList<>();

            for (Producto producto : productos) {
                ProductoDTO productoDTO = conversiones.entidadADTO(producto, false);
                productosDTO.add(productoDTO);
            }

            return productosDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        try {
            Producto productoActualizado = productoDAO.actualizarProducto(producto);
            return conversiones.entidadADTO(productoActualizado, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void eliminarProducto(Long id) {
        try {
            productoDAO.eliminarProducto(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ProductoDTO obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) {
        try {
            Producto producto = productoDAO.obtenerProductoPorCaracteristicas(nombre, categoria, comprado, cantidad, compraId);
            return conversiones.entidadADTO(producto, false);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
