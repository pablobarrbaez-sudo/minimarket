package com.jpbb.minimarket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;
    private String nombre;
    private Double precio;
    private Integer stock;

    public Integer getId_producto() { return id_producto; }
    public void setId_producto(Integer id_producto) { this.id_producto = id_producto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}