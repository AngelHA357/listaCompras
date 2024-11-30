
package ExportadorPDF;

import DTOs.CompraDTO;
import Exceptions.NegocioException;
import Subsistemas.IGestorCompras;

/**
 *
 * @author victo
 */
public class ExportadorPDF{
    private final IGestorCompras gestorCompra;

    public ExportadorPDF(IGestorCompras gestorCompra) {
        this.gestorCompra = gestorCompra;
    }

    public String exportarCompraAPDF(Long compraId) throws NegocioException {
        CompraDTO compra = gestorCompra.obtenerCompraPorId(compraId);
        
        if (compra == null) {
            throw new NegocioException("No se encontró la compra con el ID proporcionado");
        }
        
        String nombreArchivo;
        if (compra.getProductos() == null || compra.getProductos().isEmpty()) {
            nombreArchivo = compra.getNombreCompra() + "_vacia.pdf";
        } else {
            nombreArchivo = compra.getNombreCompra() + ".pdf";
        }
        
        return nombreArchivo;
    }
}
