/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import IPersistencia.IPersistencia;
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
        String sqlReparacionServicio = "INSERT INTO Reparaciones_Servicios (id_reparacion, id_servicio) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion, Statement.RETURN_GENERATED_KEYS)) {
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca());
            stmtReparacion.executeUpdate();

            ResultSet generatedKeys = stmtReparacion.getGeneratedKeys();
            if (generatedKeys.next()) {
                int reparacionId = generatedKeys.getInt(1);
                entity.setId(reparacionId);

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Reparacion entity) {
        String sqlReparacion = "UPDATE Reparaciones SET nombre_empleado = ?, placa_vehiculo = ? WHERE id = ?";
        String sqlDeleteReparacionServicio = "DELETE FROM Reparaciones_Servicios WHERE id_reparacion = ?";
        String sqlInsertReparacionServicio = "INSERT INTO Reparaciones_Servicios (id_reparacion, id_servicio) VALUES (?, ?)";

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            stmtReparacion.setString(1, entity.getNombre_empleado());
            stmtReparacion.setString(2, entity.getVehiculo().getPlaca());
            stmtReparacion.setLong(3, entity.getId());
            stmtReparacion.executeUpdate();

            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setLong(1, entity.getId());
                stmtDeleteReparacionServicio.executeUpdate();
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
        String sqlReparacionServicio = "DELETE FROM Reparaciones_Servicios WHERE id_reparacion = ?";
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
        String sqlReparacionServicio = "SELECT * FROM Reparaciones_Servicios WHERE id_reparacion = ?";
        Reparacion reparacion = null;

        try (PreparedStatement stmtReparacion = conexion.prepareStatement(sqlReparacion)) {
            stmtReparacion.setLong(1, id);
            ResultSet rsReparacion = stmtReparacion.executeQuery();
            if (rsReparacion.next()) {
                reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getInt("id"));
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));

                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparacion;
    }
}

