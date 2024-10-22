/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Oscar
 */
public class Conexion {
     private static final String URL = "jdbc:mysql://localhost:3306/SistemaTaller";
    private static final String USER = "root"; 
    private static final String PASSWORD = "1234"; 

    private static Connection connection = null;

    // Método para obtener la conexión
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");  // Cargar el driver de MySQL
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
                 } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
