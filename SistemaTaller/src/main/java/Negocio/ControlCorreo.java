/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Cliente;
import Dominio.Correos;
import Dominio.Pago;
import Dominio.Reparacion;
import Dominio.Vehiculo;
import Persistencia.Conexion;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author wikit
 */
public class ControlCorreo {

    Correos correo = new Correos();
    Connection conexion;
    private ControlPago controlPago = new ControlPago();
    private ControlReparacion controlReparacion = new ControlReparacion();
    private ControlVehiculo controlVehiculo = new ControlVehiculo();
    private ControlCliente controlCliente = new ControlCliente();

    public ControlCorreo() {
        this.conexion = Conexion.getConnection();
    }

    public String correoTexto(String nombre, String placa) {
        String texto = "";

        texto = "<h2> Mantenimiento pendiente </h2> <br>"
                + "¡Buenos días!, " + nombre + "<br>"
                + "Se le recuerda que tiene una cita de mantenimiento pendiente para su auto con la placa: " + placa + "<br><br>"
                + "Esperamos nos visite pronto a su Taller Osman más cercano, <br>"
                + "o bien una respuesta a este correo para confirmar asistencia, gracias!&#x1F697;&#x1F6E0;&#x1F525;&#x1F525;&#x1F525;";

        return texto;
    }

    public void correoEnvio(String receptor, String texto) {
        correo.sendEmail(receptor, texto);
    }

    public void enviarCorreos() {
        List<Pago> listaPagos = controlPago.listarPagos();
        List<Pago> pagosFiltrados = new ArrayList<>();

        LocalDate haceUnMes = LocalDate.now().minusMonths(1);

        for (Pago pago : listaPagos) {
            if (pago.getFecha().toLocalDate().isBefore(haceUnMes)) {
                pagosFiltrados.add(pago);
            }
        }

        for (Pago pago : pagosFiltrados) {
            Reparacion reparacion = controlReparacion.obtenerReparacionPorId(pago.getReparacion().getId());

            Vehiculo vehiculo = controlVehiculo.obtenerVehiculoPorPlaca(reparacion.getVehiculo().getPlaca());
            String placa = vehiculo.getPlaca();

            Cliente cliente = controlCliente.obtenerClientePorRfc(vehiculo.getCliente().getRfc());
            String emailCliente = cliente.getCorreo();

            String textoCorreo = correoTexto(cliente.getNombre(), placa);
            correoEnvio(emailCliente, textoCorreo);
        }
    }

}
