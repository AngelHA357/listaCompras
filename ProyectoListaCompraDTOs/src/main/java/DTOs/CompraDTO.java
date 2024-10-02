package DTOs;

import java.util.List;

public class CompraDTO {

    private Long id;
    private List<ProductoDTO> productos;
    private ClienteDTO cliente;
    private String nombreCompra;

    public CompraDTO() {
    }

    public CompraDTO(String nombreCompra, ClienteDTO cliente) {
        this.nombreCompra = nombreCompra;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompra() {
        return nombreCompra;
    }

    public void setNombreCompra(String nombreCompra) {
        this.nombreCompra = nombreCompra;
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
