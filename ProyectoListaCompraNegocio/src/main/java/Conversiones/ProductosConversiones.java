package Conversiones;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductosConversiones {

    private final ClientesConversiones clientesConversiones;

    public ProductosConversiones() {
        this.clientesConversiones = new ClientesConversiones();
    }

    public Producto dtoAEntidad(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto producto = new Producto();
        if (dto.getId() != null) {
            producto.setId(dto.getId());
        }
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setComprado(dto.isComprado());

        Compra compra = compraDtoAEntidad(dto.getCompra());
        producto.setCompra(compra);
        producto.setCantidad(dto.getCantidad());

        return producto;
    }

    public Compra compraDtoAEntidad(CompraDTO compraDTO) {
        if (compraDTO == null) {
            return null;
        }

        List<Producto> productos = new ArrayList<>();

        Compra compra = new Compra();
        compra.setNombre(compraDTO.getNombreCompra());
        compra.setId(compraDTO.getId());
        if (!productos.isEmpty()) {
            for (ProductoDTO dto : compraDTO.getProductos()) {
                Producto producto = dtoAEntidad(dto);
                productos.add(producto);
            }
            compra.setProductos(productos);
        }
        compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));

        return compra;
    }

    public ProductoDTO entidadADTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }

        CompraDTO compraDTO = compraEntidadADTO(entidad.getCompra(), false);

        ProductoDTO productoDTO = new ProductoDTO(entidad.getNombre(), entidad.getCategoria(), entidad.isComprado(), compraDTO, entidad.getCantidad());
        productoDTO.setId(entidad.getId());

        return productoDTO;
    }

    public CompraDTO compraEntidadADTO(Compra entidad, boolean incluirProductos) {
        if (entidad == null) {
            return null;
        }

        List<ProductoDTO> productosDTO = new ArrayList<>();

        if (incluirProductos) {
            for (Producto producto : entidad.getProductos()) {
                ProductoDTO productoDTO = entidadADTO(producto);
                productosDTO.add(productoDTO);
            }
        }

        CompraDTO compra = new CompraDTO(entidad.getNombre(), clientesConversiones.convertirEntidadADTO(entidad.getCliente()));
        compra.setId(entidad.getId());
        compra.setProductos(productosDTO);

        return compra;
    }

}
