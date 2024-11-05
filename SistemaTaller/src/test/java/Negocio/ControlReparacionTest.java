/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Reparacion;
import Dominio.Vehiculo;
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
        controlReparacion = new ControlReparacion();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        conexion.createStatement().executeUpdate("DELETE FROM Reparacion WHERE id = LAST_INSERT_ID()");
    }
    
    @Test
    public void agregarReparacionTest(){
     // Crear un vehículo de prueba (asegúrate de que exista la clase Vehiculo y esté configurada)
        Vehiculo vehiculo = new Vehiculo(); // Configura atributos de Vehiculo según sea necesario
        vehiculo.setPlaca("TEST1234");

        // Crear una reparación
        Reparacion reparacion = new Reparacion();
        reparacion.setNombre_empleado("Juan Perez");
        reparacion.setVehiculo(vehiculo);
        reparacion.setReparacionServicios(new ArrayList<>()); // Si tienes servicios de reparación, agrégales

        // Agregar la reparación
        controlReparacion.agregarReparacion(reparacion);

        // Verificar que se haya agregado correctamente
        Reparacion agregado = controlReparacion.obtenerReparacionPorId(reparacion.getId());
        assertNotNull(agregado);
        assertEquals("Juan Perez", agregado.getNombre_empleado());
        assertEquals("TEST1234", agregado.getVehiculo().getPlaca());
    }
    
    @Test
    public void actualizarReparacionTest(){
        // Primero agregamos una reparación para actualizar
        Reparacion reparacion = new Reparacion();
        reparacion.setNombre_empleado("Juan Perez");
        reparacion.setVehiculo(new Vehiculo()); // Asegúrate de que este objeto esté configurado adecuadamente
        controlReparacion.agregarReparacion(reparacion);

        // Supongamos que el ID es conocido
        int id = reparacion.getId();
        reparacion.setNombre_empleado("Maria Gomez"); // Cambiamos el nombre del empleado
        controlReparacion.actualizarReparacion(reparacion);

        // Verificar que se haya actualizado correctamente
        Reparacion actualizado = controlReparacion.obtenerReparacionPorId(id);
        assertEquals("Maria Gomez", actualizado.getNombre_empleado());
    
    }
    
    @Test
    public void eliminarReparacionTest(){
         // Primero agregamos una reparación para eliminar
        Reparacion reparacion = new Reparacion();
        reparacion.setNombre_empleado("Juan Perez");
        reparacion.setVehiculo(new Vehiculo()); // Asegúrate de que este objeto esté configurado adecuadamente
        controlReparacion.agregarReparacion(reparacion);

        int id = reparacion.getId();
        controlReparacion.eliminarReparacion(id);

        // Verificar que se haya eliminado correctamente
        Reparacion eliminado = controlReparacion.obtenerReparacionPorId(id);
        assertNull(eliminado);
    
    }
    
    @Test
    public void obtenerReparacionTest(){
    
          // Primero agregamos una reparación para obtener
        Reparacion reparacion = new Reparacion();
        reparacion.setNombre_empleado("Juan Perez");
        reparacion.setVehiculo(new Vehiculo()); // Asegúrate de que este objeto esté configurado adecuadamente
        controlReparacion.agregarReparacion(reparacion);

        int id = reparacion.getId();
        Reparacion obtenido = controlReparacion.obtenerReparacionPorId(id);

        assertNotNull(obtenido);
        assertEquals("Juan Perez", obtenido.getNombre_empleado());
    }
    
    @Test
    public void listarReparacionTest(){
         // Primero agregamos algunas reparaciones para listar
        Reparacion reparacion1 = new Reparacion();
        reparacion1.setNombre_empleado("Juan Perez");
        reparacion1.setVehiculo(new Vehiculo()); // Asegúrate de que este objeto esté configurado adecuadamente
        controlReparacion.agregarReparacion(reparacion1);

        Reparacion reparacion2 = new Reparacion();
        reparacion2.setNombre_empleado("Maria Gomez");
        reparacion2.setVehiculo(new Vehiculo()); // Asegúrate de que este objeto esté configurado adecuadamente
        controlReparacion.agregarReparacion(reparacion2);

        List<Reparacion> lista = controlReparacion.obtenerTodasLasReparaciones();

        assertFalse(lista.isEmpty());
        assertEquals(2, lista.size()); // Verifica que haya dos reparaciones en la lista
    
    }
    
     @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}
