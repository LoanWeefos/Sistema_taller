/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Oscar
 */
public class main {
     public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            // Aquí puedes ejecutar una consulta para probar la conexión
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES;");
            
            while (rs.next()) {
                System.out.println("Tabla: " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();  // Asegúrate de cerrar la conexión
        }
    }
    
}
