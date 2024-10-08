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
public class ProductosConversiones {

    private final ClientesConversiones clientesConversiones;

    /**
     * Constructor de la clase ProductosConversiones que inicializa la
     * conversión de clientes.
     */
    public ProductosConversiones() {
        this.clientesConversiones = new ClientesConversiones();
    }

    /**
     * Convierte un objeto ProductoDTO a un objeto Producto.
     *
     * @param dto El objeto ProductoDTO que se desea convertir.
     * @return Un objeto Producto que representa el producto, o null si el dto
     * es null.
     */
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

    /**
     * Convierte un objeto CompraDTO a un objeto Compra.
     *
     * @param compraDTO El objeto CompraDTO que se desea convertir.
     * @return Un objeto Compra que representa la compra, o null si el compraDTO
     * es null.
     */
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

    /**
     * Convierte un objeto Producto a un objeto ProductoDTO.
     *
     * @param entidad El objeto Producto que se desea convertir.
     * @return Un objeto ProductoDTO que representa el producto, o null si la
     * entidad es null.
     */
    public ProductoDTO entidadADTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }

        CompraDTO compraDTO = compraEntidadADTO(entidad.getCompra(), false);

        ProductoDTO productoDTO = new ProductoDTO(entidad.getNombre(), entidad.getCategoria(), entidad.isComprado(), compraDTO, entidad.getCantidad());
        productoDTO.setId(entidad.getId());

        return productoDTO;
    }

    /**
     * Convierte un objeto Compra a un objeto CompraDTO.
     *
     * @param entidad El objeto Compra que se desea convertir.
     * @param incluirProductos Indica si se deben incluir los productos en la
     * conversión.
     * @return Un objeto CompraDTO que representa la compra, o null si la
     * entidad es null.
     */
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
