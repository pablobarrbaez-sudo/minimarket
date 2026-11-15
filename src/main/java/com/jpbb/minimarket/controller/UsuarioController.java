package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.DetalleVenta;
import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.model.Venta;
import com.jpbb.minimarket.repository.CategoriaRepository;
import com.jpbb.minimarket.repository.DetalleVentaRepository;
import com.jpbb.minimarket.repository.ProductoRepository;
import com.jpbb.minimarket.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired private ProductoRepository productoRepo;
    @Autowired private CategoriaRepository categoriaRepo;
    @Autowired private VentaRepository ventaRepo;
    @Autowired private DetalleVentaRepository detalleVentaRepo;

    @GetMapping("/productos")
    public String listarProductos(Model m) {
        m.addAttribute("productos", productoRepo.findAll());
        return "usuario-productos";
    }

    @GetMapping("/categorias")
    public String listarCategorias(Model m) {
        m.addAttribute("categorias", categoriaRepo.findAll());
        return "usuario-categorias";
    }

    @GetMapping("/compras")
    public String listarCompras(Model m) {
        m.addAttribute("ventas", ventaRepo.findAll());
        return "usuario-compras";
    }

    public static class ItemCompra {
        private Integer id_producto;
        private Integer cantidad;
        private Double precio;

        public Integer getId_producto() { return id_producto; }
        public void setId_producto(Integer id_producto) { this.id_producto = id_producto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
    }

    @PostMapping("/pagar")
    @ResponseBody
    public ResponseEntity<?> realizarPago(@RequestBody List<ItemCompra> items) {
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body("El carrito está vacío");
        }

        double totalVenta = 0.0;
        for (ItemCompra item : items) {
            totalVenta += item.getPrecio() * item.getCantidad();
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setTotal(totalVenta);
        nuevaVenta = ventaRepo.save(nuevaVenta);

        for (ItemCompra item : items) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId_venta(nuevaVenta.getId_venta());
            detalle.setId_producto(item.getId_producto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecio_unitario(item.getPrecio());
            detalle.setSubtotal(item.getPrecio() * item.getCantidad());
            detalleVentaRepo.save(detalle);

            if (item.getId_producto() != null) {
                Producto prod = productoRepo.findById(item.getId_producto()).orElse(null);
                if (prod != null && prod.getStock() != null) {
                    int nuevoStock = Math.max(0, prod.getStock() - item.getCantidad());
                    prod.setStock(nuevoStock);
                    productoRepo.save(prod);
                }
            }
        }

        return ResponseEntity.ok(Map.of("status", "ok", "id_venta", nuevaVenta.getId_venta()));
    }
}