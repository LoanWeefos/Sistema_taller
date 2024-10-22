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

        // Iniciar una transacción
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Crear una nueva instancia de TuEntidad
            Cliente entidad = new Cliente();
            // Configura los atributos de la entidad si es necesario
            // Por ejemplo: entidad.setNombre("Ejemplo");

            // Persistir la entidad
            em.persist(entidad);

            transaction.commit(); // Guarda la transacción
            System.out.println("Entidad persistida. Verifica las tablas en MySQL.");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Revertir si algo falla
            }
            e.printStackTrace();
        } finally {
            em.close(); // Cerrar el EntityManager
            emf.close(); // Cerrar el EntityManagerFactory
        }
    }

}
