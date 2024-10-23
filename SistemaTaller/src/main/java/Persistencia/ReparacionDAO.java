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
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca());
            stmtReparacion.executeUpdate();

            ResultSet generatedKeys = stmtReparacion.getGeneratedKeys();
            if (generatedKeys.next()) {
                int reparacionId = generatedKeys.getInt(1);
                entity.setId(reparacionId);

                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                        stmtReparacionServicio.setInt(1, reparacionId);
                        stmtReparacionServicio.setInt(2, reparacionServicio.getServicio().getId_servicio());
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
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca());
            stmtReparacion.setInt(3, entity.getId());
            stmtReparacion.executeUpdate();

            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setInt(1, entity.getId());
                stmtDeleteReparacionServicio.executeUpdate();
            }

            try (PreparedStatement stmtInsertReparacionServicio = conexion.prepareStatement(sqlInsertReparacionServicio)) {
                for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                    stmtInsertReparacionServicio.setInt(1, entity.getId());
                    stmtInsertReparacionServicio.setInt(2, reparacionServicio.getServicio().getId_servicio());
                    stmtInsertReparacionServicio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public List<Reparacion> obtenerTodos() {
        String sqlReparacion = "SELECT * FROM Reparaciones";
        List<Reparacion> reparaciones = new ArrayList<>();

        try (Statement stmt = conexion.createStatement()) {
            ResultSet rsReparacion = stmt.executeQuery(sqlReparacion);
            while (rsReparacion.next()) {
                Reparacion reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getInt("id"));
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));
                reparaciones.add(reparacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparaciones;
    }

    @Override
    public void eliminar(Long id) {
        String sqlReparacionServicio = "DELETE FROM ReparacionServicio WHERE reparacion_id = ?";
        String sqlReparacion = "DELETE FROM Reparaciones WHERE id = ?";

        try {
            try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                stmtReparacionServicio.setLong(1, id);
                stmtReparacionServicio.executeUpdate();
            }

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
                reparacion.setId(rsReparacion.getInt("id"));
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));

                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    stmtReparacionServicio.setLong(1, id);
                    ResultSet rsReparacionServicio = stmtReparacionServicio.executeQuery();
                    List<ReparacionServicio> reparacionesServicios = new ArrayList<>();
                    while (rsReparacionServicio.next()) {
                        ReparacionServicio reparacionServicio = new ReparacionServicio();
                        reparacionServicio.setServicio(new ServicioDAO(conexion).obtenerPorId(rsReparacionServicio.getInt("servicio_id")));
                        reparacionesServicios.add(reparacionServicio);
                    }
                    reparacion.setReparacionServicios(reparacionesServicios);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparacion;
    }
}

