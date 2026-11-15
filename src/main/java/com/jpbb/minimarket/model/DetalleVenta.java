package com.jpbb.minimarket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    private Integer id_venta;
    private Integer id_producto;
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal;

    public DetalleVenta() {}

    public Integer getId_detalle() { return id_detalle; }
    public void setId_detalle(Integer id_detalle) { this.id_detalle = id_detalle; }

    public Integer getId_venta() { return id_venta; }
    public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }

    public Integer getId_producto() { return id_producto; }
    public void setId_producto(Integer id_producto) { this.id_producto = id_producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecio_unitario() { return precio_unitario; }
    public void setPrecio_unitario(Double precio_unitario) { this.precio_unitario = precio_unitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}