/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subsistemas;

import Conexion.Conexion;
import Conexion.IConexion;
import Conversiones.ProductosConversiones;
import DAOs.CompraDAO;
import DAOs.ICompraDAO;
import DAOs.IProductoDAO;
import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Compra;
import Entidades.Producto;
import Exceptions.NegocioException;
import Exceptions.PersistenciaException;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ICompraDAO compraDAO;

    public FiltroPorCompra() {
        conexion = Conexion.getInstance();
        this.productoDAO = new ProductoDAO(conexion);
        this.conversiones = new ProductosConversiones();
        this.compraDAO = new CompraDAO(conexion);
    }

    public FiltroPorCompra(IProductoDAO productoDAO, ProductosConversiones conversiones, ICompraDAO compraDAO) {
        this.productoDAO = productoDAO;
        this.conversiones = conversiones;
        this.compraDAO = compraDAO;
    }

    @Override
    public List<ProductoDTO> obtenerProductosFiltrarPorCompra(Long compraId) throws NegocioException {
        if (compraId == null) {
            throw new NegocioException("El ID de la compra no puede ser nulo");
        }

        try {
            Compra compra = compraDAO.obtenerCompraPorId(compraId);
            if (compra == null) {
                throw new NegocioException("No existe una compra con el ID proporcionado: " + compraId);
            }

            List<Producto> productos = productoDAO.obtenerProductosPorCompraId(compraId);
            if (productos == null || productos.isEmpty()) {
                return Collections.emptyList();
            }

            List<ProductoDTO> productosDTO = new ArrayList<>();
            for (Producto producto : productos) {
                ProductoDTO productoDTO = conversiones.entidadADTO(producto, false);
                productosDTO.add(productoDTO);
            }
            return productosDTO;

        } catch (PersistenciaException ex) {
            Logger.getLogger(FiltroPorCompra.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException("Error al obtener los productos de la compra");
        }
    }

}
