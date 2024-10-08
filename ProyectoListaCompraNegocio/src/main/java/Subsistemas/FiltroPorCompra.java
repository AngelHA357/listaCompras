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
public class FiltroPorCompra implements IFiltroPorCompra {

    private IConexion conexion;
    private final IProductoDAO productoDAO;
    private final ProductosConversiones conversiones;

    public FiltroPorCompra() {
        conexion = Conexion.getInstance();
        this.productoDAO = new ProductoDAO(conexion);
        this.conversiones = new ProductosConversiones();
    }

    public FiltroPorCompra(IProductoDAO productoDAO, ProductosConversiones conversiones) {
        this.productoDAO = productoDAO;
        this.conversiones = conversiones;
    }

    /**
     * Método para obtener productos asociados a una compra específica.
     *
     * @param compraId ID de la compra de la que se quieren obtener los
     * productos.
     * @return Lista de productos que pertenecen a la compra especificada.
     */
    @Override
    public List<ProductoDTO> obtenerProductosFiltrarPorCompra(Long compraId) {
        try {
            List<Producto> productos = productoDAO.obtenerProductosPorCompraId(compraId);
            List<ProductoDTO> productosDTO = new ArrayList<>();

            for (Producto producto : productos) {
                ProductoDTO productoDTO = conversiones.entidadADTO(producto);
                productosDTO.add(productoDTO);
            }

            return productosDTO;
        } catch (PersistenciaException ex) {
            Logger.getLogger(FiltroPorCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
