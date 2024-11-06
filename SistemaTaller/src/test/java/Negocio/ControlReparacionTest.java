/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Reparacion;
import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import Persistencia.Conexion;
import Persistencia.ReparacionDAO;
import Persistencia.VehiculoDAO;
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
    private ControlVehiculo controlVehiculo;
    private ControlCliente controlcliente;
    private ControlServicio controlservicio;

    @BeforeAll
    public static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlReparacion = new ControlReparacion();
        controlcliente = new ControlCliente();
        controlVehiculo = new ControlVehiculo();
        controlservicio = new ControlServicio();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba

        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
//        conexion.createStatement().executeUpdate("DELETE FROM Reparaciones WHERE id = 1");
//        VehiculoDAO vehiculoDAO = new VehiculoDAO(conexion);
//        vehiculoDAO.eliminar("ABC1");
//
//        ClienteDAO cliente = new ClienteDAO(conexion);
//        cliente.eliminar("RFC1");

    }

    @Test
    public void agregarReparacionTest() {
        // Preparar un cliente y un vehículo
        Cliente cliente = new Cliente("RFC1", "Esteban Duran", "duran@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", "123"), "644415095", null);
        controlcliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Crear una reparación
        Reparacion reparacion = new Reparacion(1, "Esteban Duran", vehiculo);

        // Agregar la reparación y verificar que no lance excepciones
        assertDoesNotThrow(() -> controlReparacion.agregarReparacion(reparacion));

        //controlservicio.agregarServicio("Cambio de aceite", 1234); // Asegúrate de que el servicio esté agregado a la base de datos
        // Asociar el servicio con la reparación
        // Aquí puedes crear una instancia de ReparacionServicio que haga la conexión entre Reparacion y Servicio
        //ReparacionServicio reparacionServicio = new ReparacionServicio(reparacion, servicio);
    }

    @Test
    public void actualizarReparacionTest() {
        
        // Preparar un cliente y un vehículo
        Cliente cliente = new Cliente("RFC1", "Esteban Duran", "duran@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", "123"), "644415095", null);
        controlcliente.agregarCliente(cliente);

         Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);
        // Primero agregamos una reparación para actualizar
        Reparacion reparacion = new Reparacion(1, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Supongamos que el ID es conocido
        long id = 1;
        reparacion.setNombre_empleado("Maria Gomez"); // Cambiamos el nombre del empleado
        controlReparacion.actualizarReparacion(reparacion);

        // Verificar que se haya actualizado correctamente
        Reparacion actualizado = controlReparacion.obtenerReparacionPorId(id);
        assertEquals("Maria Gomez", actualizado.getNombre_empleado());

    }

    @Test
    public void eliminarReparacionTest() {
        // Primero agregamos una reparación para eliminar
        Cliente cliente = new Cliente("RFC1", "Abril Snow", "snowmhyk@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", "123"), "644415095", null);
        controlcliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(1, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Ahora podemos eliminar la reparación
        assertDoesNotThrow(() -> controlReparacion.eliminarReparacion(1));

        

        // Luego elimina el vehículo
        assertDoesNotThrow(() -> controlVehiculo.eliminarVehiculo("ABC1"));

        // Finalmente, elimina el cliente
        assertDoesNotThrow(() -> controlcliente.eliminarCliente("RFC1"));
    }

    @Test
    public void obtenerReparacionTest() {

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC1", "Abril snow", "snowmhyk@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", "123"), "644415095", null);
        controlcliente.agregarCliente(cliente);

        
        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);
        
        Reparacion reparacion = new Reparacion(1, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Reparacion obtenido = controlReparacion.obtenerReparacionPorId(1);

        assertNotNull(obtenido);
        assertEquals("Carmen", obtenido.getNombre_empleado());
    }

    @Test
    public void listarReparacionTest() {
        
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
