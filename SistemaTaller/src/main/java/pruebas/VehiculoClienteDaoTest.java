package pruebas;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Vehiculo;
import Persistencia.Conexion;
import Persistencia.ClienteDAO;
import Persistencia.VehiculoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
/**
 * 
 * @author MichellK
 */
public class VehiculoClienteDaoTest {
    public static void main(String[] args) {
        Connection conexion = null;
        // Establecer conexión a la base de datos
        conexion = Conexion.getConnection();
        ClienteDAO clienteDAO = new ClienteDAO(conexion);
        VehiculoDAO vehiculoDAO = new VehiculoDAO(conexion);
        // Asegurarse de que el cliente exista en la base de datos antes de agregar un vehículo
        Cliente cliente = new Cliente("RFC123456", "Oscar", "oscar@example.com", new Date(), new Domicilio("Calle 123", "Colonia Centro", 456), null);
        clienteDAO.agregar(cliente); // Agregar cliente primero
        // Crear un nuevo vehículo
        Vehiculo nuevoVehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Rojo", cliente);
        // Agregar el vehículo
        vehiculoDAO.agregar(nuevoVehiculo);
        System.out.println("Vehículo agregado: " + nuevoVehiculo);
        // Obtener el vehículo por placa
        Vehiculo vehiculoObtenido = vehiculoDAO.obtenerPorId("ABC123");
        if (vehiculoObtenido != null) {
            System.out.println("Vehículo obtenido: " + vehiculoObtenido);
            
            // Actualizar el vehículo
            vehiculoObtenido.setColor("Azul");
            vehiculoDAO.actualizar(vehiculoObtenido); // Llama al método de actualización
            System.out.println("Vehículo actualizado: " + vehiculoDAO.obtenerPorId("ABC123"));
        } else {
            System.out.println("No se pudo obtener el vehículo.");
        }
        // Eliminar el vehículo
       // vehiculoDAO.eliminar("ABC123");
        //System.out.println("Vehículo eliminado.");
        // Verificar si el vehículo ha sido eliminado
        Vehiculo vehiculoVerificado = vehiculoDAO.obtenerPorId("ABC123");
        if (vehiculoVerificado == null) {
            System.out.println("El vehículo ya no existe en la base de datos.");
        } else {
            System.out.println("El vehículo todavía existe: " + vehiculoVerificado);
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}





