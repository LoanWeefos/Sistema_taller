/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import Persistencia.IPersistencia;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlCliente {
    private ClienteDAO clienteDAO;

    // Constructor que recibe una conexión y pasa al DAO
    public ControlCliente(Connection conexion) {
        this.clienteDAO = new ClienteDAO(conexion);  // Se inicializa el DAO con la conexión
    }

    // Método para agregar un cliente
    public void agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        // Validaciones adicionales de negocio antes de insertar (si es necesario)
        // Por ejemplo: Validar que el RFC no esté vacío
        if (cliente.getRfc() == null || cliente.getRfc().isEmpty()) {
            throw new IllegalArgumentException("El RFC del cliente es requerido");
        }

        clienteDAO.agregar(cliente);
        System.out.println("El cliente ha sido agregado correctamente");
    }

    // Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        // Validaciones adicionales antes de actualizar
        clienteDAO.actualizar(cliente);
        System.out.println("El cliente ha sido actualizado correctamente");
    }

    // Método para eliminar un cliente
    public void eliminarCliente(String rfc) {
        if (rfc == null || rfc.isEmpty()) {
            throw new IllegalArgumentException("El RFC del cliente es requerido para eliminar");
        }

        Cliente cliente = clienteDAO.obtenerPorId(rfc);
        if (cliente == null) {
            System.out.println("El cliente con RFC " + rfc + " no existe");
            return;
        }

        try {
            // Elimina primero los vehículos asociados si hay alguno
            clienteDAO.eliminarVehiculosDeCliente(cliente);
            // Luego elimina el cliente
            clienteDAO.eliminar(rfc);
            System.out.println("Cliente y sus vehículos han sido eliminados");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un cliente por su RFC
    public Cliente obtenerClientePorRfc(String rfc) {
        if (rfc == null || rfc.isEmpty()) {
            throw new IllegalArgumentException("El RFC es requerido");
        }

        Cliente cliente = clienteDAO.obtenerPorId(rfc);
        if (cliente == null) {
            System.out.println("Cliente no encontrado con RFC: " + rfc);
        }

        return cliente;
    }

    // Método para obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        if (clientes.isEmpty()) {
            System.out.println("No se encontraron clientes");
        }
        return clientes;
    }

//    // Método para agregar un vehículo a un cliente
//    public void agregarVehiculoACliente(Vehiculo vehiculo, String rfcCliente) {
//        Cliente cliente = obtenerClientePorRfc(rfcCliente);
//        if (cliente == null) {
//            throw new IllegalArgumentException("Cliente no encontrado para RFC: " + rfcCliente);
//        }
//
//        clienteDAO.agregarVehiculoACliente(vehiculo, cliente);
//        System.out.println("Vehículo agregado correctamente al cliente con RFC: " + rfcCliente);
//    }
}
