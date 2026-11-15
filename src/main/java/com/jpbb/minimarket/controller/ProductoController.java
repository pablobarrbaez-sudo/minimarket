package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Categoria;
import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.repository.CategoriaRepository;
import com.jpbb.minimarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @GetMapping
    public String listar(Model m) {
        m.addAttribute("productos", productoRepo.findAll());
        m.addAttribute("categorias", categoriaRepo.findAll());
        m.addAttribute("producto", new Producto());
        return "admin-productos";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Map<String, String> params) {
        Producto producto = new Producto();

        String idStr = params.get("id_producto");
        if (idStr != null && !idStr.trim().isEmpty()) {
            Integer id = Integer.parseInt(idStr.trim());
            producto = productoRepo.findById(id).orElse(new Producto());
            producto.setId_producto(id);
        }

        producto.setNombre(params.get("nombre") != null ? params.get("nombre").trim() : "");
        producto.setMarca(params.get("marca") != null ? params.get("marca").trim() : "");
        producto.setPrecio(params.get("precio") != null && !params.get("precio").isEmpty()
                ? Double.parseDouble(params.get("precio")) : 0.0);
        producto.setStock(params.get("stock") != null && !params.get("stock").isEmpty()
                ? Integer.parseInt(params.get("stock")) : 0);

        String catStr = params.get("categoria");
        if (catStr != null && !catStr.trim().isEmpty()) {
            Integer idCat = Integer.parseInt(catStr.trim());
            Categoria cat = categoriaRepo.findById(idCat).orElse(null);
            producto.setCategoria(cat);
        }

        productoRepo.save(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model m) {
        m.addAttribute("productos", productoRepo.findAll());
        m.addAttribute("categorias", categoriaRepo.findAll());
        m.addAttribute("producto", productoRepo.findById(id).orElse(new Producto()));
        return "admin-productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoRepo.deleteById(id);
        return "redirect:/admin/productos";
    }
}