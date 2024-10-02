package BOs;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.PersistenciaException;
import java.util.List;

public interface IProductoBO {

    void agregarProducto(ProductoDTO productoDTO) throws PersistenciaException;

    ProductoDTO obtenerProductoPorId(Long id) throws PersistenciaException;

    List<ProductoDTO> obtenerTodosLosProductos() throws PersistenciaException;

    void actualizarProducto(ProductoDTO productoDTO) throws PersistenciaException;

    void eliminarProducto(Long id) throws PersistenciaException;
}
