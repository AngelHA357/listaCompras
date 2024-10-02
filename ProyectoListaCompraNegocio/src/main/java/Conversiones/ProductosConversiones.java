package Conversiones;

import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;

public class ProductosConversiones {

    private final CompraConversiones compraConversiones;

    public ProductosConversiones() {
        this.compraConversiones = new CompraConversiones();
    }

    public Producto dtoAEntidad(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setComprado(dto.isComprado());

        Compra compra = compraConversiones.dtoAEntidad(dto.getCompra());
        producto.setCompra(compra);

        return producto;
    }

    public ProductoDTO entidadADTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }

        return new ProductoDTO(
                entidad.getNombre(),
                entidad.getCategoria(),
                entidad.isComprado(),
                compraConversiones.entidadADTO(entidad.getCompra())
        );
    }
}
