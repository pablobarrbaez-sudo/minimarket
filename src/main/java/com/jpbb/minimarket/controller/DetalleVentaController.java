package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.DetalleVenta;
import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.repository.DetalleVentaRepository;
import com.jpbb.minimarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/detalles")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaRepository detalleRepo;

    @Autowired
    private ProductoRepository productoRepo;

    public static class DetalleDTO {
        private Integer id_detalle;
        private Integer id_venta;
        private String nombreProducto;
        private Integer cantidad;
        private Double precio_unitario;
        private Double subtotal;

        public Integer getId_detalle() { return id_detalle; }
        public void setId_detalle(Integer id_detalle) { this.id_detalle = id_detalle; }
        public Integer getId_venta() { return id_venta; }
        public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecio_unitario() { return precio_unitario; }
        public void setPrecio_unitario(Double precio_unitario) { this.precio_unitario = precio_unitario; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }

    @GetMapping
    public String listar(Model m) {
        List<DetalleVenta> todosDetalles = detalleRepo.findAll();
        List<Producto> todosProductos = productoRepo.findAll();

        Map<Integer, String> mapaProductos = new HashMap<>();
        for (Producto p : todosProductos) {
            mapaProductos.put(p.getId_producto(), p.getNombre());
        }

        List<DetalleDTO> detallesConNombre = new ArrayList<>();
        double totalGeneral = 0.0;

        for (DetalleVenta d : todosDetalles) {
            DetalleDTO dto = new DetalleDTO();
            dto.setId_detalle(d.getId_detalle());
            dto.setId_venta(d.getId_venta());

            String nombre = mapaProductos.get(d.getId_producto());
            dto.setNombreProducto(nombre != null ? nombre : "Producto Desconocido");
            dto.setCantidad(d.getCantidad());
            dto.setPrecio_unitario(d.getPrecio_unitario());
            dto.setSubtotal(d.getSubtotal());

            if (d.getSubtotal() != null) {
                totalGeneral += d.getSubtotal();
            }

            detallesConNombre.add(dto);
        }

        m.addAttribute("detalles", detallesConNombre);
        m.addAttribute("totalGeneral", totalGeneral);
        return "admin-detalles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        detalleRepo.deleteById(id);
        return "redirect:/admin/detalles";
    }
}