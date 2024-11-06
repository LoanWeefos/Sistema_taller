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
    private ControlCliente controlCliente;
    private ControlVehiculo controlVehiculo;
    private ControlReparacion controlReparacion;

    @BeforeAll
    public static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar controles antes de cada prueba
        controlServicio = new ControlServicio();
        controlCliente = new ControlCliente();
        controlVehiculo = new ControlVehiculo();
        controlReparacion = new ControlReparacion();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        limpiarDatosDePrueba();
    }

    @Test
    public void agregarServicioTest() {
        // Primero, agregar un cliente y su vehículo
        Cliente cliente = crearCliente("RFC1", "Abril Snow", "snowmhyk@example.com");
        Vehiculo vehiculo = crearVehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Crear y agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(1, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);
        
        // Crear un nuevo servicio
        Servicio servicio = new Servicio(1, "Cambio de aceite", 1500.0); // Sin ID, será asignado por la BD

        // Agregar el servicio y verificar que no lance excepciones
        assertDoesNotThrow(() -> controlServicio.agregarServicio(servicio));
    }

    @Test
    public void eliminarServicioTest() {
        // Primero, agregar un cliente y su vehículo
        Cliente cliente = crearCliente("RFC1", "Abril Snow", "snowmhyk@example.com");
        Vehiculo vehiculo = crearVehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Crear y agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(0, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Crear un nuevo servicio
        Servicio servicio = new Servicio("Cambio de aceite", 1500.0);
        controlServicio.agregarServicio(servicio);

        int servicioId = servicio.getId_servicio(); // Obtener el ID asignado

        // Ahora eliminamos el servicio
        assertDoesNotThrow(() -> controlServicio.eliminarServicio(servicioId));
    }

    @Test
    public void obtenerServicioPorIdTest() {
        // Primero, agregar un cliente y su vehículo
        Cliente cliente = crearCliente("RFC1", "Abril Snow", "snowmhyk@example.com");
        Vehiculo vehiculo = crearVehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Crear y agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(0, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Crear un nuevo servicio
        Servicio servicio = new Servicio("Cambio de aceite", 1500.0);
        controlServicio.agregarServicio(servicio);

        // Obtener el servicio por ID y verificar
        Servicio obtenido = controlServicio.obtenerServicioPorId(servicio.getId_servicio());
        assertNotNull(obtenido);
        assertEquals(servicio.getId_servicio(), obtenido.getId_servicio());
    }

    @Test
    public void listarServiciosTest() {
        // Primero, agregar un cliente y su vehículo
        Cliente cliente = crearCliente("RFC1", "Abril Snow", "snowmhyk@example.com");
        Vehiculo vehiculo = crearVehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Crear y agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(0, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Agregar varios servicios
        Servicio servicio1 = new Servicio("Cambio de aceite", 1500.0);
        Servicio servicio2 = new Servicio("Revisión general", 2500.0);
        controlServicio.agregarServicio(servicio1);
        controlServicio.agregarServicio(servicio2);

        // Listar servicios y verificar que se hayan agregado
        List<Servicio> servicios = controlServicio.listarServicios();
        assertNotNull(servicios);
        assertEquals(2, servicios.size());
        assertTrue(servicios.contains(servicio1));
        assertTrue(servicios.contains(servicio2));
    }

    @Test
    public void actualizarServicioTest() {
        // Primero, agregar un cliente y su vehículo
        Cliente cliente = crearCliente("RFC1", "Abril Snow", "snowmhyk@example.com");
        Vehiculo vehiculo = crearVehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Crear y agregar una reparación para el vehículo
        Reparacion reparacion = new Reparacion(0, "Carmen", vehiculo);
        controlReparacion.agregarReparacion(reparacion);

        // Crear un nuevo servicio
        Servicio servicio = new Servicio("Cambio de aceite", 1500.0);
        controlServicio.agregarServicio(servicio);

        // Actualizar el servicio
        servicio.setDescripcion("Cambio de aceite y filtro");
        servicio.setCosto(1800.0);

        assertDoesNotThrow(() -> controlServicio.actualizarServicio(servicio.getId_servicio(), "Balatas", 2323.00));

        // Verificar que el servicio se haya actualizado correctamente
        Servicio actualizado = controlServicio.obtenerServicioPorId(servicio.getId_servicio());
        assertEquals("Cambio de aceite y filtro", actualizado.getDescripcion());
        assertEquals(1800.0, actualizado.getCosto());
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }

    private Cliente crearCliente(String rfc, String nombre, String email) {
        Cliente cliente = new Cliente(rfc, nombre, email, new java.util.Date(),
                new Domicilio("Calle Falsa", "Colonia", "123"), "644415095", null);
        controlCliente.agregarCliente(cliente);
        return cliente;
    }

    private Vehiculo crearVehiculo(String placa, String marca, String modelo, String color, Cliente cliente) {
        Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, color, cliente);
        controlVehiculo.agregarVehiculo(vehiculo);
        return vehiculo;
    }

    private void limpiarDatosDePrueba() throws SQLException {
        conexion.createStatement().executeUpdate("DELETE FROM Servicios WHERE id_servicio = 1");
        conexion.createStatement().executeUpdate("DELETE FROM Reparaciones WHERE id = 1");
        conexion.createStatement().executeUpdate("DELETE FROM Vehiculos WHERE PLACA = 'ABC1'");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE RFC = 'RFC1'");
    }
}
