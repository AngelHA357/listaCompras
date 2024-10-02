package Conversiones;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;

import java.util.ArrayList;
import java.util.List;

public class CompraConversiones {

    private final ProductosConversiones productosConversiones;
    private final ClientesConversiones clientesConversiones;


    public CompraConversiones() {
        this.productosConversiones = new ProductosConversiones();
        this.clientesConversiones = new ClientesConversiones();
    }

    public Compra dtoAEntidad(CompraDTO compraDTO) {
        if (compraDTO == null) {
            return null;
        }

        List<Producto> productos = new ArrayList<>();

        for (ProductoDTO dto : compraDTO.getProductos()) {
            Producto producto = productosConversiones.dtoAEntidad(dto);
            productos.add(producto);
        }

        Compra compra = new Compra();
        compra.setId(compraDTO.getId());
        compra.setProductos(productos);
        compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));

        return compra;
    }

    public CompraDTO entidadADTO(Compra entidad) {
        if (entidad == null) {
            return null;
        }

        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : entidad.getProductos()) {
            ProductoDTO productoDTO = productosConversiones.entidadADTO(producto);
            productosDTO.add(productoDTO);
        }

        return new CompraDTO(
                entidad.getId(),
                productosDTO,
                clientesConversiones.convertirEntidadADTO(entidad.getCliente() )
        );
    }
}
