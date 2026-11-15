package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Categoria;
import com.jpbb.minimarket.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepo;

    @GetMapping
    public String listar(Model m) {
        m.addAttribute("categorias", categoriaRepo.findAll());
        m.addAttribute("categoria", new Categoria());
        return "admin-categorias";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model m) {
        m.addAttribute("categorias", categoriaRepo.findAll());
        m.addAttribute("categoria", categoriaRepo.findById(id).orElse(new Categoria()));
        return "admin-categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        categoriaRepo.deleteById(id);
        return "redirect:/admin/categorias";
    }
}