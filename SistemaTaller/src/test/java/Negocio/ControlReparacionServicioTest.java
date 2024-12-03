/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Reparacion;
import Dominio.Servicio;
import Dominio.ReparacionServicio;
import Dominio.Vehiculo;
import Persistencia.Conexion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControlReparacionServicioTest {

    private static Connection conexion;
    private ControlCliente controlCliente;
    private ControlVehiculo controlVehiculo;
    private ControlReparacion controlReparacion;
    private ControlServicio controlServicio;
    private ControlReparacionServicio controlReparacionServicio;

    @BeforeAll
    static void setUpClass() throws SQLException {
        conexion = Conexion.getConnection();
        conexion.setAutoCommit(false);
    }

    @BeforeEach
    void setUp() throws SQLException {
        limpiarBaseDeDatos();
        controlCliente = new ControlCliente();
        controlVehiculo = new ControlVehiculo();
        controlReparacion = new ControlReparacion();
        controlServicio = new ControlServicio();
        controlReparacionServicio = new ControlReparacionServicio();
    }

    private void limpiarBaseDeDatos() throws SQLException {
        conexion.createStatement().executeUpdate("DELETE FROM reparaciones_servicios");
        conexion.createStatement().executeUpdate("DELETE FROM reparaciones");
        conexion.createStatement().executeUpdate("DELETE FROM servicios");
        conexion.createStatement().executeUpdate("DELETE FROM vehiculos");
        conexion.createStatement().executeUpdate("DELETE FROM clientes");
        conexion.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null && !conexion.getAutoCommit()) {
            conexion.rollback();
        }
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    @Test
    public void agregarReparacionServicioTest() {
        // Crear un cliente, vehículo, reparación y servicio para agregar
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), new Domicilio("Calle Test", "Colonia Test", "123"), "644415095", new ArrayList<>());
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        Reparacion reparacion = new Reparacion("Reparación de motor", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Servicio servicio = new Servicio(0, "Servicio de cambio de aceite", 150.0);
        controlServicio.agregarServicio(servicio);

        // Crear la relación ReparacionServicio
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);

        controlReparacionServicio.agregarReparacionServicio(reparacionServicio);

        // Verificar que se haya agregado correctamente
        ReparacionServicio agregado = controlReparacionServicio.obtenerReparacionServicioPorId(reparacionServicio.getId_repserv());
        assertNotNull(agregado);
    }

    @Test
    public void actualizarReparacionServicioTest() {
        // Agregar cliente, vehículo, reparación, y servicio
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), new Domicilio("Calle Test", "Colonia Test", "123"), "644415095", new ArrayList<>());
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        Reparacion reparacion = new Reparacion("Reparación de motor", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Servicio servicio = new Servicio(0, "Servicio de cambio de aceite", 150.0);
        controlServicio.agregarServicio(servicio);

        // Crear la relación ReparacionServicio
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);

        controlReparacionServicio.agregarReparacionServicio(reparacionServicio);

        // Actualizar la relación
        Servicio servicioNuevo = new Servicio(0, "Servicio de revisión general", 200.0);
        controlServicio.agregarServicio(servicioNuevo);

        reparacionServicio.setServicio(servicioNuevo);
        controlReparacionServicio.actualizarReparacionServicio(reparacionServicio);

        // Verificar que se haya actualizado correctamente
        ReparacionServicio actualizado = controlReparacionServicio.obtenerReparacionServicioPorId(reparacionServicio.getId_repserv());
        assertEquals(servicioNuevo.getId_servicio(), actualizado.getServicio().getId_servicio());
    }

    @Test
    public void eliminarReparacionServicioTest() {
        // Agregar cliente, vehículo, reparación, y servicio
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), new Domicilio("Calle Test", "Colonia Test", "123"), "644415095", new ArrayList<>());
        controlCliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        Reparacion reparacion = new Reparacion("Reparación de motor", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        Servicio servicio = new Servicio(0, "Servicio de cambio de aceite", 150.0);
        controlServicio.agregarServicio(servicio);

        // Crear la relación ReparacionServicio
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);

        controlReparacionServicio.agregarReparacionServicio(reparacionServicio);

        // Eliminar la relación
        controlReparacionServicio.eliminarReparacionServicio(reparacionServicio.getId_repserv());

        // Verificar que la relación haya sido eliminada
        ReparacionServicio eliminado = controlReparacionServicio.obtenerReparacionServicioPorId(reparacionServicio.getId_repserv());
        assertNull(eliminado);
    }

    @Test
    public void listarReparacionServicioTest() {
        // Agregar clientes, vehículos, reparaciones y servicios
        Cliente cliente1 = new Cliente("TEST1", "Cliente Test1", "cliente1@test.com", new java.util.Date(), new Domicilio("Calle Test 1", "Colonia Test 1", "123"), "644415095", new ArrayList<>());
        controlCliente.agregarCliente(cliente1);

        Vehiculo vehiculo1 = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente1);
        controlVehiculo.agregarVehiculo(vehiculo1);

        Reparacion reparacion1 = new Reparacion("Reparación de motor 1", vehiculo1);
        controlReparacion.agregarReparacion(reparacion1);

        Servicio servicio1 = new Servicio(0, "Servicio de cambio de aceite 1", 150.0);
        controlServicio.agregarServicio(servicio1);

        ReparacionServicio reparacionServicio1 = new ReparacionServicio();
        reparacionServicio1.setReparacion(reparacion1);
        reparacionServicio1.setServicio(servicio1);
        controlReparacionServicio.agregarReparacionServicio(reparacionServicio1);

        // Agregar otro cliente, vehículo, reparación y servicio
        Cliente cliente2 = new Cliente("TEST2", "Cliente Test2", "cliente2@test.com", new java.util.Date(), new Domicilio("Calle Test 2", "Colonia Test 2", "123"), "644415095", new ArrayList<>());
        controlCliente.agregarCliente(cliente2);

        Vehiculo vehiculo2 = new Vehiculo("DEF456", "Ford", "Focus", "Azul", cliente2);
        controlVehiculo.agregarVehiculo(vehiculo2);

        Reparacion reparacion2 = new Reparacion("Reparación de frenos", vehiculo2);
        controlReparacion.agregarReparacion(reparacion2);

        Servicio servicio2 = new Servicio(0, "Servicio de frenos", 200.0);
        controlServicio.agregarServicio(servicio2);

        ReparacionServicio reparacionServicio2 = new ReparacionServicio();
        reparacionServicio2.setReparacion(reparacion2);
        reparacionServicio2.setServicio(servicio2);
        controlReparacionServicio.agregarReparacionServicio(reparacionServicio2);

        // Listar todos los servicios de reparación
        List<ReparacionServicio> lista = controlReparacionServicio.obtenerTodasLasReparacionesServicios();

        assertFalse(lista.isEmpty());
        assertTrue(lista.size() >= 2);
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}
