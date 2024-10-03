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
    public void agregarProducto(ProductoDTO productoDTO) {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        try {
            productoDAO.agregarProducto(producto);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
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
    public void actualizarProducto(ProductoDTO productoDTO) {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        try {
            productoDAO.actualizarProducto(producto);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ProductoBO.class.getName()).log(Level.SEVERE, null, ex);
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
    public List<ProductoDTO> filtrarPorCategoría(String categoria) {
        try {
            List<Producto> productos = productoDAO.filtrarPorCategoria(categoria);
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

}
