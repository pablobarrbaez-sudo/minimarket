package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Usuario;
import com.jpbb.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login/admin")
    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
        Optional<Usuario> u = usuarioRepository.findByUsernameAndPassword(username, password);
        if (u.isPresent() && "ADMIN".equals(u.get().getRol())) {
            return "redirect:/admin";
        }
        return "redirect:/?error";
    }

    @PostMapping("/login/usuario")
    public String loginUsuario(@RequestParam String nombre) {
        Optional<Usuario> u = usuarioRepository.findByUsername(nombre);
        if (u.isPresent()) {
            return "redirect:/usuario";
        }
        return "redirect:/?error";
    }
}