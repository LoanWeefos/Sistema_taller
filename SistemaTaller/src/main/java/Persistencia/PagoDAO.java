/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Pago;
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
public class PagoDAO implements IPersistencia<Pago>{
    private Connection conexion;

    public PagoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregar(Pago pago) {
        String sqlPago = "INSERT INTO cliente (total, metodo, fecha, reparacion) VALUES (?, ?, ?, ?)";
        
        
        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {
            psPago.setDouble(1, pago.getTotal());
            psPago.setString(2, pago.getMetodo());
            psPago.setDate(3, new java.sql.Date(pago.getFecha().getTime()));
            psPago.setLong(4, pago.getReparacion().getId());

            int affectedRows = psPago.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = psPago.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pago.setId(generatedKeys.getLong(1)); // Establecer el ID generado
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
    }

    @Override
    public void actualizar(Pago pago) {
        String sqlPago = "UPDATE pago SET total = ?, metodo = ?, fecha = ?, reparacion_id = ? WHERE id = ?";
        
        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago)) {
            psPago.setDouble(1, pago.getTotal());
            psPago.setString(2, pago.getMetodo());
            psPago.setDate(3, new java.sql.Date(pago.getFecha().getTime()));
            psPago.setLong(4, pago.getReparacion().getId());
            psPago.setLong(5, pago.getId());

            psPago.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
     String sqlEliminarPago = "DELETE FROM pago WHERE id = ?";
        
        try (PreparedStatement psEliminarPago = conexion.prepareStatement(sqlEliminarPago)) {
            psEliminarPago.setLong(1, id);
            psEliminarPago.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pago obtenerPorId(Long id) {
    String sqlPago = "SELECT * FROM pago WHERE id = ?";
        Pago pago = null;
        
        try (PreparedStatement psPago = conexion.prepareStatement(sqlPago)) {
            psPago.setLong(1, id);
            try (ResultSet rsPago = psPago.executeQuery()) {
                if (rsPago.next()) {
                    pago = new Pago();
                    pago.setId(rsPago.getLong("id"));
                    pago.setTotal(rsPago.getDouble("total"));
                    pago.setMetodo(rsPago.getString("metodo"));
                    pago.setFecha(rsPago.getDate("fecha"));
                    pago.getReparacion().setId(rsPago.getLong("reparacion_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pago;
    }

    @Override
    public List<Pago> obtenerTodos() {
     String sqlPago = "SELECT * FROM pago";
        List<Pago> pagos = new ArrayList<>();
        
        try (Statement stmtPago = conexion.createStatement();
             ResultSet rsPago = stmtPago.executeQuery(sqlPago)) {
             
            while (rsPago.next()) {
                Pago pago = new Pago();
                pago.setId(rsPago.getLong("id"));
                pago.setTotal(rsPago.getDouble("total"));
                pago.setMetodo(rsPago.getString("metodo"));
                pago.setFecha(rsPago.getDate("fecha"));
                pago.getReparacion().setId(rsPago.getLong("reparacion_id"));
                pagos.add(pago);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pagos;
    }
    
}
