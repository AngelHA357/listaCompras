package DTOs;

public class ProductoDTO {

    private String nombre;
    private String categoria;
    private boolean comprado;
    private CompraDTO compraDTO;

    public ProductoDTO() {
    }

    public ProductoDTO(String nombre, String categoria, boolean comprado, CompraDTO compraDTO) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.comprado = comprado;
        this.compraDTO = compraDTO;
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
}
