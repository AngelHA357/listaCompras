package DTOs;

public class ProductoDTO {

    private Long id;
    private String nombre;
    private String categoria;
    private boolean comprado;
    private CompraDTO compraDTO;
    private Double cantidad;

    public ProductoDTO() {
    }

    public ProductoDTO(String nombre, String categoria, boolean comprado, CompraDTO compraDTO, Double cantidad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.comprado = comprado;
        this.compraDTO = compraDTO;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public CompraDTO getCompra() {
        return compraDTO;
    }

    public void setCompra(CompraDTO compraDTO) {
        this.compraDTO = compraDTO;
    }

    public CompraDTO getCompraDTO() {
        return compraDTO;
    }

    public void setCompraDTO(CompraDTO compraDTO) {
        this.compraDTO = compraDTO;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
