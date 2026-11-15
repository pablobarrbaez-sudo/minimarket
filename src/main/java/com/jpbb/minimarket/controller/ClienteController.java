package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Cliente;
import com.jpbb.minimarket.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepo;

    @GetMapping
    public String listar(Model m) {
        m.addAttribute("clientes", clienteRepo.findAll());
        m.addAttribute("cliente", new Cliente());
        return "admin-clientes";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteRepo.save(cliente);
        return "redirect:/admin/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model m) {
        m.addAttribute("clientes", clienteRepo.findAll());
        m.addAttribute("cliente", clienteRepo.findById(id).orElse(new Cliente()));
        return "admin-clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        clienteRepo.deleteById(id);
        return "redirect:/admin/clientes";
    }
}