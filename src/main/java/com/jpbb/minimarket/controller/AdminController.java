package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/admin")
    public String admin() {
        return "admin-menu";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin-usuarios";
    }
}