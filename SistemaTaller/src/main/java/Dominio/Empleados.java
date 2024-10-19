/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Oscar
 */
public class Empleados {
   public int id;
   public String correo;
   public String nombre;
   public String contraseña;

    public Empleados() {
    }

    public Empleados(int id, String correo, String nombre, String contraseña) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }

    public Empleados(String correo, String nombre, String contraseña) {
        this.correo = correo;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
   
   
}
