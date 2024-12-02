/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import IPersistencia.IPersistencia;
import Dominio.Reparacion;
import Dominio.ReparacionServicio;
import Dominio.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReparacionDAO implements IPersistencia<Reparacion> {

    private Connection conexion;
    Vehiculo vehiculoDAO;

    public ReparacionDAO(Connection conexion) {
        this.conexion = conexion;
        vehiculoDAO = new Vehiculo();
    }

    @Override
    public void agregar(Reparacion entity) {
        String sqlReparacion = "INSERT INTO Reparaciones (nombre_empleado, placa_vehiculo) VALUES (?, ?)";

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

    private Vehiculo obtenerVehiculo(String placa) {
        Vehiculo vehiculo = null;
        VehiculoDAO vehiculoDAO = new VehiculoDAO(conexion);

        vehiculo = vehiculoDAO.obtenerPorId(placa);

        return vehiculo;
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

                // Obtener la placa del vehículo y asociarlo al objeto Reparacion
                String placaVehiculo = rsReparacion.getString("placa_vehiculo");
                Vehiculo vehiculo = obtenerVehiculo(placaVehiculo); // Asegúrate de que obtenerVehiculo esté bien implementado
                reparacion.setVehiculo(vehiculo);

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
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setPlaca(rsReparacion.getString("placa_vehiculo"));
                reparacion.setVehiculo(vehiculo);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparacion;
    }

    public int agregarRepKey(Reparacion entity) {
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
                return reparacionId;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Reparacion> obtenerPorPlaca(String placa) {
        String sqlReparacion = "SELECT * FROM Reparaciones WHERE placa_vehiculo = ?";
        List<Reparacion> reparaciones = new ArrayList<>();

        try (PreparedStatement stmt = conexion.prepareStatement(sqlReparacion)) {
            stmt.setString(1, placa);
            ResultSet rsReparacion = stmt.executeQuery();

            while (rsReparacion.next()) {
                Reparacion reparacion = new Reparacion();
                reparacion.setId(rsReparacion.getInt("id"));
                reparacion.setNombre_empleado(rsReparacion.getString("nombre_empleado"));

                // Obtener el vehículo asociado a la placa
                Vehiculo vehiculo = obtenerVehiculo(placa); // Método ya existente
                reparacion.setVehiculo(vehiculo);

                reparaciones.add(reparacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reparaciones;
    }

}
