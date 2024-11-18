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
    private ControlCliente controlCliente;
    private ControlVehiculo controlVehiculo;
    private ControlReparacion controlReparacion;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        conexion.setAutoCommit(false); // Desactiva auto commit para transacciones
        // Inicialización de objetos de prueba
        controlCliente = new ControlCliente();
        controlVehiculo = new ControlVehiculo();
        controlReparacion = new ControlReparacion();
    }

    @AfterEach
    void tearDown() throws SQLException {
        conexion.rollback(); // Revertir cambios al final del test
        conexion.setAutoCommit(true); // Reactivar auto commit para pruebas futuras
    }

    @Test
    public void testAgregarReparacion() {
        System.out.println("agregarReparacion");
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        // Agregar reparación y verificar
        controlReparacion.agregarReparacion(reparacion);

        Reparacion reparacionObtenida = controlReparacion.obtenerReparacionPorPlaca("ABC123");
        assertNotNull(reparacionObtenida);
        assertEquals("tilin", reparacionObtenida.getNombre_empleado());
    }

    @Test
    public void testEliminarReparacion() {
        System.out.println("eliminarReparacion");
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>();
        Cliente cliente = new Cliente("TEST2", "Cliente Test2", "cliente2@test.com", new Date(), domicilio, "644415096", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("DEF456", "Honda", "Civic", "Azul", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Reparacion reparacionObtenida = controlReparacion.obtenerReparacionPorPlaca("DEF456");
        assertNotNull(reparacionObtenida);

        controlReparacion.eliminarReparacion(reparacionObtenida.getId());

        Reparacion reparacionEliminada = controlReparacion.obtenerReparacionPorPlaca("DEF456");
        assertNull(reparacionEliminada);
    }

    @Test
    public void testActualizarReparacion() {
        System.out.println("actualizarReparacion");
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>();
        Cliente cliente = new Cliente("TEST3", "Cliente Test3", "cliente3@test.com", new Date(), domicilio, "644415097", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("GHI789", "Ford", "Focus", "Blanco", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Reparacion reparacionActualizada = controlReparacion.obtenerReparacionPorPlaca("GHI789");
        reparacionActualizada.setNombre_empleado("nuevoEmpleado");
        controlReparacion.actualizarReparacion(reparacionActualizada);

        Reparacion reparacionObtenida = controlReparacion.obtenerReparacionPorPlaca("GHI789");
        assertNotNull(reparacionObtenida);
        assertEquals("nuevoEmpleado", reparacionObtenida.getNombre_empleado());
    }

    @Test
    public void testObtenerTodasLasReparaciones() {
        System.out.println("obtenerTodasLasReparaciones");

        // Crear cliente, vehículo y reparaciones para poblar datos
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>();
        Cliente cliente = new Cliente("TEST4", "Cliente Test4", "cliente4@test.com", new Date(), domicilio, "644415098", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo1 = new Vehiculo("JKL012", "Chevrolet", "Malibu", "Negro", cliente);
        controlVehiculo.agregarVehiculo(vehiculo1);

        Vehiculo vehiculo2 = new Vehiculo("MNO345", "Nissan", "Altima", "Gris", cliente);
        controlVehiculo.agregarVehiculo(vehiculo2);

        Reparacion reparacion1 = new Reparacion("tilin", vehiculo1);
        Reparacion reparacion2 = new Reparacion("tilin", vehiculo2);
        controlReparacion.agregarReparacion(reparacion1);
        controlReparacion.agregarReparacion(reparacion2);


        // Obtener todas las reparaciones y verificar
        List<Reparacion> reparaciones = controlReparacion.obtenerTodasLasReparaciones();
        assertNotNull(reparaciones);
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}
