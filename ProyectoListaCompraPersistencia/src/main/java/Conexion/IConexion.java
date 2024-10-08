/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Conexion;

import Exceptions.PersistenciaException;
import javax.persistence.EntityManager;

/**
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta - 245345. 
 */
public interface IConexion {

    public EntityManager crearConexion() throws PersistenciaException;


}
