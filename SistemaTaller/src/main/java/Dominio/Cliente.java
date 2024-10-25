/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Clientes")
public class Cliente implements Serializable {

    @Id
    private String rfc;  // RFC como clave primaria

    private String nombre;
    private String correo;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Embedded
    private Domicilio domicilio;  // Clase embebida para el domicilio

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehiculo> vehiculos;

    public Cliente() {
    }

    public Cliente(String rfc, String nombre, String correo, Date fechaNacimiento, Domicilio domicilio, List<Vehiculo> vehiculos) {
        this.rfc = rfc;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.domicilio = domicilio;
        this.vehiculos = vehiculos;
    }

    // Getters y setters
    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    @Override
    public int hashCode() {
        return rfc != null ? rfc.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return this.rfc != null && this.rfc.equals(other.rfc);
    }

    @Override
    public String toString() {
        return "Dominio.Cliente[ rfc=" + rfc + " ]";
    }
}
