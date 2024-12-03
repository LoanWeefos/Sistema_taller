/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Persistencia;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Reparacion;
import Dominio.Servicio;
import Dominio.ReparacionServicio;
import Dominio.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para ReparacionServicioDAO.
 */
public class ReparacionServicioDAOTest {

    private static Connection conexion;
    private ReparacionServicioDAO reparacionServicioDAO;
    private ReparacionDAO reparacionDAO;
    private ServicioDAO servicioDAO;
    private ClienteDAO clienteDAO;
    private VehiculoDAO vehiculoDAO;

    @BeforeAll
    static void setUpClass() throws SQLException {
        conexion = Conexion.getConnection();
        conexion.setAutoCommit(false);
    }


    @BeforeEach
    public void setUp() throws SQLException {
        limpiarBaseDeDatos();
        reparacionServicioDAO = new ReparacionServicioDAO(conexion);
        reparacionDAO = new ReparacionDAO(conexion); // Instancia el DAO de Reparacion
        servicioDAO = new ServicioDAO(conexion); // Instancia el DAO de Servicio
        clienteDAO = new ClienteDAO(conexion); // Instancia el DAO de Cliente
        vehiculoDAO = new VehiculoDAO(conexion); // Instancia el DAO de Vehiculo
        conexion.setAutoCommit(false); // Desactiva autocommit para poder hacer rollback
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
    public void testAgregar() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);

        // Ejecutar método
        reparacionDAO.agregar(reparacion);

        // Crear un servicio
        Servicio servicio = new Servicio(0, "Servicio Test", 100.0);
        servicioDAO.agregar(servicio);

        // Crear la relación ReparacionServicio
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);

        // Ejecutar método de agregar
        reparacionServicioDAO.agregar(reparacionServicio);

        // Verificar
        ReparacionServicio reparacionServicioObtenido = reparacionServicioDAO.obtenerPorId(reparacionServicio.getId_repserv());
        assertNotNull(reparacionServicioObtenido);
        assertEquals(reparacion.getId(), reparacionServicioObtenido.getReparacion().getId());
        assertEquals(servicio.getId_servicio(), reparacionServicioObtenido.getServicio().getId_servicio());

    }

    @Test
    public void testActualizar() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);

        // Ejecutar método
        reparacionDAO.agregar(reparacion);

        Servicio servicio = new Servicio(0, "Servicio Original", 150.0);
        servicioDAO.agregar(servicio);

        Servicio servicio2 = new Servicio(0, "Servicio nuevo", 250.0);
        servicioDAO.agregar(servicio2);
        
        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);
        reparacionServicioDAO.agregar(reparacionServicio);

        // Actualizar la relación en ReparacionServicio
        reparacionServicio.setServicio(servicio2);
        reparacionServicioDAO.actualizar(reparacionServicio);

        // Verificar la actualización
        ReparacionServicio reparacionServicioActualizado = reparacionServicioDAO.obtenerPorId(reparacionServicio.getId_repserv());
        assertNotNull(reparacionServicioActualizado);
    }

    @Test
    public void testEliminar() {
        // Preparar datos: Cliente, Vehículo, Reparación, Servicio
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);

        // Ejecutar método
        reparacionDAO.agregar(reparacion);

        Servicio servicio = new Servicio(0, "Servicio Eliminar", 250.0);
        servicioDAO.agregar(servicio);

        ReparacionServicio reparacionServicio = new ReparacionServicio();
        reparacionServicio.setReparacion(reparacion);
        reparacionServicio.setServicio(servicio);
        reparacionServicioDAO.agregar(reparacionServicio);

        // Eliminar la relación
        reparacionServicioDAO.eliminar(reparacionServicio.getId_repserv());

        // Verificar que la relación haya sido eliminada
        ReparacionServicio reparacionServicioEliminado = reparacionServicioDAO.obtenerPorId(reparacionServicio.getId_repserv());
        assertNull(reparacionServicioEliminado);

    }

    @Test
    public void testObtenerTodos() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new java.util.Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion1 = new Reparacion("tilin", vehiculo);
        Reparacion reparacion = new Reparacion("tilin", vehiculo);


        // Ejecutar método
        reparacionDAO.agregar(reparacion1);
        reparacionDAO.agregar(reparacion);

        Servicio servicio1 = new Servicio(0, "Servicio 1", 300.0);
        servicioDAO.agregar(servicio1);

        ReparacionServicio reparacionServicio1 = new ReparacionServicio();
        reparacionServicio1.setReparacion(reparacion1);
        reparacionServicio1.setServicio(servicio1);
        reparacionServicioDAO.agregar(reparacionServicio1);

        Reparacion reparacion2 = new Reparacion("Empleado 2", vehiculo);
        reparacionDAO.agregar(reparacion2);

        Servicio servicio2 = new Servicio(0, "Servicio 2", 400.0);
        servicioDAO.agregar(servicio2);

        ReparacionServicio reparacionServicio2 = new ReparacionServicio();
        reparacionServicio2.setReparacion(reparacion2);
        reparacionServicio2.setServicio(servicio2);
        reparacionServicioDAO.agregar(reparacionServicio2);

        // Obtener todas las relaciones
        List<ReparacionServicio> reparacionesServicios = reparacionServicioDAO.obtenerTodos();

        // Verificar que se obtuvieron las relaciones
        assertNotNull(reparacionesServicios);
        assertTrue(reparacionesServicios.size() >= 2);

    }
}

