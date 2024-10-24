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

import static org.junit.jupiter.api.Assertions.*;

public class ControlVehiculoTest {
    
    private ControlVehiculo controlVehiculo;
    private Connection connection;

    @BeforeAll
    public static void setUpClass() {
        // Si necesitas inicializar algo antes de todos los tests
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Si necesitas limpiar algo después de todos los tests
    }
    
    @BeforeEach
    public void setUp() {
        connection = Conexion.getConnection();
        controlVehiculo = new ControlVehiculo(connection);
        // Agregar un cliente para usar en los tests
        Cliente cliente = new Cliente("RFC123", "Rutile Flores", "rutile@example.com",
                new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        clienteDAO.agregar(cliente); // Asegúrate de que el método agregar está implementado en ClienteDAO
    }
    
    @AfterEach
    public void tearDown() {
        // Limpiar la base de datos o deshacer cambios si es necesario
        // Por ejemplo, eliminar el cliente agregado en setUp
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        clienteDAO.eliminar("RFC123"); // Asegúrate de que el método eliminar está implementado en ClienteDAO
    }

    @Test
    public void testAgregarVehiculo() {
        System.out.println("agregarVehiculo");
        
        // Preparar un vehículo para agregar
        Cliente cliente = new Cliente("RFC123", "Esteban Duran", "duran@example.com", 
                                      new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        
        // Llamar al método y verificar
        assertDoesNotThrow(() -> controlVehiculo.agregarVehiculo(vehiculo));
    }

    @Test
    public void testObtenerVehiculoPorPlaca() {
        System.out.println("obtenerVehiculoPorPlaca");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC123", "Michell Cedano", "michell@example.com", 
                                      new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Probar obtener el vehículo
        Vehiculo result = controlVehiculo.obtenerVehiculoPorPlaca("ABC123");
        assertNotNull(result);
        assertEquals("ABC123", result.getPlaca());
    }

    @Test
    public void testActualizarVehiculo() {
        System.out.println("actualizarVehiculo");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC123", "Ania Servin", "servin@example.com", 
                                      new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Actualizar el vehículo
        vehiculo.setModelo("Camry");
        assertDoesNotThrow(() -> controlVehiculo.actualizarVehiculo(vehiculo));

        // Verificar que la actualización se realizó
        Vehiculo updatedVehiculo = controlVehiculo.obtenerVehiculoPorPlaca("ABC123");
        assertEquals("Camry", updatedVehiculo.getModelo());
    }

    @Test
    public void testEliminarVehiculo() {
        System.out.println("eliminarVehiculo");

        // Preparar y agregar un vehículo
        Cliente cliente = new Cliente("RFC123", "Abril snow", "snowmhyk@example.com", 
                                      new java.util.Date(), new Domicilio("Calle Falsa", "Colonia", 123), null);
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        controlVehiculo.agregarVehiculo(vehiculo);

        // Eliminar el vehículo
        assertDoesNotThrow(() -> controlVehiculo.eliminarVehiculo("ABC123"));

        // Verificar que el vehículo ha sido eliminado
        assertThrows(IllegalArgumentException.class, () -> controlVehiculo.obtenerVehiculoPorPlaca("ABC123"));
    }
}
