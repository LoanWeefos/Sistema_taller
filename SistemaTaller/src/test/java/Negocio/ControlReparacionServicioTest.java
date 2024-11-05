/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.ReparacionServicio;
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
public class ControlReparacionServicioTest {
    private static Connection conexion;
    private ControlReparacionServicio controlReparacionServicio;
    
     @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlReparacionServicio = new ControlReparacionServicio();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        conexion.createStatement().executeUpdate("DELETE FROM ReparacionServicio WHERE id = LAST_INSERT_ID()");
    }
    
    @Test
    public void agregarReparacionServicioTest(){
        ReparacionServicio reparacionServicio = new ReparacionServicio();

        controlReparacionServicio.agregarReparacionServicio(reparacionServicio);
        
        // Verificar que se haya agregado correctamente
        ReparacionServicio agregado = controlReparacionServicio.obtenerReparacionServicioPorId(reparacionServicio.getId_repserv());
        assertNotNull(agregado);
        // Agregar más aserciones según sea necesario para validar los datos
    }
    
    @Test
    public void actualizarReparacionServicioTest(){
        
     // Primero agregamos un servicio para actualizar
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        // Configura los atributos de reparacionServicio y luego agrega
        // controlReparacionServicio.agregarReparacionServicio(reparacionServicio);

        // Supongamos que el ID es conocido
        int id = reparacionServicio.getId_repserv();
        //reparacionServicio.s("Reparación de motor actualizada");
        controlReparacionServicio.actualizarReparacionServicio(reparacionServicio);

        // Verificar que se haya actualizado correctamente
        ReparacionServicio actualizado = controlReparacionServicio.obtenerReparacionServicioPorId(id);
        //assertEquals("Reparación de motor actualizada", actualizado.getDescripcion());
    }
    
    @Test
    public void eliminarReparacionServicioTest(){
        // Primero agregamos un servicio para eliminar
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        // Configura los atributos de reparacionServicio y luego agrega
        // controlReparacionServicio.agregarReparacionServicio(reparacionServicio);
        
        int id = reparacionServicio.getId_repserv();
        controlReparacionServicio.eliminarReparacionServicio(id);
        
        // Verificar que se haya eliminado correctamente
        ReparacionServicio eliminado = controlReparacionServicio.obtenerReparacionServicioPorId(id);
        assertNull(eliminado);
    
    }
    
    @Test
    public void obtenerReparacionServicioTest(){
        // Primero agregamos un servicio para obtener
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        // Configura los atributos de reparacionServicio y luego agrega
        // controlReparacionServicio.agregarReparacionServicio(reparacionServicio);
        
        int id = reparacionServicio.getId_repserv();
        ReparacionServicio obtenido = controlReparacionServicio.obtenerReparacionServicioPorId(id);
        
        assertNotNull(obtenido);
        // Agregar más aserciones según sea necesario para validar los datos
    
    }
    
    @Test
    public void listarReparacionServicioTest(){
        // Primero agregamos algunos servicios para listar
        ReparacionServicio reparacionServicio1 = new ReparacionServicio();
        // Configura los atributos y agrega
        // controlReparacionServicio.agregarReparacionServicio(reparacionServicio1);

        ReparacionServicio reparacionServicio2 = new ReparacionServicio();
        // Configura los atributos y agrega
        // controlReparacionServicio.agregarReparacionServicio(reparacionServicio2);

        List<ReparacionServicio> lista = controlReparacionServicio.obtenerTodasLasReparacionesServicios();
        
        assertFalse(lista.isEmpty());
        // Agregar más aserciones según sea necesario para validar los datos en la lista
    
    }
    
     @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
    
}
