package DTOs;

import java.util.List;

public class CompraDTO {

    private Long id;
    private List<ProductoDTO> productos;
    private ClienteDTO cliente;

    public CompraDTO() {
    }

    public CompraDTO(Long id, List<ProductoDTO> productos, ClienteDTO cliente) {
        this.id = id;
        this.productos = productos;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
}
