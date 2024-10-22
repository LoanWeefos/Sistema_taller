/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Reparacion;
import Dominio.ReparacionServicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReparacionDAO implements IPersistencia<Reparacion> {
    private Connection conexion;

    public ReparacionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Reparacion entity) {
        String sqlReparacion = "INSERT INTO Reparaciones (nombre_empleado, placa_vehiculo) VALUES (?, ?)";
        String sqlReparacionServicio = "INSERT INTO ReparacionServicio (reparacion_id, servicio_id) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion, Statement.RETURN_GENERATED_KEYS)) {
            // Insertar la Reparacion
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca()); // Cambiar a placa
            stmtReparacion.executeUpdate();

            // Obtener el ID generado para la Reparacion
            ResultSet generatedKeys = stmtReparacion.getGeneratedKeys();
            if (generatedKeys.next()) {
                int reparacionId = generatedKeys.getInt(1); // Cambiado a int
                entity.setId(reparacionId);

                // Insertar las relaciones en la tabla ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                        stmtReparacionServicio.setInt(1, reparacionId); // Cambiado a int
                        stmtReparacionServicio.setInt(2, reparacionServicio.getServicio().getId_servicio()); // Cambiado a int
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
        String sqlReparacion = "UPDATE Reparaciones SET nombre_empleado = ?, placa_vehiculo = ? WHERE id = ?";
        String sqlDeleteReparacionServicio = "DELETE FROM ReparacionServicio WHERE reparacion_id = ?";
        String sqlInsertReparacionServicio = "INSERT INTO ReparacionServicio (reparacion_id, servicio_id) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            // Actualizar la Reparacion
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca()); // Cambiar a placa
            stmtReparacion.setInt(3, entity.getId()); // Cambiado a int
            stmtReparacion.executeUpdate();

            // Eliminar las relaciones existentes en la tabla ReparacionServicio
            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setInt(1, entity.getId()); // Cambiado a int
                stmtDeleteReparacionServicio.executeUpdate();
            }

            // Insertar las nuevas relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtInsertReparacionServicio = conexion.prepareStatement(sqlInsertReparacionServicio)) {
                for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                    stmtInsertReparacionServicio.setInt(1, entity.getId()); // Cambiado a int
                    stmtInsertReparacionServicio.setInt(2, reparacionServicio.getServicio().getId_servicio()); // Cambiado a int
                    stmtInsertReparacionServicio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) { // Cambiado a int
        String sqlReparacionServicio = "DELETE FROM ReparacionServicio WHERE reparacion_id = ?";
        String sqlReparacion = "DELETE FROM Reparaciones WHERE id = ?";

        try {
            // Eliminar las relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                stmtReparacionServicio.setInt(1, id); // Cambiado a int
                stmtReparacionServicio.executeUpdate();
            }

            // Eliminar la Reparacion
            try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
                stmtReparacion.setInt(1, id); // Cambiado a int
                stmtReparacion.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Reparacion obtenerPorId(int id) { // Cambiado a int
        String sqlReparacion = "SELECT * FROM Reparaciones WHERE id = ?";
        String sqlReparacionServicio = "SELECT * FROM ReparacionServicio WHERE reparacion_id = ?";
        Reparacion reparacion = null;

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            stmtReparacion.setInt(1, id); // Cambiado a int
            ResultSet rsReparacion = stmtReparacion.executeQuery();
            if (rsReparacion.next()) {
                reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getInt("id")); // Cambiado a int
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));

                // Obtener las relaciones ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    stmtReparacionServicio.setInt(1, id); // Cambiado a int
                    ResultSet rsReparacionServicio = stmtReparacionServicio.executeQuery();
                    List<ReparacionServicio> reparacionesServicios = new ArrayList<>();
                    while (rsReparacionServicio.next()) {
                        ReparacionServicio reparacionServicio = new ReparacionServicio();
                        // Aquí deberías cargar la entidad Servicio y asignarla a reparacionServicio
                        // reparacionServicio.setServicio(new ServicioDAO(conexion).obtenerPorId(rsReparacionServicio.getInt("servicio_id"))); // Cambiado a int
                        reparacionesServicios.add(reparacionServicio);
                    }
                    reparacion.setReparacionServicios(reparacionesServicios); // Asegúrate de asignar la lista de servicios
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
                reparacion.setId(rsReparacion.getInt("id")); // Cambiado a int
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));

                // Aquí podrías cargar las relaciones ReparacionServicio si es necesario
                reparaciones.add(reparacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparaciones;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Reparacion obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

