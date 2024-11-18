/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Persistencia;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Reparacion;
import Dominio.Vehiculo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReparacionDAOTest {

    private static Connection conexion;
    private ReparacionDAO reparacionDAO;
    private ClienteDAO clienteDAO;
    private VehiculoDAO vehiculoDAO;

    @BeforeAll
    public static void setUpClass() {
        conexion = Conexion.getConnection();
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        reparacionDAO = new ReparacionDAO(conexion);
        clienteDAO = new ClienteDAO(conexion);
        vehiculoDAO = new VehiculoDAO(conexion);
        conexion.setAutoCommit(false); // Desactiva autocommit
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null) {
            conexion.rollback(); // Revertir todos los cambios realizados durante la prueba
        }
    }

    @Test
    public void testAgregar() {
        // Preparar datos
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("tilin", vehiculo);

        // Ejecutar método
        reparacionDAO.agregar(reparacion);

        // Verificar
        Reparacion reparacionObtenida = reparacionDAO.obtenerPorId(reparacion.getId());
        assertNotNull(reparacionObtenida);
        assertEquals("tilin", reparacionObtenida.getNombre_empleado());

        // Limpiar datos
        reparacionDAO.eliminar(reparacion.getId());
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }

    @Test
    public void testActualizar() {
        // Preparar datos
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST2", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("XYZ789", "Honda", "Civic", "Negro", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("Empleado Original", vehiculo);
        int idGenerado = reparacionDAO.agregarRepKey(reparacion);

        // Actualizar reparación
        reparacion.setNombre_empleado("Empleado Actualizado");
        reparacionDAO.agregar(reparacion);

        // Verificar
        Reparacion reparacionActualizada = reparacionDAO.obtenerPorId(reparacion.getId());
        assertNotNull(reparacionActualizada);
        assertEquals("Empleado Actualizado", reparacionActualizada.getNombre_empleado());

        // Limpiar datos
        reparacionDAO.eliminar(reparacion.getId());
        vehiculoDAO.eliminar("XYZ789");
        clienteDAO.eliminar("TEST2");
    }

    @Test
    public void testEliminar() {
        // Preparar datos
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST3", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("LMN456", "Ford", "Focus", "Blanco", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion = new Reparacion("Empleado Eliminar", vehiculo);
        reparacionDAO.agregar(reparacion);

        // Eliminar reparación
        reparacionDAO.eliminar(reparacion.getId());

        // Verificar
        Reparacion reparacionEliminada = reparacionDAO.obtenerPorId(reparacion.getId());
        assertNull(reparacionEliminada);

        // Limpiar datos
        vehiculoDAO.eliminar("LMN456");
        clienteDAO.eliminar("TEST3");
    }

    @Test
    public void testObtenerTodos() {
        // Preparar datos
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        Cliente cliente = new Cliente("TEST4", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", new ArrayList<>());
        clienteDAO.agregar(cliente);

        Vehiculo vehiculo = new Vehiculo("PQR678", "Chevrolet", "Malibu", "Azul", cliente);
        vehiculoDAO.agregar(vehiculo);

        Reparacion reparacion1 = new Reparacion("Empleado 1", vehiculo);
        Reparacion reparacion2 = new Reparacion("Empleado 2", vehiculo);
        reparacionDAO.agregarRepKey(reparacion1);
        reparacionDAO.agregarRepKey(reparacion2);

        // Obtener reparaciones
        List<Reparacion> reparaciones = reparacionDAO.obtenerTodos();
        assertNotNull(reparaciones);
        assertTrue(reparaciones.size() >= 2);

        // Limpiar datos
        reparacionDAO.eliminar(reparacion1.getId());
        reparacionDAO.eliminar(reparacion2.getId());
        vehiculoDAO.eliminar("PQR678");
        clienteDAO.eliminar("TEST4");
    }
}
