/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemataller;

import Dominio.Cliente;
import Dominio.Vehiculo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Oscar
 */
public class SistemaTaller {

    public static void main(String[] args) {
       
    // Crear el EntityManagerFactory y EntityManager
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaTallerPU");
    EntityManager em = emf.createEntityManager();

    // No es necesario iniciar una transacción si no vas a persistir nada
    try {
        // Aquí no es necesario realizar ninguna operación, ya que las tablas
        // se crearán automáticamente si la configuración de persistence.xml está correcta.

        System.out.println("Las tablas se han creado correctamente.");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        em.close(); // Cerrar el EntityManager
        emf.close(); // Cerrar el EntityManagerFactory
    }
}


}
