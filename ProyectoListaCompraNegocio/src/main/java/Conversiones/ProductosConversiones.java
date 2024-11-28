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

    public ProductosConversiones() {
        this.clientesConversiones = new ClientesConversiones();
    }
    
    public ProductosConversiones(ClientesConversiones clientesConversiones) {
        this.clientesConversiones = clientesConversiones;
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
        producto.setCantidad(dto.getCantidad());

        // Convertir la compra si existe
        CompraDTO compraDTO = dto.getCompraDTO();
        if (compraDTO != null) {
            Compra compra = new Compra();
            compra.setId(compraDTO.getId());
            compra.setNombre(compraDTO.getNombreCompra());
            if (compraDTO.getCliente() != null) {
                compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));
            }
            producto.setCompra(compra);
        }

        return producto;
    }

    public Compra compraDtoAEntidad(CompraDTO compraDTO) {
        if (compraDTO == null) {
            return null;
        }

        Compra compra = new Compra();
        compra.setId(compraDTO.getId());
        compra.setNombre(compraDTO.getNombreCompra());

        List<Producto> productos = new ArrayList<>();
        if (compraDTO.getProductos() != null) {
            for (ProductoDTO dto : compraDTO.getProductos()) {
                Producto producto = dtoAEntidad(dto);
                if (producto != null) {
                    producto.setCompra(compra);
                    productos.add(producto);
                }
            }
        }
        compra.setProductos(productos);

        if (compraDTO.getCliente() != null) {
            compra.setCliente(clientesConversiones.convertirDTOAEntidad(compraDTO.getCliente()));
        }

        return compra;
    }

    public ProductoDTO entidadADTO(Producto entidad, boolean incluirCompra) {
        if (entidad == null) {
            return null;
        }

        // Crear un DTO básico de compra con solo el ID y nombre
        CompraDTO compraDTO = null;
        if (entidad.getCompra() != null) {
            compraDTO = new CompraDTO();
            compraDTO.setId(entidad.getCompra().getId());
            compraDTO.setNombreCompra(entidad.getCompra().getNombre());
            if (entidad.getCompra().getCliente() != null) {
                compraDTO.setCliente(clientesConversiones.convertirEntidadADTO(entidad.getCompra().getCliente()));
            }

            // Solo incluir productos si se solicita explícitamente
            if (incluirCompra && entidad.getCompra().getProductos() != null) {
                List<ProductoDTO> productosDTO = new ArrayList<>();
                for (Producto producto : entidad.getCompra().getProductos()) {
                    if (!producto.getId().equals(entidad.getId())) { // Evitar recursión
                        ProductoDTO productoDTO = entidadADTO(producto, false);
                        productosDTO.add(productoDTO);
                    }
                }
                compraDTO.setProductos(productosDTO);
            }
        }

        ProductoDTO productoDTO = new ProductoDTO(
                entidad.getNombre(),
                entidad.getCategoria(),
                entidad.isComprado(),
                compraDTO,
                entidad.getCantidad()
        );
        productoDTO.setId(entidad.getId());

        return productoDTO;
    }

    public CompraDTO compraEntidadADTO(Compra entidad, boolean incluirProductos) {
        if (entidad == null) {
            return null;
        }

        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setId(entidad.getId());
        compraDTO.setNombreCompra(entidad.getNombre());

        if (entidad.getCliente() != null) {
            compraDTO.setCliente(clientesConversiones.convertirEntidadADTO(entidad.getCliente()));
        }

        if (incluirProductos && entidad.getProductos() != null) {
            List<ProductoDTO> productosDTO = new ArrayList<>();
            for (Producto producto : entidad.getProductos()) {
                ProductoDTO productoDTO = entidadADTO(producto, false);
                productosDTO.add(productoDTO);
            }
            compraDTO.setProductos(productosDTO);
        }

        return compraDTO;
    }
}