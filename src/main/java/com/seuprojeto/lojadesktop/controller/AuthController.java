package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public boolean login(
            @RequestParam String usuario,
            @RequestParam String senha
    ) {
        // Primeiro tenta autenticação com AuthService (criptografado)
        try {
            return authService.autenticar(usuario, senha);
        } catch (Exception e) {
            // Se falhar, usa fallback para desenvolvimento (admin/123)
            return "admin".equals(usuario) && "123".equals(senha);
        }
    }
}
