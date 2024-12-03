/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Reparacion;
import IPersistencia.IPersistencia;
import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Dominio.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para la gestión de operaciones CRUD de la entidad Servicio utilizando
 * JDBC.
 */
public class ServicioDAO implements IPersistencia<Servicio> {

    private Connection conexion;

    public ServicioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Servicio entity) {
        String sqlServicio = "INSERT INTO Servicios (descripcion, costo) VALUES (?, ?)";
        String sqlReparacionServicio = "INSERT INTO Reparaciones_Servicios (id_reparacion, id_servicio) VALUES (?, ?)";

        try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio, Statement.RETURN_GENERATED_KEYS)) {
            // Insertar el Servicio
            stmtServicio.setString(1, entity.getDescripcion());
            stmtServicio.setDouble(2, entity.getCosto());
            stmtServicio.executeUpdate();

            // Obtener el ID generado para el Servicio
            ResultSet generatedKeys = stmtServicio.getGeneratedKeys();
            if (generatedKeys.next()) {
                int servicioId = generatedKeys.getInt(1);  // Cambiado a int
                entity.setId_servicio(servicioId); // Asignar ID al objeto Servicio

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar el servicio", e);
        }
    }

    @Override
    public void actualizar(Servicio entity) {
        String sqlServicio = "UPDATE Servicios SET descripcion = ?, costo = ? WHERE id_servicio = ?";  // Cambiado a id_servicio
        String sqlDeleteReparacionServicio = "DELETE FROM Reparaciones_Servicios WHERE id_servicio = ?";
        String sqlInsertReparacionServicio = "INSERT INTO Reparaciones_Servicios (id_reparacion, id_servicio) VALUES (?, ?)";

        try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
            // Actualizar el Servicio
            stmtServicio.setString(1, entity.getDescripcion());
            stmtServicio.setDouble(2, entity.getCosto());
            stmtServicio.setInt(3, entity.getId_servicio()); // Cambiado a int
            stmtServicio.executeUpdate();

            // Eliminar las relaciones existentes en la tabla ReparacionServicio
            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setInt(1, entity.getId_servicio()); // Cambiado a int
                stmtDeleteReparacionServicio.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    public void eliminar(int id) {  // Cambiado a int
        String sqlReparacionServicio = "DELETE FROM Reparaciones_Servicios WHERE id_servicio = ?";
        String sqlServicio = "DELETE FROM Servicios WHERE id_servicio = ?";  // Cambiado a id_servicio

        try {
            // Eliminar las relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                stmtReparacionServicio.setInt(1, id);  // Cambiado a int
                stmtReparacionServicio.executeUpdate();
            }

            // Eliminar el Servicio
            try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
                stmtServicio.setInt(1, id);  // Cambiado a int
                stmtServicio.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el servicio", e);
        }
    }

    public Servicio obtenerPorId(int id) {  // Cambiado a int
        String sqlServicio = "SELECT * FROM Servicios WHERE id_servicio = ?";  // Cambiado a id_servicio
        String sqlReparacionServicio = "SELECT * FROM Reparaciones_Servicios WHERE id_servicio = ?";
        Servicio servicio = null;

        ReparacionDAO reparacionDAO = new ReparacionDAO(conexion); // Instancia de ReparacionDAO

        try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
            stmtServicio.setInt(1, id);  // Cambiado a int
            ResultSet rsServicio = stmtServicio.executeQuery();
            if (rsServicio.next()) {
                servicio = new Servicio();
                servicio.setId_servicio(rsServicio.getInt("id_servicio")); // Cambiado a int
                servicio.setDescripcion(rsServicio.getString("descripcion"));
                servicio.setCosto(rsServicio.getDouble("costo"));

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el servicio por ID", e);
        }
        return servicio;
    }

    @Override
    public List<Servicio> obtenerTodos() {
        String sqlServicio = "SELECT * FROM Servicios";
        List<Servicio> servicios = new ArrayList<>();

        try (Statement stmt = conexion.createStatement()) {
            ResultSet rsServicio = stmt.executeQuery(sqlServicio);
            while (rsServicio.next()) {
                Servicio servicio = new Servicio();
                servicio.setId_servicio(rsServicio.getInt("id_servicio")); // Cambiado a int
                servicio.setDescripcion(rsServicio.getString("descripcion"));
                servicio.setCosto(rsServicio.getDouble("costo"));

                // Aquí podrías cargar las relaciones ReparacionServicio si es necesario
                servicios.add(servicio);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los servicios", e);
        }
        return servicios;
    }

    public List<Servicio> obtenerServiciosPorPlaca(String placa) {
        List<Servicio> servicios = new ArrayList<>();

        // SQL ajustado para usar la placa como referencia
        String sql = "SELECT s.descripcion, s.costo "
                + "FROM servicios s "
                + "JOIN reparaciones_servicios sr ON s.id_servicio = sr.id_servicio "
                + "JOIN reparaciones r ON r.id = sr.id_reparacion "
                + "JOIN vehiculos v ON v.placa = r.placa_vehiculo "
                + "WHERE v.placa = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, placa);  // Establecemos la placa como parámetro en la consulta
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String descripcion = rs.getString("descripcion");
                double costo = rs.getDouble("costo");
                servicios.add(new Servicio(descripcion, costo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servicios;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
