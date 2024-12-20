package Conversiones;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class CompraConversiones {

    private final ProductosConversiones productosConversiones;
    private final ClientesConversiones clientesConversiones;

    public CompraConversiones() {
        this.productosConversiones = new ProductosConversiones();
        this.clientesConversiones = new ClientesConversiones();
    }
    
    public CompraConversiones(ProductosConversiones productosConversiones, ClientesConversiones clientesConversiones) {
        this.productosConversiones = productosConversiones;
        this.clientesConversiones = clientesConversiones;
    }

    public Compra dtoAEntidad(CompraDTO compraDTO) {
        if (compraDTO == null) {
            return null;
        }

        Compra compra = new Compra();

        if (compraDTO.getId() != null) {
            compra.setId(compraDTO.getId());
        }

        compra.setNombre(compraDTO.getNombreCompra());

        if (compraDTO.getProductos() != null) {
            List<Producto> productos = new ArrayList<>();
            for (ProductoDTO dto : compraDTO.getProductos()) {
                Producto producto = productosConversiones.dtoAEntidad(dto);
                productos.add(producto);
            }
            compra.setProductos(productos);
        }
        compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));

        return compra;
    }

    public CompraDTO entidadADTO(Compra entidad) {
        if (entidad == null) {
            return null;
        }

        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : entidad.getProductos()) {
            ProductoDTO productoDTO = productosConversiones.entidadADTO(producto, false);
            productosDTO.add(productoDTO);
        }

        CompraDTO compra = new CompraDTO(entidad.getNombre(), clientesConversiones.convertirEntidadADTO(entidad.getCliente()));
        compra.setId(entidad.getId());
        compra.setProductos(productosDTO);

        return compra;

    }
}
