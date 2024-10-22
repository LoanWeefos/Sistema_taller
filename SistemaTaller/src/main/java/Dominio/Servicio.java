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
import javax.persistence.Table;

/**
 *
 * @author Oscar
 */
@Entity
@Table(name = "Servicios")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descripcion;
    private Double costo;
    
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    private List<ReparacionServicio> reparacionServicios;

    public Servicio() {
    }

    public Servicio(Long id, String descripcion, Double costo, List<ReparacionServicio> reparacionServicios) {
        this.id = id;
        this.descripcion = descripcion;
        this.costo = costo;
        this.reparacionServicios = reparacionServicios;
    }

    public Servicio(String descripcion, Double costo, List<ReparacionServicio> reparacionServicios) {
        this.descripcion = descripcion;
        this.costo = costo;
        this.reparacionServicios = reparacionServicios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public List<ReparacionServicio> getReparacionServicios() {
        return reparacionServicios;
    }

    public void setReparacionServicios(List<ReparacionServicio> reparacionServicios) {
        this.reparacionServicios = reparacionServicios;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dominio.Servicio[ id=" + id + " ]";
    }
    
}
