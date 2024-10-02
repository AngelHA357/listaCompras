/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.listacompragestorclientes;

import DTOs.ClienteDTO;

/**
 *
 * @author PC
 */
public interface IGestorClientes {

    public void agregarCliente(ClienteDTO clienteDTO);

    public ClienteDTO encontrarClientePorUsuarioYContrasena(String usuario, String contrasena);
}
