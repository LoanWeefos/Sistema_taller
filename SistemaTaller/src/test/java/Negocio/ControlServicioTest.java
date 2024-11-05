/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.ReparacionServicio;
import Dominio.Servicio;
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
public class ControlServicioTest {
    private static Connection conexion;
    private ControlServicio controlServicio;
    
     @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlServicio = new ControlServicio();
    }
    

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        conexion.createStatement().executeUpdate("DELETE FROM Servicios WHERE descripcion = 'TEST_SERVICIO'");
    }
    
    @Test
    public void agregarServicioTest(){
        List<ReparacionServicio> reparacionServicios = new ArrayList<>();
        ReparacionServicio reparacionServicio = new ReparacionServicio(); // Crear un objeto de prueba
        reparacionServicios.add(reparacionServicio);
        
        controlServicio.agregarServicio("TEST_SERVICIO", 100.0, reparacionServicios);
        
        // Verificar que el servicio se haya agregado correctamente
        // Aquí necesitarías un método en el ServicioDAO para obtener por ID o descripción
        Servicio servicio = controlServicio.obtenerServicioPorId(1); // Asumiendo que este método ya existe
        assertNotNull(servicio);
        assertEquals("TEST_SERVICIO", servicio.getDescripcion());
        assertEquals(100.0, servicio.getCosto());
    
    }
    
    @Test
    public void actualizarServicioTest(){
        List<ReparacionServicio> reparacionServicios = new ArrayList<>();
        ReparacionServicio reparacionServicio = new ReparacionServicio(); // Crear un objeto de prueba
        reparacionServicios.add(reparacionServicio);
        
        controlServicio.agregarServicio("TEST_SERVICIO", 100.0, reparacionServicios);
        
        // Actualizar el servicio
        controlServicio.actualizarServicio(1, "SERVICIO_ACTUALIZADO", 150.0, reparacionServicios);
        
        // Verificar que el servicio se haya actualizado correctamente
        Servicio servicioActualizado = controlServicio.obtenerServicioPorId(1);
        assertNotNull(servicioActualizado);
        assertEquals("SERVICIO_ACTUALIZADO", servicioActualizado.getDescripcion());
        assertEquals(150.0, servicioActualizado.getCosto());
    
    }
    
    @Test
    public void eliminarServicioTest(){
        List<ReparacionServicio> reparacionServicios = new ArrayList<>();
        ReparacionServicio reparacionServicio = new ReparacionServicio(); // Crear un objeto de prueba
        reparacionServicios.add(reparacionServicio);
        
        controlServicio.agregarServicio("TEST_SERVICIO", 100.0, reparacionServicios);
        
        // Eliminar el servicio
        controlServicio.eliminarServicio(1);
        
        // Verificar que el servicio se haya eliminado
        Servicio servicioEliminado = controlServicio.obtenerServicioPorId(1);
        assertNull(servicioEliminado);
    
    }
    
    @Test
    public void obtenerServicioTest(){
        List<ReparacionServicio> reparacionServicios = new ArrayList<>();
        ReparacionServicio reparacionServicio = new ReparacionServicio(); // Crear un objeto de prueba
        reparacionServicios.add(reparacionServicio);
        
        controlServicio.agregarServicio("TEST_SERVICIO", 100.0, reparacionServicios);
        
        // Obtener el servicio por ID
        Servicio servicio = controlServicio.obtenerServicioPorId(1);
        assertNotNull(servicio);
        assertEquals("TEST_SERVICIO", servicio.getDescripcion());
    
    }
    
    @Test
    public void listarServicioTest(){
         // Agregar un par de servicios de prueba
        List<ReparacionServicio> reparacionServicios = new ArrayList<>();
        ReparacionServicio reparacionServicio = new ReparacionServicio(); // Crear un objeto de prueba
        reparacionServicios.add(reparacionServicio);
        
        controlServicio.agregarServicio("SERVICIO_1", 100.0, reparacionServicios);
        controlServicio.agregarServicio("SERVICIO_2", 200.0, reparacionServicios);
        
        // Listar servicios
        List<Servicio> servicios = controlServicio.listarServicios();
        assertEquals(2, servicios.size()); // Verificar que se hayan agregado correctamente
    
    }
    
    
    
     @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}
