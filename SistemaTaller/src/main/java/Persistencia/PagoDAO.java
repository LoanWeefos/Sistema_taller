/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Pago;
import Dominio.Reparacion;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author hoshi
 */
public class PagoDAO implements IPersistencia<Pago> {
    private Connection conexion;

    public PagoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Pago pago) {
        String sqlPago = "INSERT INTO Pagos (total, metodo, fecha, reparacion_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {
            psPago.setDouble(1, pago.getTotal());
            psPago.setString(2, pago.getMetodo());
            psPago.setTimestamp(3, java.sql.Timestamp.valueOf(pago.getFecha())); // Cambiado a Timestamp
            psPago.setInt(4, pago.getReparacion().getId()); // Cambiado a setInt

            int affectedRows = psPago.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = psPago.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pago.setId(generatedKeys.getInt(1)); // Cambiado a setInt
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Pago pago) {
        String sqlPago = "UPDATE Pagos SET total = ?, metodo = ?, fecha = ?, reparacion_id = ? WHERE id = ?";

        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago)) {
            psPago.setDouble(1, pago.getTotal());
            psPago.setString(2, pago.getMetodo());
            psPago.setTimestamp(3, java.sql.Timestamp.valueOf(pago.getFecha())); // Cambiado a Timestamp
            psPago.setInt(4, pago.getReparacion().getId()); // Cambiado a setInt
            psPago.setInt(5, pago.getId()); // Cambiado a setInt

            psPago.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) { // Cambiado a int
        String sqlEliminarPago = "DELETE FROM Pagos WHERE id = ?";

        try (PreparedStatement psEliminarPago = conexion.prepareStatement(sqlEliminarPago)) {
            psEliminarPago.setInt(1, id); // Cambiado a setInt
            psEliminarPago.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pago obtenerPorId(int id) { // Cambiado a int
        String sqlPago = "SELECT * FROM Pagos WHERE id = ?";
        Pago pago = null;

        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago)) {
            psPago.setInt(1, id); // Cambiado a setInt
            try (ResultSet rsPago = psPago.executeQuery()) {
                if (rsPago.next()) {
                    pago = new Pago();
                    pago.setId(rsPago.getInt("id")); // Cambiado a getInt
                    pago.setTotal(rsPago.getDouble("total"));
                    pago.setMetodo(rsPago.getString("metodo"));
                    pago.setFecha(rsPago.getTimestamp("fecha").toLocalDateTime()); // Convertir a LocalDateTime
                    Reparacion reparacion = new Reparacion();
                    reparacion.setId(rsPago.getInt("reparacion_id")); // Cambiado a getInt
                    pago.setReparacion(reparacion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pago;
    }

    @Override
    public List<Pago> obtenerTodos() {
        String sqlPago = "SELECT * FROM Pagos";
        List<Pago> pagos = new ArrayList<>();

        try (Statement stmtPago = conexion.createStatement();
             ResultSet rsPago = stmtPago.executeQuery(sqlPago)) {

            while (rsPago.next()) {
                Pago pago = new Pago();
                pago.setId(rsPago.getInt("id")); // Cambiado a getInt
                pago.setTotal(rsPago.getDouble("total"));
                pago.setMetodo(rsPago.getString("metodo"));
                pago.setFecha(rsPago.getTimestamp("fecha").toLocalDateTime()); // Convertir a LocalDateTime
                Reparacion reparacion = new Reparacion();
                reparacion.setId(rsPago.getInt("reparacion_id")); // Cambiado a getInt
                pago.setReparacion(reparacion);
                pagos.add(pago);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pagos;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Pago obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

