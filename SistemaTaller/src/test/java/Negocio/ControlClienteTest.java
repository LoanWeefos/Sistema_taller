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
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControlClienteTest {

    private static Connection conexion;
    private ControlCliente controlCliente;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlCliente = new ControlCliente(conexion);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        ClienteDAO clienteDAO = new ClienteDAO(conexion);
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        // Ejemplo: clienteDAO.eliminar("TEST1234");
    }

    @Test
    void testAgregarCliente() {
        // Crear un cliente para agregar
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), new Domicilio("Calle Test", "Colonia Test", 123), new ArrayList<>());
        
        // Agregar cliente
        controlCliente.agregarCliente(cliente);
        
        // Verificar que el cliente se haya agregado correctamente
        Cliente clienteObtenido = controlCliente.obtenerClientePorRfc("TEST1234");
        assertNotNull(clienteObtenido);
        assertEquals("Cliente Test", clienteObtenido.getNombre());
    }

    @Test
    void testActualizarCliente() {
        // Crear un cliente y agregarlo
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), new Domicilio("Calle Test", "Colonia Test", 123), new ArrayList<>());
        controlCliente.agregarCliente(cliente);
        
        // Actualizar los datos del cliente
        cliente.setNombre("Cliente Actualizado");
        controlCliente.actualizarCliente(cliente);

        // Verificar que el cliente se haya actualizado
        Cliente clienteActualizado = controlCliente.obtenerClientePorRfc("TEST1234");
        assertEquals("Cliente Actualizado", clienteActualizado.getNombre());
    }

    @Test
    void testEliminarCliente() {
        // Crear y agregar un cliente
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), new Domicilio("Calle Test", "Colonia Test", 123), new ArrayList<>());
        controlCliente.agregarCliente(cliente);
        
        // Eliminar el cliente
        controlCliente.eliminarCliente("TEST1234");

        // Verificar que el cliente haya sido eliminado
        Cliente clienteEliminado = controlCliente.obtenerClientePorRfc("TEST1234");
        assertNull(clienteEliminado);
    }

    @Test
    void testObtenerClientePorRfc() {
        // Crear y agregar un cliente
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), new Domicilio("Calle Test", "Colonia Test", 123), new ArrayList<>());
        controlCliente.agregarCliente(cliente);
        
        // Obtener el cliente por RFC
        Cliente clienteObtenido = controlCliente.obtenerClientePorRfc("TEST1234");
        assertNotNull(clienteObtenido);
        assertEquals("Cliente Test", clienteObtenido.getNombre());
    }

    @Test
    void testObtenerTodosLosClientes() {
        // Agregar varios clientes
        controlCliente.agregarCliente(new Cliente("RFC1", "Cliente 1", "cliente1@test.com", new Date(), new Domicilio("Calle 1", "Colonia 1", 1), new ArrayList<>()));
        controlCliente.agregarCliente(new Cliente("RFC2", "Cliente 2", "cliente2@test.com", new Date(), new Domicilio("Calle 2", "Colonia 2", 2), new ArrayList<>()));
        
        // Obtener todos los clientes
        List<Cliente> clientes = controlCliente.obtenerTodosLosClientes();
        assertFalse(clientes.isEmpty());
        assertEquals(2, clientes.size());
    }

    @Test
    void testAgregarVehiculoACliente() {
        // Crear y agregar un cliente
        Cliente cliente = new Cliente("TEST1234", "Cliente Test", "cliente@test.com", new Date(), new Domicilio("Calle Test", "Colonia Test", 123), new ArrayList<>());
        controlCliente.agregarCliente(cliente);
        
        // Crear un vehículo
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo al cliente
        controlCliente.agregarVehiculoACliente(vehiculo, "TEST1234");
        
        // Verificar que el vehículo fue agregado correctamente
        Cliente clienteObtenido = controlCliente.obtenerClientePorRfc("TEST1234");
        assertNotNull(clienteObtenido);
        assertFalse(clienteObtenido.getVehiculos().isEmpty());
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Cerrar la conexión después de ejecutar todas las pruebas
        Conexion.closeConnection();
    }
}

