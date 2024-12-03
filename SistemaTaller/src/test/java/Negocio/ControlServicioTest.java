/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Servicio;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para ControlServicio.
 */
public class ControlServicioTest {

    private static Connection conexion;
    private ControlServicio controlServicio;

    @BeforeAll
    public static void setUpClass() {
        conexion = Conexion.getConnection(); // Configuración inicial de la conexión
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        if (conexion != null) {
            conexion.close(); // Cerrar conexión al final de las pruebas
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        controlServicio = new ControlServicio();
        conexion.setAutoCommit(false); // Desactivar autocommit para pruebas
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null) {
            conexion.rollback(); // Revertir cambios después de cada prueba
        }
    }

    @Test
    public void testAgregarServicio() {
        // Crear un servicio
        Servicio servicio = new Servicio(0, "Cambio de aceite", 500.0);

        // Agregar el servicio
        controlServicio.agregarServicio(servicio);

        // Verificar que el servicio se haya agregado correctamente
        Servicio servicioObtenido = controlServicio.obtenerServicioPorId(servicio.getId_servicio());
        assertNotNull(servicioObtenido);
        assertEquals("Cambio de aceite", servicioObtenido.getDescripcion());
        assertEquals(500.0, servicioObtenido.getCosto());
    }

    @Test
    public void testActualizarServicio() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(0, "Cambio de filtro", 300.0);
        controlServicio.agregarServicio(servicio);

        // Actualizar el servicio
        servicio.setDescripcion("Cambio de filtro y aceite");
        servicio.setCosto(400.0);
        controlServicio.actualizarServicio(servicio);

        // Verificar que el servicio se haya actualizado
        Servicio servicioActualizado = controlServicio.obtenerServicioPorId(servicio.getId_servicio());
        assertNotNull(servicioActualizado);
        assertEquals("Cambio de filtro y aceite", servicioActualizado.getDescripcion());
        assertEquals(400.0, servicioActualizado.getCosto());
    }

    @Test
    public void testEliminarServicio() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(0, "Lavado básico", 200.0);
        controlServicio.agregarServicio(servicio);

        // Eliminar el servicio
        controlServicio.eliminarServicio(servicio.getId_servicio());

        // Verificar que el servicio ya no exista
        Servicio servicioEliminado = controlServicio.obtenerServicioPorId(servicio.getId_servicio());
        assertNull(servicioEliminado);
    }

    @Test
    public void testObtenerServicioPorId() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(0, "Alineación de ruedas", 350.0);
        controlServicio.agregarServicio(servicio);

        // Obtener el servicio por ID
        Servicio servicioObtenido = controlServicio.obtenerServicioPorId(servicio.getId_servicio());

        // Verificar que los datos sean correctos
        assertNotNull(servicioObtenido);
        assertEquals("Alineación de ruedas", servicioObtenido.getDescripcion());
        assertEquals(350.0, servicioObtenido.getCosto());
    }

    @Test
    public void testListarServicios() {
        // Crear y agregar varios servicios
        Servicio servicio1 = new Servicio(0, "Cambio de pastillas de freno", 800.0);
        Servicio servicio2 = new Servicio(0, "Revisión general", 1000.0);
        controlServicio.agregarServicio(servicio1);
        controlServicio.agregarServicio(servicio2);

        // Obtener todos los servicios
        List<Servicio> servicios = controlServicio.listarServicios();

        // Verificar que se obtuvieron los servicios esperados
        assertNotNull(servicios);
        assertTrue(servicios.size() >= 2);
        assertTrue(servicios.stream().anyMatch(s -> "Cambio de pastillas de freno".equals(s.getDescripcion())));
        assertTrue(servicios.stream().anyMatch(s -> "Revisión general".equals(s.getDescripcion())));
    }

}

