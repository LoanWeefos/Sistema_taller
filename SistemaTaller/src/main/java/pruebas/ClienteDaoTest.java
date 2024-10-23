package pruebas;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import Persistencia.Conexion;

import java.sql.Connection;
import java.util.Date;

public class ClienteDaoTest {
   public static void main(String[] args) {
    Connection conexion = null;
    try {
        conexion = Conexion.getConnection();
        ClienteDAO clienteDAO = new ClienteDAO(conexion);

        // Crear un nuevo domicilio
        Domicilio domicilio = new Domicilio("Calle 123", "Colonia Centro", 456);

        // Crear un nuevo cliente
        Cliente nuevoCliente = new Cliente("RFC123456", "Oscar", "oscar@example.com", new Date(), domicilio, null);

        // Agregar el cliente
        clienteDAO.agregar(nuevoCliente);

        // Obtener el cliente por ID (RFC)
        Cliente clienteObtenido = clienteDAO.obtenerPorId("RFC123456");
        System.out.println("Cliente obtenido: " + clienteObtenido);

        // Actualizar el cliente
        clienteObtenido.setNombre("Oscar Actualizado");
        clienteDAO.actualizar(clienteObtenido);
        System.out.println("Cliente actualizado: " + clienteDAO.obtenerPorId("RFC123456"));

        // Agregar un vehículo al cliente
        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", clienteObtenido);
        clienteDAO.agregarVehiculoACliente(vehiculo, clienteObtenido);
        System.out.println("Vehículo agregado al cliente: " + vehiculo.getPlaca());

        // Obtener todos los clientes
        System.out.println("Todos los clientes: " + clienteDAO.obtenerTodos());

        // Eliminar primero los vehículos asociados al cliente
        clienteDAO.eliminarVehiculosDeCliente(clienteObtenido);
        System.out.println("Vehículos eliminados para el cliente: " + clienteObtenido.getRfc());

        // Eliminar el cliente
       
        
        //clienteDAO.eliminar("RFC123456");
       // System.out.println("Cliente eliminado.");

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (conexion != null) {
            Conexion.closeConnection();
        }
    }
}

}


