/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import Persistencia.Conexion;
import Persistencia.VehiculoDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ControlVehiculoTest {

    private ControlVehiculo controlVehiculo;
    private static Connection connection;
    private ControlCliente controlcliente;

    @BeforeAll
    public static void setUpClass() throws SQLException{
        // Si necesitas inicializar algo antes de todos los tests
        connection = Conexion.getConnection();
    }

    @AfterAll
    public static void tearDownClass() {
        // Si necesitas limpiar algo después de todos los tests

    }

    @BeforeEach
    public void setUp() {
        controlcliente = new ControlCliente(connection);
        controlVehiculo = new ControlVehiculo(connection);
        // Agregar un cliente para usar en los tests

    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar la base de datos o deshacer cambios si es necesario
        // Por ejemplo, eliminar el cliente agregado en setUp
//        connection.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'RFC123'");
//        connection.createStatement().executeUpdate("DELETE FROM Vehiculos WHERE placa = 'ABC123'");
     ClienteDAO cliente= new ClienteDAO(connection);
     cliente.eliminar("RFC1");
    
     VehiculoDAO vehiculoDAO= new VehiculoDAO(connection);
     vehiculoDAO.eliminar("ABC1");
     
    }

    @Test
    public void testAgregarVehiculo() {
        System.out.println("agregarVehiculo");

        // Preparar un vehículo para agregar
        Cliente cliente = new Cliente("RFC1", "Esteban Duran", "duran@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        controlcliente.agregarCliente(cliente);
        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);

        // Llamar al método y verificar
        assertDoesNotThrow(() -> controlVehiculo.agregarVehiculo(vehiculo));
    }

    @Test
    public void testObtenerVehiculoPorPlaca() {
        System.out.println("obtenerVehiculoPorPlaca");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC1", "Michell Cedano", "michell@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        controlcliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Probar obtener el vehículo
        Vehiculo result = controlVehiculo.obtenerVehiculoPorPlaca("ABC1");
        assertNotNull(result);
        assertEquals("ABC1", result.getPlaca());
    }

    @Test
    public void testActualizarVehiculo() {
        System.out.println("actualizarVehiculo");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC1", "Ania Servin", "servin@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        controlcliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Actualizar el vehículo
        vehiculo.setModelo("Camry");
        assertDoesNotThrow(() -> controlVehiculo.actualizarVehiculo(vehiculo));

        // Verificar que la actualización se realizó
        Vehiculo updatedVehiculo = controlVehiculo.obtenerVehiculoPorPlaca("ABC1");
        assertEquals("Camry", updatedVehiculo.getModelo());
    }

    @Test
    public void testEliminarVehiculo() {
        System.out.println("eliminarVehiculo");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC1", "Abril snow", "snowmhyk@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        controlcliente.agregarCliente(cliente);

        Vehiculo vehiculo = new Vehiculo("ABC1", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Eliminar el vehículo
        assertDoesNotThrow(() -> controlVehiculo.eliminarVehiculo("ABC1"));

        // Verificar que el vehículo ha sido eliminado
    }
}
