package pruebas;

import Dominio.Cliente;
import Dominio.Domicilio;
import Persistencia.ClienteDAO;
import Persistencia.Conexion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDaoTest {

    private static Connection conexion;
    private ClienteDAO clienteDAO;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        conexion = Conexion.getConnection();
        assertNotNull(conexion, "La conexión no debe ser nula");
    }

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDAO(conexion);
    }

    @AfterEach
    void tearDown() throws Exception {
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'TEST1'");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'TEST2'");
    }

    @Test
    void testAgregarCliente() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", Date.valueOf(LocalDate.of(2003, 11, 2)), domicilio, null);

        clienteDAO.agregar(cliente);

        Cliente clienteObtenido = clienteDAO.obtenerPorId("TEST1");
        assertAll("Verificando datos del cliente",
            () -> assertNotNull(clienteObtenido),
            () -> assertEquals("Cliente Test", clienteObtenido.getNombre()),
            () -> assertEquals("cliente@test.com", clienteObtenido.getCorreo())
        );
    }

    @Test
    void testActualizarCliente() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", Date.valueOf(LocalDate.of(2003, 11, 2)), domicilio, null);
        clienteDAO.agregar(cliente);

        cliente.setNombre("Cliente Actualizado");
        clienteDAO.actualizar(cliente);

        Cliente clienteActualizado = clienteDAO.obtenerPorId("TEST1");
        assertNotNull(clienteActualizado);
        assertEquals("Cliente Actualizado", clienteActualizado.getNombre());
    }

    @Test
    void testEliminarCliente() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", 123);
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", Date.valueOf(LocalDate.of(2003, 11, 2)), domicilio, null);
        clienteDAO.agregar(cliente);

        clienteDAO.eliminar("TEST1");

        assertNull(clienteDAO.obtenerPorId("TEST1"));
    }

    @Test
    void testObtenerTodosLosClientes() {
        Domicilio domicilio1 = new Domicilio("Calle 1", "Colonia 1", 111);
        Cliente cliente1 = new Cliente("TEST1", "Cliente 1", "cliente1@test.com", Date.valueOf(LocalDate.of(2003, 11, 2)), domicilio1, null);
        clienteDAO.agregar(cliente1);

        Domicilio domicilio2 = new Domicilio("Calle 2", "Colonia 2", 222);
        Cliente cliente2 = new Cliente("TEST2", "Cliente 2", "cliente2@test.com", Date.valueOf(LocalDate.of(2003, 11, 1)), domicilio2, null);
        clienteDAO.agregar(cliente2);

        List<Cliente> clientes = clienteDAO.obtenerTodos();
        assertTrue(clientes.size() >= 2);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Conexion.closeConnection();
    }
}
