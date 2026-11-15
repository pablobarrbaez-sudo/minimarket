package com.jpbb.minimarket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_venta;
    private Double total;

    public Integer getId_venta() { return id_venta; }
    public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}