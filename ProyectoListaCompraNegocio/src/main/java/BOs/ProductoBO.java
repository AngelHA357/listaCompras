package BOs;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ProductosConversiones;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoBO implements IProductoBO {

    private IConexion conexion;
    private final ProductoDAO productoDAO;
    private final ProductosConversiones conversiones;

    public ProductoBO() {
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
    public List<ProductoDTO> filtrarPorCategoriaYCompraId(String categoria, Long compraId) {
        try {
            List<Producto> productos = productoDAO.filtrarPorCategoriaYCompraId(categoria, compraId);
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
    public List<ProductoDTO> filtrarPorCompra(Long compraId) {
        try {
            List<Producto> productos = productoDAO.obtenerProductosPorCompraId(compraId);
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
