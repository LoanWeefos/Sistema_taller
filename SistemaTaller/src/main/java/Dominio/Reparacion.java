/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Oscar
 */
@Entity
@Table(name = "Reparaciones")
public class Reparacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombreE;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
    
    @OneToMany(mappedBy = "reparacion", cascade = CascadeType.ALL)
    private List<ReparacionServicio> reparacionServicios;
    
    @OneToOne(mappedBy = "reparacion")
    private Pago pago;

    public Reparacion() {
    }

    public Reparacion(Long id, String nombreE, Vehiculo vehiculo, List<ReparacionServicio> reparacionServicios, Pago pago) {
        this.id = id;
        this.nombreE = nombreE;
        this.vehiculo = vehiculo;
        this.reparacionServicios = reparacionServicios;
        this.pago = pago;
    }

    public Reparacion(String nombreE, Vehiculo vehiculo, List<ReparacionServicio> reparacionServicios, Pago pago) {
        this.nombreE = nombreE;
        this.vehiculo = vehiculo;
        this.reparacionServicios = reparacionServicios;
        this.pago = pago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreE() {
        return nombreE;
    }

    public void setNombreE(String nombreE) {
        this.nombreE = nombreE;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<ReparacionServicio> getReparacionServicios() {
        return reparacionServicios;
    }

    public void setReparacionServicios(List<ReparacionServicio> reparacionServicios) {
        this.reparacionServicios = reparacionServicios;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reparacion)) {
            return false;
        }
        Reparacion other = (Reparacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persistencia.Reparacion[ id=" + id + " ]";
    }

}
