/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import IPersistencia.IPersistencia;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlCliente {

    private ClienteDAO clienteDAO;
    private Connection conexion;

    public ControlCliente() {
        this.clienteDAO = new ClienteDAO(Conexion.getConnection());  // Usamos la conexión de Conexion
    }

    public boolean agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        // Validaciones adicionales de negocio antes de insertar (si es necesario)
        if (cliente.getRfc() == null || cliente.getRfc().isEmpty()) {
            throw new IllegalArgumentException("El RFC del cliente es requerido");
        }

        // Agregar el cliente usando el método del DAO
        clienteDAO.agregar(cliente); // No se lanzará SQLException aquí
        System.out.println("El cliente ha sido agregado correctamente");
        return true; // Retornamos true si se agregó correctamente
    }

    public boolean editarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        // Validaciones adicionales de negocio antes de actualizar (si es necesario)
        if (cliente.getRfc() == null || cliente.getRfc().isEmpty()) {
            throw new IllegalArgumentException("El RFC del cliente es requerido");
        }

        clienteDAO.actualizar(cliente); // Asegúrate de implementar el método en ClienteDAO
        System.out.println("El cliente ha sido editado correctamente");
        return true;
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

    public boolean eliminarCliente(String rfc) {
    if (rfc == null || rfc.isEmpty()) {
        throw new IllegalArgumentException("El RFC no puede ser nulo o vacío.");
    }
    clienteDAO.eliminar(rfc);
    return true; // Retorna true si la eliminación fue llamada correctamente
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
