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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Servicios")
public class Servicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_servicio;

    private String descripcion;
    private Double costo;

    public Servicio() {
    }

    public Servicio(int id_servicio, String descripcion, Double costo) {
        this.id_servicio = id_servicio;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public Servicio(String descripcion, Double costo) {
        this.descripcion = descripcion;
        this.costo = costo;
    }

    // Getters y Setters
    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_servicio != 0 ? id_servicio : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        return this.id_servicio == other.id_servicio;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
