/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Vehiculos")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false) // Asegurando que la placa sea única
    private String placa; // Usamos la placa como clave primaria

    private String marca;
    private String modelo;
    private String color;

    @ManyToOne
    @JoinColumn(name = "rfc_cliente", nullable = false) // Relación con Cliente
    private Cliente cliente; 

    public Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, String color, Cliente cliente) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.cliente = cliente;
    }

    // Getters y setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        return placa != null ? placa.hashCode() : 0; // Hash por placa
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
        return this.placa != null && this.placa.equals(other.placa); // Comparar placas
    }

    @Override
    public String toString() {
        return "Dominio.Vehiculo[ placa=" + placa + ", marca=" + marca + " ]"; // Representación
    }
}


