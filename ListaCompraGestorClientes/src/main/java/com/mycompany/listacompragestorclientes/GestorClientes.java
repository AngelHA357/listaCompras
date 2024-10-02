/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.listacompragestorclientes;

import BOs.IClienteBO;
import DTOs.ClienteDTO;

/**
 *
 * @author PC
 */
public class GestorClientes implements IGestorClientes {

    private IClienteBO clienteBO;

    public GestorClientes(IClienteBO clienteBO) {
        this.clienteBO = clienteBO;
    }

    @Override
    public void agregarCliente(ClienteDTO clienteDTO) {
        clienteBO.agregarCliente(clienteDTO);
    }

    @Override
    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena) {
        return clienteBO.encontrarClientePorUsuarioYContrasena(usuario, contrasena);
    }

}
