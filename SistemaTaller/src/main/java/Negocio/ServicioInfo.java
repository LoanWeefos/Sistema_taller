/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 *
 * @author Oscar
 */
public class ServicioInfo {

    private String descripcion;
    private double costoTotal;

    // Constructor
    public ServicioInfo(String descripcion, double costoTotal) {
        this.descripcion = descripcion;
        this.costoTotal = costoTotal;
    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    // Setters
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }
}
