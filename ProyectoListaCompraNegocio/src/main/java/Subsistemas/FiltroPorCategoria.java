/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subsistemas;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ProductosConversiones;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public class FiltroPorCategoria implements IFiltroPorCategoria {

    private IConexion conexion;
    private final IProductoDAO productoDAO;
    private final ProductosConversiones conversiones;

    /**
     * Constructor que inicializa la conexión a la base de datos, un objeto de
     * acceso a datos de productos y un objeto de conversión de productos.
     *
     * Este constructor obteniene la instancia de conexión, y se crea un
     * ProductoDAO para interactuar con la base de datos. También se inicializa
     * un objeto ProductosConversiones para realizar conversiones entre
     * entidades y DTOs.
     */
    public FiltroPorCategoria() {
        conexion = Conexion.getInstance();
        this.productoDAO = new ProductoDAO(conexion);
        this.conversiones = new ProductosConversiones();
    }

    /**
     * Incializa el objeto productoDAO y el objeto de Conversiones mediante la
     * inyeccion de dependencias, este constructor es útil para la elaboración
     * de pruebas unitarias.
     *
     * @param productoDAO Objeto que implementa la interfaz IProductoDAO.
     * @param conversiones Objeto de la clase ProductosConversiones.
     */
    public FiltroPorCategoria(IProductoDAO productoDAO, ProductosConversiones conversiones) {
        this.productoDAO = productoDAO;
        this.conversiones = conversiones;
    }

    /**
     * Método para filtrar productos de una compra específica por categoría.
     *
     * @param categoria Categoría de los productos a filtrar.
     * @param compraId ID de la compra de la que se quieren filtrar los
     * productos.
     * @return Lista de productos que pertenecen a la categoría y compra
     * especificada.
     */
    @Override
    public List<ProductoDTO> filtrarPorCategoriaYCompraId(String categoria, Long compraId) {
        try {
            List<Producto> productos = productoDAO.filtrarPorCategoriaYCompraId(categoria, compraId);
            List<ProductoDTO> productosDTO = new ArrayList<>();

            for (Producto producto : productos) {
                ProductoDTO productoDTO = conversiones.entidadADTO(producto);
                productosDTO.add(productoDTO);
            }

            return productosDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(FiltroPorCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
