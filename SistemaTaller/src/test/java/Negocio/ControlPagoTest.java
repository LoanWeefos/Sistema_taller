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
        // Inicializar el ControlCliente antes de cada prueba
        controlPago = new ControlPago();
        reparacionPrueba = new Reparacion(); 
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        conexion.createStatement().executeUpdate("DELETE FROM Pagos WHERE id = 1");
    }
    
    @Test
    public void agregarPagoTest(){
          // Agregar un pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();

        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);

        // Verificar si el pago se agregó correctamente consultándolo
        Pago pagoObtenido = controlPago.obtenerPagoPorId(1); // Cambia el ID según corresponda
        assertNotNull(pagoObtenido, "El pago debería haberse agregado y existir en la base de datos");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería ser el esperado");
        assertEquals(metodo, pagoObtenido.getMetodo(), "El método de pago debería ser el esperado");
    }
    
    @Test
    public void actualizarPagoTest(){
         // Agregar un pago de prueba
        double totalInicial = 100.0;
        String metodoInicial = "Efectivo";
        LocalDateTime fechaInicial = LocalDateTime.now();

        controlPago.agregarPago(totalInicial, metodoInicial, fechaInicial, reparacionPrueba);

        // Actualizar el pago
        int idPago = 1; // Cambiar ID según corresponda
        double totalActualizado = 150.0;
        String metodoActualizado = "Tarjeta";
        LocalDateTime fechaActualizada = LocalDateTime.now().plusDays(1);

        controlPago.actualizarPago(idPago, totalActualizado, metodoActualizado, fechaActualizada, reparacionPrueba);

        // Verificar si el pago se actualizó correctamente
        Pago pagoActualizado = controlPago.obtenerPagoPorId(idPago);
        assertNotNull(pagoActualizado, "El pago debería existir tras la actualización");
        assertEquals(totalActualizado, pagoActualizado.getTotal(), "El total debería haber sido actualizado");
        assertEquals(metodoActualizado, pagoActualizado.getMetodo(), "El método de pago debería haber sido actualizado");
        
    }
    
    @Test
    public void eliminarPagoTest(){
         // Agregar un pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();

        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);

        // Eliminar el pago
        int idPago = 1; // Cambiar ID según corresponda
        controlPago.eliminarPago(idPago);

        // Verificar si el pago fue eliminado
        Pago pagoEliminado = controlPago.obtenerPagoPorId(idPago);
        assertNull(pagoEliminado, "El pago debería haber sido eliminado de la base de datos");
        
    }
    
    @Test
    public void obtenerPagoTest(){
         // Agregar un pago de prueba
        double total = 100.0;
        String metodo = "Efectivo";
        LocalDateTime fecha = LocalDateTime.now();

        controlPago.agregarPago(total, metodo, fecha, reparacionPrueba);

        // Obtener el pago por ID
        int idPago = 1; // Cambiar ID según corresponda
        Pago pagoObtenido = controlPago.obtenerPagoPorId(idPago);
        assertNotNull(pagoObtenido, "El pago debería haberse encontrado");
        assertEquals(total, pagoObtenido.getTotal(), "El total debería coincidir con el registrado");
    }
    
    @Test
    public void listarPagosTest(){
        // Agregar dos pagos de prueba
        controlPago.agregarPago(100.0, "Efectivo", LocalDateTime.now(), reparacionPrueba);
        controlPago.agregarPago(200.0, "Tarjeta", LocalDateTime.now(), reparacionPrueba);

        // Obtener la lista de pagos
        List<Pago> listaPagos = controlPago.listarPagos();
        assertNotNull(listaPagos, "La lista de pagos no debería ser nula");
        assertTrue(listaPagos.size() >= 2, "La lista de pagos debería contener al menos dos elementos");
    }
    
    
     @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
    
}
