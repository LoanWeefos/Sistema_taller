/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Persistencia;

import Dominio.Servicio;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para ServicioDAO.
 */
public class ServicioDAOTest {

    private static Connection conexion;
    private ServicioDAO servicioDAO;

    @BeforeAll
    public static void setUpClass() {
        conexion = Conexion.getConnection(); // Configuración inicial
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        if (conexion != null) {
            conexion.close(); // Cerrar conexión al final de las pruebas
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        servicioDAO = new ServicioDAO(conexion); // Instancia del DAO
        conexion.setAutoCommit(false);          // Desactiva autocommit para evitar persistencia
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null) {
            conexion.rollback(); // Revierte cambios después de cada prueba
        }
    }

    @Test
    public void testAgregar() {
        // Crear un servicio
        Servicio servicio = new Servicio(1, "Cambio de aceite", 450.00);

        // Ejecutar el método de agregar
        servicioDAO.agregar(servicio);

        // Recuperar el servicio para verificarlo
        Servicio servicioObtenido = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNotNull(servicioObtenido);
        assertEquals("Cambio de aceite", servicioObtenido.getDescripcion());
        assertEquals(450.00, servicioObtenido.getCosto());
    }

    @Test
    public void testActualizar() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(1, "Cambio de filtros", 300.00);
        servicioDAO.agregar(servicio);

        // Actualizar los datos del servicio
        servicio.setDescripcion("Cambio de filtros completos");
        servicio.setCosto(350.00);
        servicioDAO.actualizar(servicio);

        // Verificar los cambios
        Servicio servicioActualizado = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNotNull(servicioActualizado);
        assertEquals("Cambio de filtros completos", servicioActualizado.getDescripcion());
        assertEquals(350.00, servicioActualizado.getCosto());
    }

    @Test
    public void testEliminar() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(1, "Lavado completo", 200.00);
        servicioDAO.agregar(servicio);

        // Eliminar el servicio
        servicioDAO.eliminar(servicio.getId_servicio());

        // Verificar que el servicio ya no exista
        Servicio servicioEliminado = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNull(servicioEliminado);
    }

    @Test
    public void testObtenerPorId() {
        // Crear y agregar un servicio
        Servicio servicio = new Servicio(1, "Balanceo de llantas", 400.00);
        servicioDAO.agregar(servicio);

        // Recuperar el servicio por su ID
        Servicio servicioObtenido = servicioDAO.obtenerPorId(servicio.getId_servicio());

        // Verificar que los datos sean correctos
        assertNotNull(servicioObtenido);
        assertEquals("Balanceo de llantas", servicioObtenido.getDescripcion());
        assertEquals(400.00, servicioObtenido.getCosto());
    }

    @Test
    public void testObtenerTodos() {
        // Crear y agregar varios servicios
        Servicio servicio1 = new Servicio(1, "Inspección general", 500.00);
        Servicio servicio2 = new Servicio(2, "Cambio de líquido de frenos", 250.00);
        servicioDAO.agregar(servicio1);
        servicioDAO.agregar(servicio2);

        // Obtener todos los servicios
        List<Servicio> servicios = servicioDAO.obtenerTodos();

        // Verificar que se obtuvieron los servicios esperados
        assertNotNull(servicios);
        assertTrue(servicios.size() >= 2);

        // Verificar los datos de los servicios
        assertTrue(servicios.stream().anyMatch(s -> "Inspección general".equals(s.getDescripcion())));
        assertTrue(servicios.stream().anyMatch(s -> "Cambio de líquido de frenos".equals(s.getDescripcion())));
    }

}

    

