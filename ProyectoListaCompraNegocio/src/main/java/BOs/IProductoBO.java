package BOs;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IProductoBO {

    public void agregarProducto(ProductoDTO productoDTO);

    public ProductoDTO obtenerProductoPorId(Long id);

    public List<ProductoDTO> obtenerTodosLosProductos();

    public void actualizarProducto(ProductoDTO productoDTO);

    public void eliminarProducto(Long id);
}
