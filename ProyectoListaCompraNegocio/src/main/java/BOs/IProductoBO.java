package BOs;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IProductoBO {

    public ProductoDTO agregarProducto(ProductoDTO productoDTO);

    public ProductoDTO obtenerProductoPorId(Long id);

    public List<ProductoDTO> obtenerTodosLosProductos();

    public ProductoDTO actualizarProducto(ProductoDTO productoDTO);

    public void eliminarProducto(Long id);

    public List<ProductoDTO> filtrarPorCategoría(String categoria);

    public List<ProductoDTO> filtrarPorCompra(Long compraId);
}
