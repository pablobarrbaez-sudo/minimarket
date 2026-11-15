package com.jpbb.minimarket.controller;

import com.jpbb.minimarket.model.Usuario;
import com.jpbb.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/menu")
    public String menu() {
        return "admin-menu";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model m) {
        m.addAttribute("usuarios", usuarioRepo.findAll());
        m.addAttribute("usuario", new Usuario());
        return "admin-usuarios";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam Map<String, String> params, Model m) {
        Usuario usuario = new Usuario();
        try {
            String idStr = params.get("id_usuario");
            if (idStr != null && !idStr.trim().isEmpty()) {
                usuario.setId_usuario(Integer.parseInt(idStr.trim()));
            }

            String nombreLimpio = params.get("nombre") != null ? params.get("nombre").trim() : "";
            if (nombreLimpio.length() > 30) {
                nombreLimpio = nombreLimpio.substring(0, 30);
            }
            usuario.setNombre(nombreLimpio);

            String userParam = params.get("usuario");
            String userLogin = (userParam != null && !userParam.trim().isEmpty())
                    ? userParam.trim()
                    : nombreLimpio.toLowerCase().replaceAll("\\s+", "");
            if (userLogin.length() > 20) {
                userLogin = userLogin.substring(0, 20);
            }
            usuario.setUsuario(userLogin);
            usuario.setUsername(userLogin);

            String rolParam = params.get("rol");
            usuario.setRol((rolParam != null && !rolParam.trim().isEmpty()) ? rolParam.trim() : "USUARIO");

            final String loginFinal = userLogin;
            boolean duplicado = usuarioRepo.findAll().stream()
                    .anyMatch(u -> (usuario.getId_usuario() == null || !u.getId_usuario().equals(usuario.getId_usuario()))
                            && (u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(loginFinal)));

            if (duplicado) {
                m.addAttribute("error", "El usuario '" + loginFinal + "' ya existe. Elige otro.");
                m.addAttribute("usuarios", usuarioRepo.findAll());
                m.addAttribute("usuario", usuario);
                return "admin-usuarios";
            }

            String passParam = params.get("password");
            if (passParam == null || passParam.trim().isEmpty()) {
                if (usuario.getId_usuario() != null) {
                    Usuario existente = usuarioRepo.findById(usuario.getId_usuario()).orElse(null);
                    usuario.setPassword(existente != null ? existente.getPassword() : "123");
                } else {
                    usuario.setPassword("123");
                }
            } else {
                String passLimpia = passParam.trim();
                usuario.setPassword(passLimpia.length() > 20 ? passLimpia.substring(0, 20) : passLimpia);
            }

            usuarioRepo.save(usuario);
            return "redirect:/admin/usuarios";

        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("error", "No se pudo guardar: " + e.getMessage());
            m.addAttribute("usuarios", usuarioRepo.findAll());
            m.addAttribute("usuario", usuario);
            return "admin-usuarios";
        }
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model m) {
        m.addAttribute("usuarios", usuarioRepo.findAll());
        m.addAttribute("usuario", usuarioRepo.findById(id).orElse(new Usuario()));
        return "admin-usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioRepo.deleteById(id);
        return "redirect:/admin/usuarios";
    }
}