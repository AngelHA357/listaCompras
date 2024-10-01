package DAOs;

import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;

public interface ICompraDAO {

    void agregarCompra(Compra compra) throws PersistenciaException;

    Compra obtenerCompraPorId(String id) throws PersistenciaException;

    List<Compra> obtenerTodasLasCompras() throws PersistenciaException;

    void actualizarCompra(Compra compra) throws PersistenciaException;

    void eliminarCompra(String id) throws PersistenciaException;
}
