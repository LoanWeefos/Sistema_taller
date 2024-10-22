/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.ReparacionServicio;
import Dominio.Servicio;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author hoshi
 */
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
                Long servicioId = generatedKeys.getLong(1);
                entity.setId(servicioId);

                // Insertar las relaciones en la tabla ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    for (ReparacionServicio reparacionServicio : entity.getReparacionServicios()) {
                        stmtReparacionServicio.setLong(1, servicioId);
                        stmtReparacionServicio.setLong(2, reparacionServicio.getReparacion().getId());
                        stmtReparacionServicio.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Servicio entity) {
        String sqlServicio = "UPDATE Servicios SET descripcion = ?, costo = ? WHERE id = ?";
        String sqlDeleteReparacionServicio = "DELETE FROM ReparacionServicio WHERE servicio_id = ?";
        String sqlInsertReparacionServicio = "INSERT INTO ReparacionServicio (servicio_id, reparacion_id) VALUES (?, ?)";

        try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
            // Actualizar el Servicio
            stmtServicio.setString(1, entity.getDescripcion());
            stmtServicio.setDouble(2, entity.getCosto());
            stmtServicio.setLong(3, entity.getId());
            stmtServicio.executeUpdate();

            // Eliminar las relaciones existentes en la tabla ReparacionServicio
            try (PreparedStatement stmtDeleteReparacionServicio = conexion.prepareStatement(sqlDeleteReparacionServicio)) {
                stmtDeleteReparacionServicio.setLong(1, entity.getId());
                stmtDeleteReparacionServicio.executeUpdate();
            }

            // Insertar las nuevas relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtInsertReparacionServicio = conexion.prepareStatement(sqlInsertReparacionServicio)) {
                for (ReparacionServicio reparacionServicio :  entity.getReparacionServicios()) {
                    stmtInsertReparacionServicio.setLong(1, entity.getId());
                    stmtInsertReparacionServicio.setLong(2, reparacionServicio.getReparacion().getId());
                    stmtInsertReparacionServicio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        String sqlReparacionServicio = "DELETE FROM ReparacionServicio WHERE servicio_id = ?";
        String sqlServicio = "DELETE FROM Servicios WHERE id = ?";

        try {
            // Eliminar las relaciones en la tabla ReparacionServicio
            try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                stmtReparacionServicio.setLong(1, id);
                stmtReparacionServicio.executeUpdate();
            }

            // Eliminar el Servicio
            try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
                stmtServicio.setLong(1, id);
                stmtServicio.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Servicio obtenerPorId(Long id) {
        String sqlServicio = "SELECT * FROM Servicios WHERE id = ?";
        String sqlReparacionServicio = "SELECT * FROM ReparacionServicio WHERE servicio_id = ?";
        Servicio servicio = null;

        try (PreparedStatement stmtServicio = conexion.prepareStatement(sqlServicio)) {
            stmtServicio.setLong(1, id);
            ResultSet rsServicio = stmtServicio.executeQuery();
            if (rsServicio.next()) {
                servicio = new Servicio();
                servicio.setId(rsServicio.getLong("id"));
                servicio.setDescripcion(rsServicio.getString("descripcion"));
                servicio.setCosto(rsServicio.getDouble("costo"));

                // Obtener las relaciones ReparacionServicio
                try (PreparedStatement stmtReparacionServicio = conexion.prepareStatement(sqlReparacionServicio)) {
                    stmtReparacionServicio.setLong(1, id);
                    ResultSet rsReparacionServicio = stmtReparacionServicio.executeQuery();
                    List<ReparacionServicio> reparacionesServicio = new ArrayList<>();
                    while (rsReparacionServicio.next()) {
                        ReparacionServicio reparacionServicio = new ReparacionServicio();
                        // Aquí deberías cargar la entidad Reparacion y asignarla a reparacionServicio
                        reparacionesServicio.add(reparacionServicio);
                    }
                    servicio.setReparacionServicios(reparacionesServicio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                servicio.setId(rsServicio.getLong("id"));
                servicio.setDescripcion(rsServicio.getString("descripcion"));
                servicio.setCosto(rsServicio.getDouble("costo"));

                // Aquí podrías cargar las relaciones ReparacionServicio si es necesario
                servicios.add(servicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicios;
    }
}
