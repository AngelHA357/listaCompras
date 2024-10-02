package DTOs;

public class ProductoDTO {

    private String nombre;
    private String categoria;
    private boolean comprado;
    private String compraId; 

    public ProductoDTO() {
    }

    public ProductoDTO(String nombre, String categoria, boolean comprado, String compraId) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.comprado = comprado;
        this.compraId = compraId;
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

    public String getCompraId() {
        return compraId;
    }

    public void setCompraId(String compraId) {
        this.compraId = compraId;
    }
}
