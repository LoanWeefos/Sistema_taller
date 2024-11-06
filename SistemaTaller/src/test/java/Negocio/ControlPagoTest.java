/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Pago;
import Dominio.Reparacion;
import Dominio.Vehiculo;
import Persistencia.Conexion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hoshi
 */
public class ControlPagoTest {
    private static Connection conexion;
    private ControlPago controlPago;
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
        controlPago = new ControlPago();
        controlReparacion = new ControlReparacion();
    }

    @AfterEach
    void tearDown() throws SQLException {
        conexion.rollback(); // Revertir cambios al final del test
        conexion.setAutoCommit(true); // Reactivar auto commit para pruebas futuras
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }

    @Test
    void testAgregarPago() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);
        int keyRep = controlReparacion.agregarReparacionK(reparacion);

        double total = 1000.0;
        String metodo = "tarjeta";
        
        Pago pago = new Pago(total, metodo, LocalDateTime.now(), reparacion);
        controlPago.agregarPago(pago);

        Pago pagoObtenido = controlPago.obtenerPagoPorIdReparacion(keyRep);
        assertNotNull(pagoObtenido, "El pago debería haberse agregado y existir en la base de datos");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería ser el esperado");
        assertEquals(metodo, pagoObtenido.getMetodo(), "El método de pago debería ser el esperado");
        
        controlPago.eliminarPago(pagoObtenido.getId());
        controlReparacion.eliminarReparacion(reparacion.getId());
    }

    @Test
    void testActualizarPago() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);
        int keyRep = controlReparacion.agregarReparacionK(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);
        controlPago.agregarPago(pago);

        // Obtener el ID del pago recién agregado
        int idPago = controlPago.obtenerPagoPorIdReparacion(keyRep).getId();
        
        double totalActualizado = 150.0;
        String metodoActualizado = "Efectivo";
        LocalDateTime fechaActualizada = LocalDateTime.now().plusDays(1);

        controlPago.actualizarPago(idPago, totalActualizado, metodoActualizado, fechaActualizada, reparacion);

        Pago pagoActualizado = controlPago.obtenerPagoPorId(idPago);
        assertNotNull(pagoActualizado, "El pago debería existir tras la actualización");
        assertEquals(totalActualizado, pagoActualizado.getTotal(), "El total debería haber sido actualizado");
        assertEquals(metodoActualizado, pagoActualizado.getMetodo(), "El método de pago debería haber sido actualizado");

        controlPago.eliminarPago(pagoActualizado.getId());
        controlReparacion.eliminarReparacion(reparacion.getId());
    }

    @Test
    void testEliminarPago() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);
        int keyRep = controlReparacion.agregarReparacionK(reparacion);

        
        
        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);
        controlPago.agregarPago(pago);
        

        int idPago = controlPago.obtenerPagoPorIdReparacion(keyRep).getId();
        controlPago.eliminarPago(idPago);

        Pago pagoEliminado = controlPago.obtenerPagoPorId(idPago);
        assertNull(pagoEliminado, "El pago debería haber sido eliminado de la base de datos");

        controlReparacion.eliminarReparacion(reparacion.getId());
    }

    @Test
    void testObtenerPagoPorId() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); 
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);
        int keyRep = controlReparacion.agregarReparacionK(reparacion);

        double total = 1000.0;
        String metodo = "tarjeta";

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);
        controlPago.agregarPago(pago);
        

        Pago pagoObtenido = controlPago.obtenerPagoPorIdReparacion(keyRep);
        assertNotNull(pagoObtenido, "El pago debería haberse encontrado");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería coincidir con el registrado");

        controlPago.eliminarPago(pagoObtenido.getId());
        controlReparacion.eliminarReparacion(reparacion.getId());
    }

    @Test
    void testListarPagos() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); 
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        controlCliente.agregarCliente(cliente);

        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(nuevoVehiculo);

        Reparacion reparacion1 = new Reparacion("tilin", nuevoVehiculo);
        Reparacion reparacion2 = new Reparacion("tilin", nuevoVehiculo);
        
        controlReparacion.agregarReparacion(reparacion1);
        controlReparacion.agregarReparacion(reparacion2);

        

        Pago pago = new Pago(1200.0, "Efectivo", LocalDateTime.now(), reparacion1);
        controlPago.agregarPago(pago);
        
        Pago pago2 = new Pago(1200.0, "Efectivo", LocalDateTime.now(), reparacion2);
        controlPago.agregarPago(pago2);
        

        List<Pago> listaPagos = controlPago.listarPagos();
        assertNotNull(listaPagos, "La lista de pagos no debería ser nula");
        assertTrue(listaPagos.size() >= 2, "La lista de pagos debería contener al menos dos elementos");

        controlPago.eliminarPago(listaPagos.get(0).getId());
        controlPago.eliminarPago(listaPagos.get(1).getId());
    }
}

