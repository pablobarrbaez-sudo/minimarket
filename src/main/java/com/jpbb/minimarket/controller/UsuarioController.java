package com.jpbb.minimarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    @GetMapping("/usuario")
    public String menuUsuario() {
        return "usuario-menu";
    }
}