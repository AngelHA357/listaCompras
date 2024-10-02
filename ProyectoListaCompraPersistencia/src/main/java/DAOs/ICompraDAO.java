package DAOs;

import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;

public interface ICompraDAO {

    Compra agregarCompra(Compra compra) throws PersistenciaException;

    Compra obtenerCompraPorId(Long id) throws PersistenciaException;

    List<Compra> obtenerTodasLasCompras() throws PersistenciaException;

    Compra actualizarCompra(Compra compra) throws PersistenciaException;

    Compra eliminarCompra(Long id) throws PersistenciaException;
}
