package DTOs;

import java.util.List;

public class CompraDTO {
    private List<String> productoIds;
    private String clienteId; // Asociación con Cliente

    public CompraDTO() {}

    public CompraDTO(List<String> productoIds, String clienteId) {
        this.productoIds = productoIds;
        this.clienteId = clienteId;
    }

    public List<String> getProductoIds() {
        return productoIds;
    }

    public void setProductoIds(List<String> productoIds) {
        this.productoIds = productoIds;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}
