package DTOs;

import java.util.List;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class CompraDTO {

    private Long id;
    private List<ProductoDTO> productos;
    private ClienteDTO cliente;
    private String nombreCompra;

    /**
     *Constructor por Defecto.
     */
    public CompraDTO() {
    }

    /**
     * Constructor con parámetros para crear un CompraDTO.
     *
     * @param nombreCompra Nombre de la compra.
     * @param cliente Objeto ClienteDTO asociado a la compra.
     */
    public CompraDTO(String nombreCompra, ClienteDTO cliente) {
        this.nombreCompra = nombreCompra;
        this.cliente = cliente;
    }

    // Métodos getter y setter
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
