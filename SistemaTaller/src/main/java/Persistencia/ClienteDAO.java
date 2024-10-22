/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dominio.Cliente;
import Dominio.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ClienteDAO implements IPersistencia<Cliente>{
    private Connection conexion;

    public ClienteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
     public void agregar(Cliente cliente) {
        String sqlCliente = "INSERT INTO cliente (nombre, correo, fecha_nacimiento, rfc, domicilio) VALUES (?, ?, ?, ?, ?)";
        String sqlVehiculo = "INSERT INTO vehiculo (cliente_id, placa, marca, modelo, color) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conexion.setAutoCommit(false);  // Comenzamos una transacción

            // Agregar cliente
            try (PreparedStatement psCliente = conexion.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                psCliente.setString(1, cliente.getNombre());
                psCliente.setString(2, cliente.getCorreo());
                psCliente.setDate(3, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
                psCliente.setLong(4, cliente.getId());
                psCliente.setString(5, cliente.getDireccion());

                int affectedRows = psCliente.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = psCliente.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            cliente.setId(generatedKeys.getLong(1));  // Establecer el ID generado
                        }
                    }
                }

                // Agregar vehículos del cliente
                for (Vehiculo vehiculo : cliente.getVehiculos()) {
                    try (PreparedStatement psVehiculo = conexion.prepareStatement(sqlVehiculo)) {
                        psVehiculo.setLong(1, cliente.getId());
                        psVehiculo.setString(2, vehiculo.getPlaca());
                        psVehiculo.setString(3, vehiculo.getMarca());
                        psVehiculo.setString(4, vehiculo.getModelo());
                        psVehiculo.setString(5, vehiculo.getColor());
                        psVehiculo.executeUpdate();
                    }
                }

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

    @Override
    public void actualizar(Cliente cliente) {
        String sqlCliente = "UPDATE cliente SET nombre = ?, correo = ?, fecha_nacimiento = ?, rfc = ?, domicilio = ? WHERE id = ?";
        String sqlEliminarVehiculos = "DELETE FROM vehiculo WHERE cliente_id = ?";
        String sqlVehiculo = "INSERT INTO vehiculo (cliente_id, placa, marca, modelo, color) VALUES (?, ?, ?, ?, ?)";

        try {
            conexion.setAutoCommit(false);  // Comenzamos una transacción

            // Actualizar cliente
            try (PreparedStatement psCliente = conexion.prepareStatement(sqlCliente)) {
                psCliente.setString(1, cliente.getNombre());
                psCliente.setString(2, cliente.getCorreo());
                psCliente.setDate(3, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
                psCliente.setString(4, cliente.getRfc());
                psCliente.setString(5, cliente.getDireccion());
                psCliente.setLong(6, cliente.getId());
                psCliente.executeUpdate();

                // Eliminar vehículos actuales
                try (PreparedStatement psEliminarVehiculos = conexion.prepareStatement(sqlEliminarVehiculos)) {
                    psEliminarVehiculos.setLong(1, cliente.getId());
                    psEliminarVehiculos.executeUpdate();
                }

                // Volver a insertar vehículos del cliente
                for (Vehiculo vehiculo : cliente.getVehiculos()) {
                    try (PreparedStatement psVehiculo = conexion.prepareStatement(sqlVehiculo)) {
                        psVehiculo.setLong(1, cliente.getId());
                        psVehiculo.setString(2, vehiculo.getPlaca());
                        psVehiculo.setString(3, vehiculo.getMarca());
                        psVehiculo.setString(4, vehiculo.getModelo());
                        psVehiculo.setString(5, vehiculo.getColor());
                        psVehiculo.executeUpdate();
                    }
                }

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

    @Override
    public void eliminar(Long id) {
        String sqlEliminarVehiculos = "DELETE FROM vehiculo WHERE cliente_id = ?";
        String sqlEliminarCliente = "DELETE FROM cliente WHERE id = ?";
        
        try (PreparedStatement psEliminarVehiculos = conexion.prepareStatement(sqlEliminarVehiculos);
             PreparedStatement psEliminarCliente = conexion.prepareStatement(sqlEliminarCliente)) {
             
            // Eliminar vehículos del cliente
            psEliminarVehiculos.setLong(1, id);
            psEliminarVehiculos.executeUpdate();
            
            // Eliminar cliente
            psEliminarCliente.setLong(1, id);
            psEliminarCliente.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        String sqlCliente = "SELECT * FROM cliente WHERE id = ?";
        String sqlVehiculo = "SELECT * FROM vehiculo WHERE cliente_id = ?";
        Cliente cliente = null;
        
        try (PreparedStatement psCliente = conexion.prepareStatement(sqlCliente);
             PreparedStatement psVehiculo = conexion.prepareStatement(sqlVehiculo)) {
             
            // Obtener cliente
            psCliente.setLong(1, id);
            try (ResultSet rsCliente = psCliente.executeQuery()) {
                if (rsCliente.next()) {
                    cliente = new Cliente();
                    cliente.setId(rsCliente.getLong("id"));
                    cliente.setNombre(rsCliente.getString("nombre"));
                    cliente.setCorreo(rsCliente.getString("correo"));
                    cliente.setFechaNacimiento(rsCliente.getDate("fecha_nacimiento"));
                    cliente.setRfc(rsCliente.getString("rfc"));
                    cliente.setDireccion(rsCliente.getString("direccion"));
                }
            }
            
            // Obtener vehículos asociados al cliente
            if (cliente != null) {
                List<Vehiculo> vehiculos = new ArrayList<>();
                psVehiculo.setLong(1, id);
                try (ResultSet rsVehiculo = psVehiculo.executeQuery()) {
                    while (rsVehiculo.next()) {
                        Vehiculo vehiculo = new Vehiculo();
                        vehiculo.setPlaca(rsVehiculo.getString("placa"));
                        vehiculo.setMarca(rsVehiculo.getString("marca"));
                        vehiculo.setModelo(rsVehiculo.getString("modelo"));
                        vehiculo.setColor(rsVehiculo.getString("color"));
                        vehiculos.add(vehiculo);
                    }
                    cliente.setVehiculos(vehiculos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        String sqlCliente = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Statement stmtCliente = conexion.createStatement();
             ResultSet rsCliente = stmtCliente.executeQuery(sqlCliente)) {
             
            while (rsCliente.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rsCliente.getLong("id"));
                cliente.setNombre(rsCliente.getString("nombre"));
                cliente.setCorreo(rsCliente.getString("correo"));
                cliente.setFechaNacimiento(rsCliente.getDate("fecha_nacimiento"));
                cliente.setRfc(rsCliente.getString("rfc"));
                cliente.setDireccion(rsCliente.getString("domicilio"));
                
                // Obtener vehículos para cada cliente
                String sqlVehiculo = "SELECT * FROM vehiculo WHERE cliente_id = " + cliente.getId();
                List<Vehiculo> vehiculos = new ArrayList<>();
                try (Statement stmtVehiculo = conexion.createStatement();
                     ResultSet rsVehiculo = stmtVehiculo.executeQuery(sqlVehiculo)) {
                     
                    while (rsVehiculo.next()) {
                        Vehiculo vehiculo = new Vehiculo();
                        vehiculo.setPlaca(rsVehiculo.getString("placa"));
                        vehiculo.setMarca(rsVehiculo.getString("marca"));
                        vehiculo.setModelo(rsVehiculo.getString("modelo"));
                        vehiculo.setColor(rsVehiculo.getString("color"));
                        vehiculos.add(vehiculo);
                    }
                    cliente.setVehiculos(vehiculos);
                }
                   clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return clientes;
    }
}
