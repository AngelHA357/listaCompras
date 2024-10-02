package Entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Producto implements Serializable {

    @Id
    private String id;
    private String nombre;
    private String categoria;
    private boolean comprado;

    @ManyToOne
    private Compra compra;

    // Constructor, getters y setters

    public Producto() {
    }

    public Producto(String nombre, String categoria, boolean comprado, Compra compra) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.comprado = comprado;
        this.compra = compra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
}
