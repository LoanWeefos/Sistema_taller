/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;
import java.time.LocalDateTime; // Cambiado para un manejo de fecha moderno
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Pagos")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cambiado a IDENTITY para reflejar la base de datos
    private int id; // Cambiado a int

    private Double total;
    private String metodo; // Cambiar a enum si se desea

    private LocalDateTime fecha; // Cambiado a LocalDateTime

    @OneToOne
    @JoinColumn(name = "reparacion_id")
    private Reparacion reparacion;

    public Pago() {
    }

    public Pago(int id, Double total, String metodo, LocalDateTime fecha, Reparacion reparacion) {
        this.id = id;
        this.total = total;
        this.metodo = metodo;
        this.fecha = fecha;
        this.reparacion = reparacion;
    }

    public Pago(Double total, String metodo, LocalDateTime fecha, Reparacion reparacion) {
        this.total = total;
        this.metodo = metodo;
        this.fecha = fecha;
        this.reparacion = reparacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }

    @Override
    public int hashCode() {
        return (id != 0) ? Integer.hashCode(id) : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Dominio.Pago[ id=" + id + " ]";
    }
}



