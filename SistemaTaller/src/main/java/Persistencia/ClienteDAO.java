package Persistencia;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IPersistencia<Cliente> {
    private Connection conexion;

    public ClienteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para agregar un cliente
    @Override
    public void agregar(Cliente cliente) {
        String sqlCliente = "INSERT INTO Clientes (rfc, nombre, correo, fecha_nacimiento, calle, colonia, numero) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = conexion.prepareStatement(sqlCliente)) {
            statement.setString(1, cliente.getRfc());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getCorreo());
            statement.setDate(4, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
            statement.setString(5, cliente.getDomicilio().getCalle());
            statement.setString(6, cliente.getDomicilio().getColonia());
            statement.setInt(7, cliente.getDomicilio().getNumero());

            statement.executeUpdate();
            System.out.println("Cliente agregado exitosamente: " + cliente);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para agregar un vehículo a un cliente
    public void agregarVehiculoACliente(Vehiculo vehiculo, Cliente cliente) {
        if (cliente == null) {
            System.out.println("Cliente no puede ser nulo");
            return;
        }
        vehiculo.setCliente(cliente); // Asociar el vehículo con el cliente

        // Crear instancia de VehiculoDAO pasando la conexión
        VehiculoDAO vehiculoDAO = new VehiculoDAO(conexion); // Pasa la conexión aquí
        vehiculoDAO.agregar(vehiculo); // Agregar vehículo
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sqlCliente = "UPDATE Clientes SET nombre = ?, correo = ?, fecha_nacimiento = ?, calle = ?, colonia = ?, numero = ? WHERE rfc = ?";
        
        try {
            conexion.setAutoCommit(false);  // Comenzamos una transacción

            // Actualizar cliente
            try (PreparedStatement psCliente = conexion.prepareStatement(sqlCliente)) {
                psCliente.setString(1, cliente.getNombre());
                psCliente.setString(2, cliente.getCorreo());
                psCliente.setDate(3, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
                psCliente.setString(4, cliente.getDomicilio().getCalle());
                psCliente.setString(5, cliente.getDomicilio().getColonia());
                psCliente.setInt(6, cliente.getDomicilio().getNumero());
                psCliente.setString(7, cliente.getRfc());

                psCliente.executeUpdate();
                conexion.commit();  // Confirmamos la transacción
            } catch (SQLException e) {
                conexion.rollback();  // Revertimos la transacción en caso de error
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conexion.setAutoCommit(true);  // Restauramos el comportamiento por defecto
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminar(String rfc) {
        String sqlEliminarCliente = "DELETE FROM Clientes WHERE rfc = ?";

        try (PreparedStatement psEliminarCliente = conexion.prepareStatement(sqlEliminarCliente)) {
            psEliminarCliente.setString(1, rfc);
            psEliminarCliente.executeUpdate();
            System.out.println("Cliente eliminado exitosamente: " + rfc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente obtenerPorId(String rfc) {
        String sqlCliente = "SELECT * FROM Clientes WHERE rfc = ?";
        Cliente cliente = null;

        try (PreparedStatement psCliente = conexion.prepareStatement(sqlCliente)) {
            psCliente.setString(1, rfc);
            try (ResultSet rsCliente = psCliente.executeQuery()) {
                if (rsCliente.next()) {
                    cliente = new Cliente();
                    cliente.setRfc(rsCliente.getString("rfc"));
                    cliente.setNombre(rsCliente.getString("nombre"));
                    cliente.setCorreo(rsCliente.getString("correo"));
                    cliente.setFechaNacimiento(rsCliente.getDate("fecha_nacimiento"));

                    // Recuperar domicilio
                    Domicilio domicilio = new Domicilio();
                    domicilio.setCalle(rsCliente.getString("calle"));
                    domicilio.setColonia(rsCliente.getString("colonia"));
                    domicilio.setNumero(rsCliente.getInt("numero"));
                    cliente.setDomicilio(domicilio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        String sqlCliente = "SELECT * FROM Clientes";
        List<Cliente> clientes = new ArrayList<>();

        try (Statement stmtCliente = conexion.createStatement();
             ResultSet rsCliente = stmtCliente.executeQuery(sqlCliente)) {

            while (rsCliente.next()) {
                Cliente cliente = new Cliente();
                cliente.setRfc(rsCliente.getString("rfc"));
                cliente.setNombre(rsCliente.getString("nombre"));
                cliente.setCorreo(rsCliente.getString("correo"));
                cliente.setFechaNacimiento(rsCliente.getDate("fecha_nacimiento"));

                // Recuperar domicilio
                Domicilio domicilio = new Domicilio();
                domicilio.setCalle(rsCliente.getString("calle"));
                domicilio.setColonia(rsCliente.getString("colonia"));
                domicilio.setNumero(rsCliente.getInt("numero"));
                cliente.setDomicilio(domicilio);

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void eliminarVehiculosDeCliente(Cliente cliente) throws SQLException {
    String sql = "DELETE FROM vehiculos WHERE rfc_cliente = ?";
    try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setString(1, cliente.getRfc());
        stmt.executeUpdate();
    }
}

}


