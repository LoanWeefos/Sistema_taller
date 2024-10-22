/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Reparacion;
import Dominio.ReparacionServicio;
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
 */import javax.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para la gestión de operaciones CRUD de la entidad Reparacion utilizando JDBC.
 */
public class ReparacionDAO implements IPersistencia<Reparacion> {
    private Connection conexion;

    public ReparacionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Reparacion entity) {
        String sqlReparacion = "INSERT INTO Reparaciones (nombreE, vehiculo_id) VALUES (?, ?)";
        String sqlReparacionServicio = "INSERT INTO ReparacionServicio (reparacion_id, servicio_id) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion, Statement.RETURN_GENERATED_KEYS)) {
            // Insertar la Reparacion
            stmtReparacion.setString(1, entity.getNombreE());
            stmtReparacion.setLong(2, entity.getVehiculo().getId());
            stmtReparacion.executeUpdate();

            // Obtener el ID generado para la Reparacion
            ResultSet generatedKeys = stmtReparacion.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long reparacionId = generatedKeys.getLong(1);
                entity.setId(reparacionId);

                // Insertar las relaciones en la tabla ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                        stmtReparacionServicio.setLong(1, reparacionId);
                        stmtReparacionServicio.setLong(2, reparacionServicio.getServicio().getId());
                        stmtReparacionServicio.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Reparacion entity) {
        String sqlReparacion = "UPDATE Reparaciones SET nombreE = ?, vehiculo_id = ? WHERE id = ?";
        String sqlDeleteReparacionServicio = "DELETE FROM ReparacionServicio WHERE reparacion_id = ?";
        String sqlInsertReparacionServicio = "INSERT INTO ReparacionServicio (reparacion_id, servicio_id) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            // Actualizar la Reparacion
            stmtReparacion.setString(1, entity.getNombreE());
            stmtReparacion.setLong(2, entity.getVehiculo().getId());
            stmtReparacion.setLong(3, entity.getId());
            stmtReparacion.executeUpdate();

            // Eliminar las relaciones existentes en la tabla ReparacionServicio
            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setLong(1, entity.getId());
                stmtDeleteReparacionServicio.executeUpdate();
            }

            // Insertar las nuevas relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtInsertReparacionServicio = conexion.prepareStatement(sqlInsertReparacionServicio)) {
                for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                    stmtInsertReparacionServicio.setLong(1, entity.getId());
                    stmtInsertReparacionServicio.setLong(2, reparacionServicio.getServicio().getId());
                    stmtInsertReparacionServicio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        String sqlReparacionServicio = "DELETE FROM ReparacionServicio WHERE reparacion_id = ?";
        String sqlReparacion = "DELETE FROM Reparaciones WHERE id = ?";

        try {
            // Eliminar las relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                stmtReparacionServicio.setLong(1, id);
                stmtReparacionServicio.executeUpdate();
            }

            // Eliminar la Reparacion
            try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
                stmtReparacion.setLong(1, id);
                stmtReparacion.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reparacion obtenerPorId(Long id) {
        String sqlReparacion = "SELECT * FROM Reparaciones WHERE id = ?";
        String sqlReparacionServicio = "SELECT * FROM ReparacionServicio WHERE reparacion_id = ?";
        Reparacion reparacion = null;

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            stmtReparacion.setLong(1, id);
            ResultSet rsReparacion = stmtReparacion.executeQuery();
            if (rsReparacion.next()) {
                reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getLong("id"));
                reparacion.setNombreE(rsReparacion.getString("nombreE"));
                
                // Obtener las relaciones ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    stmtReparacionServicio.setLong(1, id);
                    ResultSet rsReparacionServicio = stmtReparacionServicio.executeQuery();
                    List<ReparacionServicio> reparacionesServicios = new ArrayList<>();
                    while (rsReparacionServicio.next()) {
                        ReparacionServicio reparacionServicio = new ReparacionServicio();
                        // Aquí deberías cargar la entidad Servicio y asignarla a reparacionServicio
                        //reparacionServicio.setServicio(new ServicioDAO(conexion).obtenerPorId(rsReparacionServicio.getLong("servicio_id")));
                        reparacionesServicios.add(reparacionServicio);
                    }
                    //reparacion.(reparacionesServicios);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparacion;
    }

    @Override
    public List<Reparacion> obtenerTodos() {
        String sqlReparacion = "SELECT * FROM Reparaciones";
        List<Reparacion> reparaciones = new ArrayList<>();

        try (Statement stmt = conexion.createStatement()) {
            ResultSet rsReparacion = stmt.executeQuery(sqlReparacion);
            while (rsReparacion.next()) {
                Reparacion reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getLong("id"));
                reparacion.setNombreE(rsReparacion.getString("nombreE"));

                // Aquí podrías cargar las relaciones ReparacionServicio si es necesario
                reparaciones.add(reparacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparaciones;
    }
}
