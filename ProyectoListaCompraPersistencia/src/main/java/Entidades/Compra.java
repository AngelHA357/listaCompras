package Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "compra")
    private List<Producto> productos;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    private String nombre;

    // Constructor, getters y setters
    public Compra() {
        productos = new ArrayList<Producto>();
        
    }
    
    public Compra(String nombre, Cliente cliente){
        this.nombre = nombre;
        this.cliente = cliente;
        productos = new ArrayList<Producto>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void agregarProducto(Producto producto){
        producto.setCompra(this);  
        productos.add(producto);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
