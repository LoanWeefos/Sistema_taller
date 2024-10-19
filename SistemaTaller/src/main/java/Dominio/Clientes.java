/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.Date;

/**
 *
 * @author Oscar
 */
public class Clientes {
    private String Nombre;
    private String correo;
    private Date fechaN;
    private String rfc;
    private String domicilio;

    public Clientes(String Nombre, String correo, Date fechaN, String rfc, String domicilio) {
        this.Nombre = Nombre;
        this.correo = correo;
        this.fechaN = fechaN;
        this.rfc = rfc;
        this.domicilio = domicilio;
    }

    public Clientes() {
    }

    public Clientes(String Nombre, String correo, Date fechaN, String domicilio) {
        this.Nombre = Nombre;
        this.correo = correo;
        this.fechaN = fechaN;
        this.domicilio = domicilio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaN() {
        return fechaN;
    }

    public void setFechaN(Date fechaN) {
        this.fechaN = fechaN;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    
    
    
}
