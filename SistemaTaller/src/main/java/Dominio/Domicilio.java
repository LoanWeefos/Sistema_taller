/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import javax.persistence.Embeddable;

@Embeddable
public class Domicilio {

    private String calle;
    private String colonia;
    private int numero;

    public Domicilio() {
    }

    public Domicilio(String calle, String colonia, int numero) {
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
    }

    // Getters y setters
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}

