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

    /**
     * Método para agregar un nuevo producto al sistema.
     *
     * @param productoDTO Objeto ProductoDTO que contiene los datos del
     * producto.
     * @return El producto agregado.
     * @throws NegocioException Si ocurre un error durante la operación.
     */
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

        Producto producto = conversiones.dtoAEntidad(productoDTO);

        try {
            Producto productoAgregado = productoDAO.agregarProducto(producto);
            return conversiones.entidadADTO(productoAgregado);

        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Método para obtener un producto por su ID.
     *
     * @param id ID del producto.
     * @return El producto encontrado o null si no se encuentra.
     */
    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            Producto producto = productoDAO.obtenerProductoPorId(id);
            return conversiones.entidadADTO(producto);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para obtener todos los productos del sistema.
     *
     * @return Lista de todos los productos.
     */
    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() {
        try {
            List<Producto> productos = productoDAO.obtenerTodosLosProductos();
            List<ProductoDTO> productosDTO = new ArrayList<>();

            for (Producto producto : productos) {
                ProductoDTO productoDTO = conversiones.entidadADTO(producto);
                productosDTO.add(productoDTO);
            }

            return productosDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método para actualizar un producto existente.
     *
     * @param productoDTO Objeto ProductoDTO con los datos actualizados del
     * producto.
     * @return El producto actualizado.
     */
    @Override
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        try {
            Producto productoActualizado = productoDAO.actualizarProducto(producto);
            return conversiones.entidadADTO(productoActualizado);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Método para eliminar un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     */
    @Override
    public void eliminarProducto(Long id) {
        try {
            productoDAO.eliminarProducto(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
    @Override
    public ProductoDTO obtenerProductoPorCaracteristicas(String nombre, String categoria, boolean comprado, Double cantidad, Long compraId) {
        try {
            Producto producto = productoDAO.obtenerProductoPorCaracteristicas(nombre, categoria, comprado, cantidad, compraId);
            return conversiones.entidadADTO(producto);
        } catch (PersistenciaException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
