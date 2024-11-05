package Persistencia;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Pago;
import Dominio.Reparacion;
import Dominio.Vehiculo;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PagoDAOTest {

    private static Connection conexion;
    private PagoDAO pagoDAO;
    private ClienteDAO clienteDAO;
    private VehiculoDAO vehiculoDAO;
    private ReparacionDAO reparacionDAO;

    @BeforeAll
    public static void setUpClass() {
        conexion = Conexion.getConnection();
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() {
        pagoDAO = new PagoDAO(conexion);
        clienteDAO = new ClienteDAO(conexion);
        vehiculoDAO = new VehiculoDAO(conexion);
        reparacionDAO = new ReparacionDAO(conexion);
    }

    @Test
    public void testAgregar() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        int keyRep = reparacionDAO.agregarRepKey(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);

        assertNotNull(pago.getId(), "El ID del pago debería generarse después de agregarlo.");

        Pago pagoObtenido = pagoDAO.obtenerPorIdReparacion(keyRep);
        assertEquals(pago.getTotal(), pagoObtenido.getTotal());
        assertEquals(pago.getMetodo(), pagoObtenido.getMetodo());
        
        pagoDAO.eliminar(pago.getId());
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }

    @Test
    public void testActualizar() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        int keyRep = reparacionDAO.agregarRepKey(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);

        pago.setTotal(750.0);
        pago.setMetodo("tarjeta");
        pagoDAO.actualizar(pago);

        Pago pagoActualizado = pagoDAO.obtenerPorIdReparacion(keyRep);
        assertEquals(750.0, pagoActualizado.getTotal());
        assertEquals("tarjeta", pagoActualizado.getMetodo());
        
        pagoDAO.eliminar(pago.getId());
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }

    @Test
    public void testEliminar() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        reparacionDAO.agregar(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);
        pagoDAO.eliminar(pago.getId());

        Pago pagoEliminado = pagoDAO.obtenerPorId(pago.getId());
        assertNull(pagoEliminado, "El pago debería ser nulo después de eliminarlo.");
        
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }

    
    public void testObtenerPorId() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        reparacionDAO.agregar(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);

        Pago pagoObtenido = pagoDAO.obtenerPorId(reparacion.getId());
        assertNotNull(pagoObtenido);
        assertEquals(pago.getId(), pagoObtenido.getId());
        assertEquals(pago.getMetodo(), pagoObtenido.getMetodo());
        
        pagoDAO.eliminar(pago.getId());
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }

    @Test
    public void testObtenerTodos() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        reparacionDAO.agregar(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);
        Pago pago2 = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);
        pagoDAO.agregar(pago2);
        List<Pago> pagos = pagoDAO.obtenerTodos();

        assertNotNull(pagos);
        assertTrue(!pagos.isEmpty(), "La lista de pagos debería tener al menos un elemento.");
        
        pagoDAO.eliminar(pago.getId());
        pagoDAO.eliminar(pago2.getId());
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }
    
    @Test
    public void testObtenerPorIdReparacion() {
        Domicilio domicilio = new Domicilio("Calle Test", "Colonia Test", "123");
        List<Vehiculo> vehiculos = new ArrayList<>(); // Lista vacía de vehículos
        Cliente cliente = new Cliente("TEST1", "Cliente Test", "cliente@test.com", new Date(), domicilio, "644415095", vehiculos);
        clienteDAO.agregar(cliente);
        // Crear un vehículo de prueba
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);

        // Agregar vehículo a la base de datos
        vehiculoDAO.agregar(nuevoVehiculo);

        Reparacion reparacion = new Reparacion("tilin", nuevoVehiculo);

        int keyRep = reparacionDAO.agregarRepKey(reparacion);

        Pago pago = new Pago(1000.0, "tarjeta", LocalDateTime.now(), reparacion);

        pagoDAO.agregar(pago);

        Pago pagoObtenido = pagoDAO.obtenerPorIdReparacion(keyRep);
        assertNotNull(pagoObtenido);
        assertEquals(pago.getId(), pagoObtenido.getId());
        assertEquals(pago.getMetodo(), pagoObtenido.getMetodo());
        
        pagoDAO.eliminar(pago.getId());
        reparacionDAO.eliminar(reparacion.getId());
        
        vehiculoDAO.eliminar("ABC123");
        clienteDAO.eliminar("TEST1");
    }
}
