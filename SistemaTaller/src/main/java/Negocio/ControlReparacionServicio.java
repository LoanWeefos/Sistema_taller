/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Persistencia.Conexion;
import Persistencia.ReparacionServicioDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlReparacionServicio {

    private ReparacionServicioDAO reparacionServicioDAO;

    public ControlReparacionServicio() {
        this.reparacionServicioDAO = new ReparacionServicioDAO(Conexion.getConnection());
    }

    // Método para agregar una nueva relación de ReparacionServicio
    public void agregarReparacionServicio(ReparacionServicio reparacionServicio) {
        reparacionServicioDAO.agregar(reparacionServicio);
    }

    // Método para actualizar una relación de ReparacionServicio existente
    public void actualizarReparacionServicio(ReparacionServicio reparacionServicio) {
        reparacionServicioDAO.actualizar(reparacionServicio);
    }

    // Método para eliminar una relación de ReparacionServicio por ID
    public void eliminarReparacionServicio(int id) {
        reparacionServicioDAO.eliminar(id);
    }

    // Método para obtener una relación de ReparacionServicio por ID
    public ReparacionServicio obtenerReparacionServicioPorId(int id) {
        return reparacionServicioDAO.obtenerPorId(id);
    }

    // Método para obtener todas las relaciones de ReparacionServicio
    public List<ReparacionServicio> obtenerTodasLasReparacionesServicios() {
        return reparacionServicioDAO.obtenerTodos();
    }

    public List<Servicio> obtenerServiciosPorReparacion(long idReparacion) {
        // Obtener las relaciones de ReparacionServicio usando el idReparacion de tipo long
        List<ReparacionServicio> relaciones = reparacionServicioDAO.obtenerPorIdReparacion(idReparacion);
        List<Servicio> servicios = new ArrayList<>();

        // Iterar sobre las relaciones y agregar los servicios a la lista
        for (ReparacionServicio relacion : relaciones) {
            servicios.add(relacion.getServicio());
        }

        return servicios;
    }

}
