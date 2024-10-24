package Persistencia;

import Dominio.Vehiculo;
import Dominio.Cliente; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 
 * @author MichellK
 */
public class VehiculoDAO {
    private Connection connection;

    // Constructor que acepta una conexión
    public VehiculoDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para agregar un vehículo
    public void agregar(Vehiculo vehiculo) {
        String sql = "INSERT INTO Vehiculos (placa, marca, modelo, color, rfc_cliente) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehiculo.getPlaca());
            statement.setString(2, vehiculo.getMarca());
            statement.setString(3, vehiculo.getModelo());
            statement.setString(4, vehiculo.getColor());
            statement.setString(5, vehiculo.getCliente().getRfc()); // Asociar vehículo con cliente

            statement.executeUpdate();
            System.out.println("Vehículo agregado exitosamente: " + vehiculo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un vehículo por placa
    public Vehiculo obtenerPorId(String placa) {
        String sql = "SELECT * FROM Vehiculos WHERE placa = ?";
        Vehiculo vehiculo = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, placa);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // Obtener el cliente correspondiente usando el RFC
                String rfcCliente = resultSet.getString("rfc_cliente");
                Cliente cliente = obtenerClientePorRfc(rfcCliente); // Método que deberás implementar
                
                // Crear objeto Vehiculo
                vehiculo = new Vehiculo(
                    resultSet.getString("placa"),
                    resultSet.getString("marca"),
                    resultSet.getString("modelo"),
                    resultSet.getString("color"),
                    cliente // Asocia el cliente recuperado
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehiculo;
    }

    // Método para obtener un Cliente por RFC
    private Cliente obtenerClientePorRfc(String rfc) {
        Cliente cliente = null;
        ClienteDAO clienteDAO = new ClienteDAO(connection); // Crear instancia de ClienteDAO

        cliente = clienteDAO.obtenerPorId(rfc); // Usa el método existente en ClienteDAO

        return cliente;
    }
    
    // Método para actualizar un vehículo
    public void actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE Vehiculos SET marca = ?, modelo = ?, color = ? WHERE placa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehiculo.getMarca());
            statement.setString(2, vehiculo.getModelo());
            statement.setString(3, vehiculo.getColor());
            statement.setString(4, vehiculo.getPlaca());
            statement.executeUpdate();
            System.out.println("Vehículo actualizado: " + vehiculo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un vehículo
    public void eliminar(String placa) {
        String sql = "DELETE FROM Vehiculos WHERE placa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, placa);
            statement.executeUpdate();
            System.out.println("Vehículo eliminado: " + placa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

