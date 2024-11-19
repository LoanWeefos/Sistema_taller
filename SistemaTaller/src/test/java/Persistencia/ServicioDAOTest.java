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
 *
 * @author Oscar
 */
public class ServicioDAOTest {
    
    private static Connection conexion;
    private ServicioDAO servicioDAO;
    
    public ServicioDAOTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        conexion = Conexion.getConnection();
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        servicioDAO = new ServicioDAO(conexion);
        conexion.setAutoCommit(false); // Desactiva autocommit
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null) {
            conexion.rollback(); // Revierte cambios después de cada prueba
        }
    }
    @Test
    public void testAgregar() {
        // Preparar datos
        Servicio servicio = new Servicio(1,"Cambio de aceite", 450.00);

        // Ejecutar método
        servicioDAO.agregar(servicio);

        // Verificar
        Servicio servicioObtenido = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNotNull(servicioObtenido);
        assertEquals("Cambio de aceite", servicioObtenido.getDescripcion());
        assertEquals(450.00, servicioObtenido.getCosto());

        // Limpiar datos
        servicioDAO.eliminar(servicio.getId_servicio());
    }

    @Test
    public void testActualizar() {
        // Preparar datos
        Servicio servicio = new Servicio("Cambio de filtros", 300.00);
        servicioDAO.agregar(servicio);

        // Actualizar servicio
        servicio.setDescripcion("Cambio de filtros completos");
        servicio.setCosto(350.00);
        servicioDAO.actualizar(servicio);

        // Verificar
        Servicio servicioActualizado = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNotNull(servicioActualizado);
        assertEquals("Cambio de filtros completos", servicioActualizado.getDescripcion());
        assertEquals(350.00, servicioActualizado.getCosto());

        // Limpiar datos
        servicioDAO.eliminar(servicio.getId_servicio());
    }

    @Test
    public void testEliminar() {
        // Preparar datos
        Servicio servicio = new Servicio("Lavado completo", 200.00);
        servicioDAO.agregar(servicio);

        // Eliminar servicio
        servicioDAO.eliminar(servicio.getId_servicio());

        // Verificar
        Servicio servicioEliminado = servicioDAO.obtenerPorId(servicio.getId_servicio());
        assertNull(servicioEliminado);
    }

    @Test
    public void testObtenerPorId() {
        // Preparar datos
        Servicio servicio = new Servicio("Balanceo de llantas", 400.00);
        servicioDAO.agregar(servicio);

        // Obtener servicio por ID
        Servicio servicioObtenido = servicioDAO.obtenerPorId(servicio.getId_servicio());

        // Verificar
        assertNotNull(servicioObtenido);
        assertEquals("Balanceo de llantas", servicioObtenido.getDescripcion());
        assertEquals(400.00, servicioObtenido.getCosto());

        // Limpiar datos
        servicioDAO.eliminar(servicio.getId_servicio());
    }

    @Test
    public void testObtenerTodos() {
        // Preparar datos
        Servicio servicio1 = new Servicio("Inspección general", 500.00);
        Servicio servicio2 = new Servicio("Cambio de líquido de frenos", 250.00);
        servicioDAO.agregar(servicio1);
        servicioDAO.agregar(servicio2);

        // Obtener todos los servicios
        List<Servicio> servicios = servicioDAO.obtenerTodos();

        // Verificar
        assertNotNull(servicios);
        assertTrue(servicios.size() >= 2);

        // Limpiar datos
        servicioDAO.eliminar(servicio1.getId_servicio());
        servicioDAO.eliminar(servicio2.getId_servicio());
    }

    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
}
    

