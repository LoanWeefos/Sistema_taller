/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Reparacion;
import java.util.List;

/**
 *
 * @author hoshi
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 *
 * @author hoshi
 */
public class ReparacionDAO implements IPersistencia<Reparacion> {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadDePersistencia");
    private EntityManager em;

    public ReparacionDAO() {
        em = emf.createEntityManager();
    }

    @Override
    public void agregar(Reparacion entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Reparacion entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            em.getTransaction().begin();
            Reparacion reparacion = em.find(Reparacion.class, id);
            if (reparacion != null) {
                em.remove(reparacion);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Reparacion obtenerPorId(Long id) {
        try {
            return em.find(Reparacion.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reparacion> obtenerTodos() {
        try {
            return em.createQuery("SELECT r FROM Reparacion r", Reparacion.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para cerrar el EntityManager
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
