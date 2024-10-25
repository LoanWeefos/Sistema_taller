/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;
import Dominio.Reparacion;
import Dominio.ReparacionServicio;
import Dominio.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Oscar
 */
public class ReparacionServicioDAO {
    
 private Connection conexion;

    public ReparacionServicioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void agregar(ReparacionServicio reparacionServicio) {
        String sql = "INSERT INTO Reparaciones_Servicios (id_reparacion, id_servicio) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reparacionServicio.getReparacion().getId());
            ps.setInt(2, reparacionServicio.getServicio().getId_servicio());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reparacionServicio.setId_repserv(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(ReparacionServicio reparacionServicio) {
        String sql = "UPDATE Reparaciones_Servicios SET id_reparacion = ?, id_servicio = ? WHERE id_repserv = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, reparacionServicio.getReparacion().getId());
            ps.setInt(2, reparacionServicio.getServicio().getId_servicio());
            ps.setInt(3, reparacionServicio.getId_repserv());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM Reparaciones_Servicios WHERE id_repserv = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReparacionServicio obtenerPorId(int id) {
        String sql = "SELECT * FROM Reparaciones_Servicios WHERE id_repserv = ?";
        ReparacionServicio reparacionServicio = null;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reparacionServicio = new ReparacionServicio();
                    reparacionServicio.setId_repserv(rs.getInt("id_repserv"));

                    Reparacion reparacion = new Reparacion();
                    reparacion.setId(rs.getInt("id_reparacion"));
                    reparacionServicio.setReparacion(reparacion);

                    Servicio servicio = new Servicio();
                    servicio.setId_servicio(rs.getInt("id_servicio"));
                    reparacionServicio.setServicio(servicio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reparacionServicio;
    }

    public List<ReparacionServicio> obtenerTodos() {
        String sql = "SELECT * FROM Reparaciones_Servicios";
        List<ReparacionServicio> reparacionesServicios = new ArrayList<>();

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ReparacionServicio reparacionServicio = new ReparacionServicio();
                reparacionServicio.setId_repserv(rs.getInt("id_repserv"));

                Reparacion reparacion = new Reparacion();
                reparacion.setId(rs.getInt("id_reparacion"));
                reparacionServicio.setReparacion(reparacion);

                Servicio servicio = new Servicio();
                servicio.setId_servicio(rs.getInt("id_servicio"));
                reparacionServicio.setServicio(servicio);

                reparacionesServicios.add(reparacionServicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reparacionesServicios;
    }
}
