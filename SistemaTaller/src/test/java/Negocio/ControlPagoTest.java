/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Pago;
import Dominio.Reparacion;
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
    private Reparacion reparacionPrueba;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar ControlPago y una reparación de prueba antes de cada prueba
        controlPago = new ControlPago();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        conexion.createStatement().executeUpdate("DELETE FROM Pagos WHERE id = 1");
    }

    @Test
    void testAgregarPago() {
        // Datos para el pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();
        reparacionPrueba = new Reparacion();

        // Agregar el pago de prueba
        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);

        // Verificar que el pago se haya agregado correctamente
        Pago pagoObtenido = controlPago.obtenerPagoPorId(1);
        assertNotNull(pagoObtenido, "El pago debería haberse agregado y existir en la base de datos");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería ser el esperado");
        assertEquals(metodo, pagoObtenido.getMetodo(), "El método de pago debería ser el esperado");
    }

    @Test
    void testActualizarPago() {
        // Datos iniciales para el pago de prueba
        double totalInicial = 100.0;
        String metodoInicial = "Efectivo";
        LocalDateTime fechaInicial = LocalDateTime.now();

        // Agregar el pago inicial
        controlPago.agregarPago(totalInicial, metodoInicial, fechaInicial, reparacionPrueba);

        // Datos para actualizar el pago
        int idPago = 1; // Cambia el ID según corresponda
        double totalActualizado = 150.0;
        String metodoActualizado = "Tarjeta";
        LocalDateTime fechaActualizada = LocalDateTime.now().plusDays(1);

        // Actualizar el pago
        controlPago.actualizarPago(idPago, totalActualizado, metodoActualizado, fechaActualizada, reparacionPrueba);

        // Verificar que el pago se haya actualizado correctamente
        Pago pagoActualizado = controlPago.obtenerPagoPorId(idPago);
        assertNotNull(pagoActualizado, "El pago debería existir tras la actualización");
        assertEquals(totalActualizado, pagoActualizado.getTotal(), "El total debería haber sido actualizado");
        assertEquals(metodoActualizado, pagoActualizado.getMetodo(), "El método de pago debería haber sido actualizado");
    }

    @Test
    void testEliminarPago() {
        // Datos para el pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();

        // Agregar y luego eliminar el pago
        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);
        int idPago = 1; // Cambia el ID según corresponda
        controlPago.eliminarPago(idPago);

        // Verificar que el pago haya sido eliminado
        Pago pagoEliminado = controlPago.obtenerPagoPorId(idPago);
        assertNull(pagoEliminado, "El pago debería haber sido eliminado de la base de datos");
    }

    @Test
    void testObtenerPagoPorId() {
        // Datos para el pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();

        // Agregar el pago y obtenerlo por ID
        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);
        int idPago = 1; // Cambia el ID según corresponda
        Pago pagoObtenido = controlPago.obtenerPagoPorId(idPago);

        // Verificar que el pago se haya obtenido correctamente
        assertNotNull(pagoObtenido, "El pago debería haberse encontrado");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería coincidir con el registrado");
    }

    @Test
    void testListarPagos() {
        // Agregar varios pagos de prueba
        controlPago.agregarPago(100.0, "Efectivo", LocalDateTime.now(), reparacionPrueba);
        controlPago.agregarPago(200.0, "Tarjeta", LocalDateTime.now(), reparacionPrueba);

        // Obtener la lista de pagos
        List<Pago> listaPagos = controlPago.listarPagos();

        // Verificar que la lista de pagos contenga los elementos esperados
        assertNotNull(listaPagos, "La lista de pagos no debería ser nula");
        assertTrue(listaPagos.size() >= 2, "La lista de pagos debería contener al menos dos elementos");
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
    
}
