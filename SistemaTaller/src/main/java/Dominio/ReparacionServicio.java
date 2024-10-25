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
@Table(name = "Reparaciones_Servicios")
public class ReparacionServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repserv") // Clave primaria compuesta
    private int id_repserv;

    @ManyToOne
    @JoinColumn(name = "id_reparacion") // Nombre de la columna según el esquema
    private Reparacion reparacion;

    @ManyToOne
    @JoinColumn(name = "id_servicio") // Nombre de la columna según el esquema
    private Servicio servicio;

    public ReparacionServicio() {
    }

    public ReparacionServicio(Reparacion reparacion, Servicio servicio) {
        this.reparacion = reparacion;
        this.servicio = servicio;
    }

    // Getters y Setters

    public int getId_repserv() {
        return id_repserv;
    }

    public void setId_repserv(int id_repserv) {
        this.id_repserv = id_repserv;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_repserv != 0 ? id_repserv : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReparacionServicio)) {
            return false;
        }
        ReparacionServicio other = (ReparacionServicio) object;
        return this.id_repserv == other.id_repserv;
    }

    @Override
    public String toString() {
        return "Dominio.ReparacionServicio[ id_repserv=" + id_repserv + " ]";
    }
}

