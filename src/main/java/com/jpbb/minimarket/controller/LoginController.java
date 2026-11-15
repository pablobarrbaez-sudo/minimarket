package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Usuario;
import com.jpbb.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam(required = false) String usuario,
                                @RequestParam(required = false) String password,
                                Model model) {
        if (usuario == null || usuario.trim().isEmpty()) {
            model.addAttribute("error", "Por favor ingresa un nombre de usuario.");
            return "login";
        }

        String buscado = usuario.trim();
        String passIngresada = password != null ? password.trim() : "";

        List<Usuario> usuarios = usuarioRepo.findAll();
        Usuario encontrado = usuarios.stream()
                .filter(u -> (u.getUsuario() != null && u.getUsuario().trim().equalsIgnoreCase(buscado))
                        || (u.getUsername() != null && u.getUsername().trim().equalsIgnoreCase(buscado))
                        || (u.getNombre() != null && u.getNombre().trim().equalsIgnoreCase(buscado)))
                .findFirst()
                .orElse(null);

        if (encontrado != null) {

            if (passIngresada.isEmpty()) {
                return "redirect:/usuario/menu";
            }


            if (encontrado.getPassword() != null && encontrado.getPassword().equals(passIngresada)) {
                boolean esAdmin = encontrado.getRol() != null && encontrado.getRol().trim().equalsIgnoreCase("ADMIN");
                return esAdmin ? "redirect:/admin/menu" : "redirect:/usuario/menu";
            } else {
                model.addAttribute("error", "Contraseña incorrecta.");
                return "login";
            }
        }

        if ("admin".equalsIgnoreCase(buscado)) {
            return "redirect:/admin/menu";
        }

        return "redirect:/usuario/menu";
    }

    @GetMapping("/usuario/menu")
    public String usuarioMenu() {
        return "usuario-menu";
    }
}