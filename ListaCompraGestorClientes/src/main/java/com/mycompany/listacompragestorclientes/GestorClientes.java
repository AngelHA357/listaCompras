/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.listacompragestorclientes;

import BOs.ClienteBO;
import BOs.IClienteBO;
import DTOs.ClienteDTO;

/**
 *
 * @author PC
 */
public class GestorClientes implements IGestorClientes {

    IClienteBO clienteBO;
    
    public GestorClientes() {
        this.clienteBO = new ClienteBO();
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
