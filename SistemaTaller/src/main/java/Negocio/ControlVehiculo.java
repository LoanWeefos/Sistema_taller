/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Vehiculo;
import Persistencia.ClienteDAO;
import Persistencia.VehiculoDAO;
import java.sql.Connection;

/**
 *
 * @author Oscar
 */
public class ControlVehiculo {
     private VehiculoDAO vehiculoDAO;
    private ClienteDAO clienteDAO;

    // Constructor que acepta la conexión y crea instancias de los DAOs
    public ControlVehiculo(Connection connection) {
        this.vehiculoDAO = new VehiculoDAO(connection);
        this.clienteDAO = new ClienteDAO(connection);  // También necesitamos acceso al ClienteDAO
    }

    // Método para agregar un vehículo con validación de existencia previa
    public void agregarVehiculo(Vehiculo vehiculo) {
        // Verifica si ya existe un vehículo con la misma placa
        Vehiculo vehiculoExistente = vehiculoDAO.obtenerPorId(vehiculo.getPlaca());
        
        if (vehiculoExistente != null) {
            throw new IllegalArgumentException("El vehículo con placa " + vehiculo.getPlaca() + " ya existe.");
        }
        
        // Verificar si el cliente asociado existe
        Cliente cliente = clienteDAO.obtenerPorId(vehiculo.getCliente().getRfc());
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente con RFC " + vehiculo.getCliente().getRfc() + " no existe.");
        }

        // Si todo está bien, agregar el vehículo
        vehiculoDAO.agregar(vehiculo);
    }

    // Método para obtener un vehículo por su placa
    public Vehiculo obtenerVehiculoPorPlaca(String placa) {
        Vehiculo vehiculo = vehiculoDAO.obtenerPorId(placa);

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo con placa " + placa + " no se encontró.");
        }

        return vehiculo;
    }

    // Método para actualizar los datos de un vehículo
    public void actualizarVehiculo(Vehiculo vehiculo) {
        Vehiculo vehiculoExistente = vehiculoDAO.obtenerPorId(vehiculo.getPlaca());
        
        if (vehiculoExistente == null) {
            throw new IllegalArgumentException("El vehículo con placa " + vehiculo.getPlaca() + " no se encontró.");
        }
        
        // Verificar si el cliente asociado existe
        Cliente cliente = clienteDAO.obtenerPorId(vehiculo.getCliente().getRfc());
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente con RFC " + vehiculo.getCliente().getRfc() + " no existe.");
        }
        
        vehiculoDAO.actualizar(vehiculo);
    }

    // Método para eliminar un vehículo
    public void eliminarVehiculo(String placa) {
        Vehiculo vehiculoExistente = vehiculoDAO.obtenerPorId(placa);
        
        if (vehiculoExistente == null) {
            throw new IllegalArgumentException("El vehículo con placa " + placa + " no se encontró.");
        }

        vehiculoDAO.eliminar(placa);
    }
}
