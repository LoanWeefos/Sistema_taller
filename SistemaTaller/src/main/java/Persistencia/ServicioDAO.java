/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.ReparacionServicio;
import Dominio.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para la gestión de operaciones CRUD de la entidad Servicio utilizando JDBC.
 */
public class ServicioDAO implements IPersistencia<Servicio> {
    private Connection conexion;

    public ServicioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Servicio entity) {
        String sqlServicio = "INSERT INTO Servicios (descripcion, costo) VALUES (?, ?)";
        String sqlReparacionServicio = "INSERT INTO ReparacionServicio (servicio_id, reparacion_id) VALUES (?, ?)";

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

                // Insertar las relaciones en la tabla ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                        stmtReparacionServicio.setInt(1, servicioId);  // Cambiado a int
                        stmtReparacionServicio.setInt(2, reparacionServicio.getReparacion().getId());  // Cambiado a int
                        stmtReparacionServicio.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar el servicio", e);
        }
    }

    @Override
    public void actualizar(Servicio entity) {
        String sqlServicio = "UPDATE Servicios SET descripcion = ?, costo = ? WHERE id_servicio = ?";  // Cambiado a id_servicio
        String sqlDeleteReparacionServicio = "DELETE FROM ReparacionServicio WHERE servicio_id = ?";
        String sqlInsertReparacionServicio = "INSERT INTO ReparacionServicio (servicio_id, reparacion_id) VALUES (?, ?)";

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

            // Insertar las nuevas relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtInsertReparacionServicio = conexion.prepareStatement(sqlInsertReparacionServicio)) {
                for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                    stmtInsertReparacionServicio.setInt(1, entity.getId_servicio()); // Cambiado a int
                    stmtInsertReparacionServicio.setInt(2, reparacionServicio.getReparacion().getId()); // Cambiado a int
                    stmtInsertReparacionServicio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    public void eliminar(int id) {  // Cambiado a int
        String sqlReparacionServicio = "DELETE FROM ReparacionServicio WHERE servicio_id = ?";
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
        String sqlReparacionServicio = "SELECT * FROM ReparacionServicio WHERE servicio_id = ?";
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

                // Obtener las relaciones ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    stmtReparacionServicio.setInt(1, id);  // Cambiado a int
                    ResultSet rsReparacionServicio = stmtReparacionServicio.executeQuery();
                    List<ReparacionServicio> reparacionesServicio = new ArrayList<>();
                    while (rsReparacionServicio.next()) {
                        ReparacionServicio reparacionServicio = new ReparacionServicio();
                        // Aquí, cambiamos el método para que reciba un int
                        reparacionServicio.setReparacion(reparacionDAO.obtenerPorId(rsReparacionServicio.getInt("reparacion_id"))); // Cargar la Reparacion
                        reparacionesServicio.add(reparacionServicio);
                    }
                    servicio.setReparacionServicios(reparacionesServicio);
                }
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

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

