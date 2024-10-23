/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Reparacion;
import Persistencia.ReparacionDAO;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlReparacion {
     private ReparacionDAO reparacionDAO;

    // Constructor que recibe una conexión y pasa al DAO
    public ControlReparacion(Connection conexion) {
        this.reparacionDAO = new ReparacionDAO(conexion);
    }

    // Método para agregar una nueva reparación
    public void agregarReparacion(Reparacion reparacion) {
        if (reparacion == null || reparacion.getVehiculo() == null) {
            throw new IllegalArgumentException("La reparación y el vehículo asociado no pueden ser nulos");
        }

        reparacionDAO.agregar(reparacion); // Llama al DAO para agregar la reparación
        System.out.println("Reparación agregada exitosamente para el vehículo con placa: " + reparacion.getVehiculo().getPlaca());
    }

    // Método para actualizar una reparación existente
    public void actualizarReparacion(Reparacion reparacion) {
        if (reparacion == null || reparacion.getId() == 0) {
            throw new IllegalArgumentException("La reparación no es válida o no tiene un ID asignado");
        }

        reparacionDAO.actualizar(reparacion); // Llama al DAO para actualizar la reparación
        System.out.println("Reparación actualizada exitosamente: " + reparacion.getId());
    }

    // Método para eliminar una reparación
    public void eliminarReparacion(long id) {
        Reparacion reparacion = reparacionDAO.obtenerPorId(id);
        if (reparacion == null) {
            throw new IllegalArgumentException("No se encontró la reparación con ID: " + id);
        }

        reparacionDAO.eliminar(id); // Llama al DAO para eliminar la reparación
        System.out.println("Reparación eliminada exitosamente con ID: " + id);
    }

    // Método para obtener una reparación por su ID
    public Reparacion obtenerReparacionPorId(long id) {
        Reparacion reparacion = reparacionDAO.obtenerPorId(id);
        if (reparacion == null) {
            throw new IllegalArgumentException("No se encontró la reparación con ID: " + id);
        }

        return reparacion; // Devuelve la reparación encontrada
    }

    // Método para obtener todas las reparaciones
    public List<Reparacion> obtenerTodasLasReparaciones() {
        List<Reparacion> reparaciones = reparacionDAO.obtenerTodos();
        if (reparaciones.isEmpty()) {
            System.out.println("No se encontraron reparaciones");
        }

        return reparaciones; // Devuelve la lista de reparaciones
    }
}
