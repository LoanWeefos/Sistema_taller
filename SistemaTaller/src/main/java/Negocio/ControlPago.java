/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Pago;
import Dominio.Reparacion;
import Persistencia.Conexion;
import Persistencia.PagoDAO;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Oscar
 */
public class ControlPago {
    
    private PagoDAO pagoDAO;

    public ControlPago() {
        this.pagoDAO = new PagoDAO(Conexion.getConnection());
    }

    // Método para agregar un nuevo pago
    public void agregarPago(Pago pago) {
//        Pago pago = new Pago();
//        pago.setTotal(total);
//        pago.setMetodo(metodo);
//        pago.setFecha(fecha);
//        pago.setReparacion(reparacion);

        try {
            pagoDAO.agregar(pago);
            System.out.println("Pago agregado correctamente.");
        } catch (RuntimeException e) {
            System.err.println("Error al agregar el pago: " + e.getMessage());
        }
    }

    // Método para actualizar un pago existente
    public void actualizarPago(int id, double total, String metodo, LocalDateTime fecha, Reparacion reparacion) {
        try {
            Pago pago = pagoDAO.obtenerPorId(id);
            if (pago != null) {
                pago.setTotal(total);
                pago.setMetodo(metodo);
                pago.setFecha(fecha);
                pago.setReparacion(reparacion);

                pagoDAO.actualizar(pago);
                System.out.println("Pago actualizado correctamente.");
            } else {
                System.err.println("El pago con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al actualizar el pago: " + e.getMessage());
        }
    }

    // Método para eliminar un pago por su ID
    public void eliminarPago(int id) {
        try {
            Pago pago = pagoDAO.obtenerPorId(id);
            if (pago != null) {
                pagoDAO.eliminar(id);
                System.out.println("Pago eliminado correctamente.");
            } else {
                System.err.println("El pago con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al eliminar el pago: " + e.getMessage());
        }
    }

    // Método para obtener un pago por su ID
    public Pago obtenerPagoPorId(int id) {
        try {
            Pago pago = pagoDAO.obtenerPorId(id);
            if (pago != null) {
                System.out.println("Pago encontrado: " + pago);
            } else {
                System.err.println("El pago con ID " + id + " no existe.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al obtener el pago: " + e.getMessage());
        }
        return null;
    }

    // Método para listar todos los pagos
    public List<Pago> listarPagos() {
        try {
            List<Pago> pagos = pagoDAO.obtenerTodos();
            if (pagos.isEmpty()) {
                System.out.println("No hay pagos registrados.");
                return pagos;
            } else {
                for (Pago pago : pagos) {
                    System.out.println(pago);
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Error al listar los pagos: " + e.getMessage());
        }
        return null;
    }

}
