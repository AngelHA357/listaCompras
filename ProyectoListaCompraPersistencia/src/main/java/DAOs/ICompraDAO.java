package DAOs;

import Entidades.Compra;
import Exceptions.PersistenciaException;
import java.util.List;

public interface ICompraDAO {

    public Compra agregarCompra(Compra compra) throws PersistenciaException;

    public Compra obtenerCompraPorId(Long id) throws PersistenciaException;

    public List<Compra> obtenerTodasLasCompras() throws PersistenciaException;

    public Compra actualizarCompra(Compra compra) throws PersistenciaException;

    public Compra eliminarCompra(Long id) throws PersistenciaException;

    public List<Compra> obtenerComprasPorCliente(Long clienteId) throws PersistenciaException;

    public Compra obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws PersistenciaException;
}
