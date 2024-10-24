package prueb;

import Dominio.Vehiculo;
import Dominio.Cliente;
import Dominio.Domicilio;
import Persistencia.VehiculoDAO;
import Persistencia.Conexion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoDaoClienteTest {

    private static Connection conexion;
    private VehiculoDAO vehiculoDAO;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Obtener la conexión a la base de datos antes de ejecutar todas las pruebas
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Crear una nueva instancia de VehiculoDAO antes de cada prueba
        vehiculoDAO = new VehiculoDAO(conexion);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Limpiar los datos de prueba después de cada prueba
        Statement stmt = conexion.createStatement();
        stmt.executeUpdate("DELETE FROM Vehiculos WHERE placa = 'ABC123'");
        stmt.executeUpdate("DELETE FROM Vehiculos WHERE placa = 'XYZ789'");
    }

    @Test
    void testAgregarVehiculo() {
        // Crear un cliente para asociar al vehículo
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), domicilio, vehiculos);

        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        // Verificar que el vehículo fue agregado correctamente
        Vehiculo vehiculoObtenido = vehiculoDAO.obtenerPorId("ABC123");
        assertNotNull(vehiculoObtenido);
        assertEquals("Toyota", vehiculoObtenido.getMarca());
        assertEquals("Corolla", vehiculoObtenido.getModelo());
    }

    @Test
    void testActualizarVehiculo() {
        // Crear un cliente para asociar al vehículo
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), domicilio, vehiculos);

        // Crear y agregar un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(nuevoVehiculo);

        // Actualizar el vehículo
        nuevoVehiculo.setMarca("Honda");
        nuevoVehiculo.setModelo("Civic");
        vehiculoDAO.actualizar(nuevoVehiculo);

        // Verificar que los cambios se reflejan en la base de datos
        Vehiculo vehiculoActualizado = vehiculoDAO.obtenerPorId("ABC123");
        assertNotNull(vehiculoActualizado);
        assertEquals("Honda", vehiculoActualizado.getMarca());
        assertEquals("Civic", vehiculoActualizado.getModelo());
    }

    @Test
    void testEliminarVehiculo() {
        // Crear un cliente para asociar al vehículo
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), domicilio, vehiculos);

        // Crear y agregar un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(nuevoVehiculo);

        // Eliminar el vehículo
        vehiculoDAO.eliminar("ABC123");

        // Verificar que el vehículo fue eliminado correctamente
        Vehiculo vehiculoEliminado = vehiculoDAO.obtenerPorId("ABC123");
        assertNull(vehiculoEliminado);
    }

    @Test
    void testObtenerVehiculoPorPlaca() {
        // Crear un cliente para asociar al vehículo
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), domicilio, vehiculos);

        // Crear y agregar un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        vehiculoDAO.agregar(nuevoVehiculo);

        // Obtener el vehículo por placa
        Vehiculo vehiculoObtenido = vehiculoDAO.obtenerPorId("ABC123");
        assertNotNull(vehiculoObtenido);
        assertEquals("Rojo", vehiculoObtenido.getColor());
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}





