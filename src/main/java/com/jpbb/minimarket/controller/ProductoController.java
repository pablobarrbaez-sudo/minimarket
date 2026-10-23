package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository repo;

    // ========= USUARIO: VER Y COMPRAR =========
    @GetMapping("/usuario/productos")
    public String verProductos(Model model, HttpSession session) {
        model.addAttribute("productos", repo.findAll());
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();
        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "usuario-productos";
    }

    @GetMapping("/usuario/comprar/{id}")
    public String comprar(@PathVariable Integer id, HttpSession session) {
        Producto p = repo.findById(id).orElse(null);
        if (p != null) {
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) carrito = new ArrayList<>();
            carrito.add(p);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/usuario/productos";
    }

    @GetMapping("/usuario/quitar/{index}")
    public String quitar(@PathVariable int index, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null && index < carrito.size()) carrito.remove(index);
        return "redirect:/usuario/productos";
    }

    @GetMapping("/usuario/pagar")
    public String pagar(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/usuario/productos?pagado";
    }

    // ========= ADMIN: AGREGAR / SUBIR PRECIO / QUITAR =========
    @GetMapping("/admin/productos")
    public String adminProductos(Model model) {
        model.addAttribute("productos", repo.findAll());
        model.addAttribute("nuevo", new Producto());
        return "admin-productos";
    }

    @PostMapping("/admin/productos/agregar")
    public String agregar(@ModelAttribute Producto nuevo) {
        if (nuevo.getStock() == null) nuevo.setStock(10);
        repo.save(nuevo);
        return "redirect:/admin/productos";
    }

    @PostMapping("/admin/productos/precio/{id}")
    public String cambiarPrecio(@PathVariable Integer id, @RequestParam Double precio) {
        Producto p = repo.findById(id).orElse(null);
        if (p != null) { p.setPrecio(precio); repo.save(p); }
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        repo.deleteById(id);
        return "redirect:/admin/productos";
    }
}