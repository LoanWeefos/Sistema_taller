/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Oscar
 */
public class Servicios {
    public int id;
    public String descripcion;
    public float costo;

    public Servicios() {
    }

    public Servicios(int id, String descripcion, float costo) {
        this.id = id;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public Servicios(String descripcion, float costo) {
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
    
    
}
