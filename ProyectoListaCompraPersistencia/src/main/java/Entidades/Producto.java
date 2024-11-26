package Entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "categoria", nullable = true)
    private String categoria;
    
    @Column(name = "comprado")
    private boolean comprado;
    
    @Column(name = "cantidad")
    private Double cantidad;

    @ManyToOne
    @JoinColumn(name = "compra_id", referencedColumnName  = "id")
    private Compra compra;

    // Constructor, getters y setters
    public Producto() {
    }

    public Producto(String nombre, String categoria, boolean comprado, Compra compra, Double cantidad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.comprado = comprado;
        this.compra = compra;
        this.cantidad = cantidad;
    }

    public Producto(String nombre, String categoría, Compra compra, Double cantidad) {
        this.nombre = nombre;
        this.categoria = categoría;
        this.compra = compra;
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
