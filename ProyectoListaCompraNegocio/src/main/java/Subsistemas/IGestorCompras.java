/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Subsistemas;

import DTOs.CompraDTO;
import Exceptions.NegocioException;
import java.util.List;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345 .
 */
public interface IGestorCompras {

    /**
     * Método para agregar una nueva compra al sistema.
     *
     * @param compraDTO Objeto de tipo CompraDTO que contiene los datos de la
     * compra.
     * @return La compra agregada.
     * @throws NegocioException Si ocurre un error al agregar la compra.
     */
    public CompraDTO agregarCompra(CompraDTO compraDTO) throws NegocioException;

    /**
     * Método para obtener una compra por su ID.
     *
     * @param id ID de la compra.
     * @return La compra encontrada o null si no se encuentra.
     */
    public CompraDTO obtenerCompraPorId(Long id) throws NegocioException;

    /**
     * Método para obtener todas las compras registradas en el sistema.
     *
     * @return Lista de todas las compras.
     */
    public List<CompraDTO> obtenerTodasLasCompras() throws NegocioException;


    /**
     * Método para eliminar una compra del sistema.
     *
     * @param id ID de la compra a eliminar.
     */
    public void eliminarCompra(Long id) throws NegocioException;

    /**
     * Método para obtener todas las compras realizadas por un cliente
     * específico.
     *
     * @param clienteId ID del cliente.
     * @return Lista de compras realizadas por el cliente.
     */
    public List<CompraDTO> obtenerComprasPorCliente(Long clienteId) throws NegocioException;

    /**
     * Método para obtener una compra específica por su nombre y el ID del
     * cliente.
     *
     * @param nombre Nombre de la compra.
     * @param clienteId ID del cliente.
     * @return La compra correspondiente o null si no se encuentra.
     */
    public CompraDTO obtenerCompraPorNombreYCliente(String nombre, Long clienteId) throws NegocioException;
}
