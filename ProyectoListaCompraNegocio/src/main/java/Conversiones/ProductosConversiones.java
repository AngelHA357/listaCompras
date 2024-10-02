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

        for (ProductoDTO dto : compraDTO.getProductos()) {
            Producto producto = dtoAEntidad(dto);
            productos.add(producto);
        }

        Compra compra = new Compra();
        compra.setId(compraDTO.getId());
        compra.setProductos(productos);
        compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));

        return compra;
    }

    public ProductoDTO entidadADTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }

        return new ProductoDTO(
                entidad.getNombre(),
                entidad.getCategoria(),
                entidad.isComprado(),
                compraEntidadADTO(entidad.getCompra()),
                entidad.getCantidad()
        );
    }

    public CompraDTO compraEntidadADTO(Compra entidad) {
        if (entidad == null) {
            return null;
        }

        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : entidad.getProductos()) {
            ProductoDTO productoDTO = entidadADTO(producto);
            productosDTO.add(productoDTO);
        }

        CompraDTO compra = new CompraDTO(entidad.getNombre(), clientesConversiones.convertirEntidadADTO(entidad.getCliente()));
        compra.setId(entidad.getId());
        compra.setProductos(productosDTO);

        return compra;

    }

}
