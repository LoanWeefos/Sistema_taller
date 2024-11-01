/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Persistencia.Conexion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hoshi
 */
public class ControlReparacionTest {
    private static Connection conexion;
    private ControlReparacion controlReparacion;
    
     @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlReparacion = new ControlReparacion(conexion);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        // Ejemplo: clienteDAO.eliminar("TEST1234");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'TEST1'");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'RFC1'");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'RFC2'");
    }
    
    @Test
    public void agregarReparacionTest(){
    
    }
    
    @Test
    public void actualizarReparacionTest(){
    
    }
    
    @Test
    public void eliminarReparacionTest(){
    
    }
    
    @Test
    public void obtenerReparacionTest(){
    
    }
    
    @Test
    public void listarReparacionTest(){
    
    }
    
     @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}
