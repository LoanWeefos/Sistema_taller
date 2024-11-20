/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Correos;

import Dominio.Cliente;
import Dominio.Correos;
import Dominio.Domicilio;
import Negocio.ControlCliente;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author wikit
 */
public class CorreosTest {
    private static Connection conexion;
    private ControlCliente controlCliente;

    private Correos correo;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
    }

    @BeforeEach
    void setUp() {
        // Inicializar el ControlCliente antes de cada prueba
        controlCliente = new ControlCliente();
        correo = new Correos();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpiar los datos de prueba después de cada prueba
        
        // Aquí puedes agregar la lógica para eliminar clientes de prueba si es necesario
        // Ejemplo: clienteDAO.eliminar("TEST1234");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'RFC1'");
        conexion.createStatement().executeUpdate("DELETE FROM Clientes WHERE rfc = 'RFC2'");
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void probarCorreo() {
        String texto = correo.sendEmail("michell.cedano.lopez@gmail.com", "Prueba de correo");
        assertEquals("Prueba de correo", texto);
    }

    @Test
    public void obtenerCorreos() {
        // Agregar varios clientes
        controlCliente.agregarCliente(new Cliente("RFC1", "Cliente 1", "cliente1@test.com", new Date(), new Domicilio("Calle 1", "Colonia 1", "123"), "644415095", new ArrayList<>()));
        controlCliente.agregarCliente(new Cliente("RFC2", "Cliente 2", "cliente2@test.com", new Date(), new Domicilio("Calle 2", "Colonia 2", "123"), "644415095", new ArrayList<>()));

        // Obtener todos los clientes
        List<Cliente> clientes = controlCliente.obtenerTodosLosClientes();
        List<String> correos = new ArrayList<>();

        // Iterar sobre la lista de clientes y extraer los correos
        for (Cliente cliente : clientes) {
            correos.add(cliente.getCorreo());
        }

        assertFalse(correos.isEmpty());
    }
}
