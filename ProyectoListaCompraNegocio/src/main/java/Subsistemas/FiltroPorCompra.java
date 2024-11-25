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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IJCF
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

    public FiltroPorCompra(IProductoDAO productoDAO, ProductosConversiones conversiones) {
        this.productoDAO = productoDAO;
        this.conversiones = conversiones;
        this.compraDAO = new CompraDAO(conexion); 
    }

    @Override
    public List<ProductoDTO> obtenerProductosFiltrarPorCompra(Long compraId) throws NegocioException {
        if (compraId == null) {
            throw new NegocioException("El ID de la compra no puede ser nulo");
        }

        try {
            // Verificar que la compra exista
            Compra compra = compraDAO.obtenerCompraPorId(compraId);
            if (compra == null) {
                throw new NegocioException("No existe una compra con el ID proporcionado: " + compraId);
            }

            // Obtener productos asociados a la compra
            List<Producto> productos = productoDAO.obtenerProductosPorCompraId(compraId);

            if (productos.isEmpty()) {
                throw new NegocioException("La compra con ID " + compraId + " no tiene productos asociados");
            }

            // Convertir los productos a DTO
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
