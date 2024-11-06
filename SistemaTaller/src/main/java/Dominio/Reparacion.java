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

@Entity
@Table(name = "Reparaciones")
public class Reparacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se cambia a IDENTITY para reflejar la base de datos
    private long id;

    private String nombre_empleado; // Se cambia el nombre del atributo

    @ManyToOne
    @JoinColumn(name = "placa_vehiculo") // Se cambia el nombre de la columna a placa_vehiculo
    private Vehiculo vehiculo;
    
   
    // Se elimina la relación con Pago si no está en tu esquema

    public Reparacion() {
    }

    public Reparacion(long id, String nombre_empleado, Vehiculo vehiculo) {
        this.id = id;
        this.nombre_empleado = nombre_empleado;
        this.vehiculo = vehiculo;
        
    }

    public Reparacion(String nombre_empleado, Vehiculo vehiculo) {
        this.nombre_empleado = nombre_empleado;
        this.vehiculo = vehiculo;
    }

   
    

    // Getters y Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != 0 ? id : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reparacion)) {
            return false;
        }
        Reparacion other = (Reparacion) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Dominio.Reparacion[ id=" + id + " ]";
    }
}

