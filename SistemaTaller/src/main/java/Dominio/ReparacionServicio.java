/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author hoshi
 */
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ReparacionServicio")
public class ReparacionServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reparacion_id")
    private Reparacion reparacion;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    // Atributos adicionales opcionales (por ejemplo, fecha o costo específico para esta relación)
    private String detalles;
    private Double costoAdicional;

    public ReparacionServicio() {
    }

    public ReparacionServicio(Reparacion reparacion, Servicio servicio, String detalles, Double costoAdicional) {
        this.reparacion = reparacion;
        this.servicio = servicio;
        this.detalles = detalles;
        this.costoAdicional = costoAdicional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Double getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(Double costoAdicional) {
        this.costoAdicional = costoAdicional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReparacionServicio)) {
            return false;
        }
        ReparacionServicio other = (ReparacionServicio) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Dominio.ReparacionServicio[ id=" + id + " ]";
    }
}

