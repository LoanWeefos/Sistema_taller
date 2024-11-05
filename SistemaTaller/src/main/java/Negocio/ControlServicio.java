/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Persistencia.Conexion;
import Persistencia.ServicioDAO;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlServicio {
     private ServicioDAO servicioDAO;

    public ControlServicio() {
        this.servicioDAO = new ServicioDAO(Conexion.getConnection());
    }

    // Método para agregar una nueva reparación
    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulos");
        }

        servicioDAO.agregar(servicio); // Llama al DAO para agregar la reparación
        System.out.println("Servicio agregada exitosamente");
    }

    // Método para actualizar un servicio existente
    public void actualizarServicio(int id, String descripcion, double costo) {
        try {
            Servicio servicio = servicioDAO.obtenerPorId(id);
            if (servicio != null) {
                servicio.setDescripcion(descripcion);
                servicio.setCosto(costo);
                

                servicioDAO.actualizar(servicio);
                System.out.println("Servicio actualizado correctamente.");
            } else {
                System.err.println("El servicio con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al actualizar el servicio: " + e.getMessage());
        }
    }

    // Método para eliminar un servicio por su ID
    public void eliminarServicio(int id) {
        try {
            Servicio servicio = servicioDAO.obtenerPorId(id);
            if (servicio != null) {
                servicioDAO.eliminar(id);
                System.out.println("Servicio eliminado correctamente.");
            } else {
                System.err.println("El servicio con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al eliminar el servicio: " + e.getMessage());
        }
    }

    // Método para obtener un servicio por su ID
    public Servicio obtenerServicioPorId(int id) {
        try {
            Servicio servicio = servicioDAO.obtenerPorId(id);
            if (servicio != null) {
                System.out.println("Servicio encontrado: " + servicio);
            } else {
                System.err.println("El servicio con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al obtener el servicio: " + e.getMessage());
        }
        return null;
    }

    // Método para listar todos los servicios
    public List<Servicio> listarServicios() {
        try {
            List<Servicio> servicios = servicioDAO.obtenerTodos();
            if (servicios.isEmpty()) {
                System.out.println("No hay servicios registrados.");
            } else {
                for (Servicio servicio : servicios) {
                    System.out.println(servicio);
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Error al listar los servicios: " + e.getMessage());
        }
        return null;
    }
}
