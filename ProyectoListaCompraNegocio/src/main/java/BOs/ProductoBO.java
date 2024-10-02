package BOs;

import Conversiones.ProductosConversiones;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

public class ProductoBO implements IProductoBO {

    private final ProductoDAO productoDAO;
    private final ProductosConversiones conversiones;

    public ProductoBO(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
        this.conversiones = new ProductosConversiones();
    }

    @Override
    public void agregarProducto(ProductoDTO productoDTO) throws PersistenciaException {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        productoDAO.agregarProducto(producto);
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) throws PersistenciaException {
        Producto producto = productoDAO.obtenerProductoPorId(id);
        return conversiones.entidadADTO(producto);
    }

    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() throws PersistenciaException {
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            ProductoDTO productoDTO = conversiones.entidadADTO(producto);
            productosDTO.add(productoDTO);
        }

        return productosDTO;
    }

    @Override
    public void actualizarProducto(ProductoDTO productoDTO) throws PersistenciaException {
        Producto producto = conversiones.dtoAEntidad(productoDTO);
        productoDAO.actualizarProducto(producto);
    }

    @Override
    public void eliminarProducto(Long id) throws PersistenciaException {
        productoDAO.eliminarProducto(id);
    }
}
