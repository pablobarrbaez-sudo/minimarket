package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.DetalleVenta;
import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.model.Venta;
import com.jpbb.minimarket.repository.DetalleVentaRepository;
import com.jpbb.minimarket.repository.ProductoRepository;
import com.jpbb.minimarket.repository.VentaRepository;
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
@RequestMapping("/admin/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepo;

    @Autowired
    private DetalleVentaRepository detalleRepo;

    @Autowired
    private ProductoRepository productoRepo;

    public static class ItemDTO {
        private String nombreProducto;
        private Integer cantidad;
        private Double precio_unitario;
        private Double subtotal;

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecio_unitario() { return precio_unitario; }
        public void setPrecio_unitario(Double precio_unitario) { this.precio_unitario = precio_unitario; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }

    public static class VentaDTO {
        private Integer id_venta;
        private Double total;
        private List<ItemDTO> items = new ArrayList<>();

        public Integer getId_venta() { return id_venta; }
        public void setId_venta(Integer id_venta) { this.id_venta = id_venta; }
        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }
        public List<ItemDTO> getItems() { return items; }
        public void setItems(List<ItemDTO> items) { this.items = items; }
    }

    @GetMapping
    public String listar(Model m) {
        List<Venta> todasVentas = ventaRepo.findAll();
        List<DetalleVenta> todosDetalles = detalleRepo.findAll();
        List<Producto> todosProductos = productoRepo.findAll();

        Map<Integer, String> mapaProductos = new HashMap<>();
        for (Producto p : todosProductos) {
            mapaProductos.put(p.getId_producto(), p.getNombre());
        }

        List<VentaDTO> listaVentas = new ArrayList<>();
        for (Venta v : todasVentas) {
            VentaDTO vDto = new VentaDTO();
            vDto.setId_venta(v.getId_venta());
            vDto.setTotal(v.getTotal());

            for (DetalleVenta d : todosDetalles) {
                if (d.getId_venta() != null && d.getId_venta().equals(v.getId_venta())) {
                    ItemDTO item = new ItemDTO();
                    String nombre = mapaProductos.get(d.getId_producto());
                    item.setNombreProducto(nombre != null ? nombre : "Producto #" + d.getId_producto());
                    item.setCantidad(d.getCantidad());
                    item.setPrecio_unitario(d.getPrecio_unitario());
                    item.setSubtotal(d.getSubtotal());
                    vDto.getItems().add(item);
                }
            }
            listaVentas.add(vDto);
        }

        m.addAttribute("ventas", listaVentas);
        return "admin-ventas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        ventaRepo.deleteById(id);
        return "redirect:/admin/ventas";
    }
}