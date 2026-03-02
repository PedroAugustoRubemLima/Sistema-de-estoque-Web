package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public boolean login(
            @RequestParam String usuario,
            @RequestParam String senha
    ) {
        logger.info("Tentativa de login - Usuário: {}", usuario);
        
        // Primeiro tenta autenticação com AuthService (criptografado)
        try {
            boolean autenticado = authService.autenticar(usuario, senha);
            logger.info("Autenticação via AuthService: {}", autenticado);
            if (autenticado) {
                return true;
            }
        } catch (Exception e) {
            logger.warn("Erro na autenticação via AuthService: {}", e.getMessage());
        }
        
        // Se falhar, usa fallback para desenvolvimento (admin/123)
        boolean fallback = "admin".equals(usuario) && "123".equals(senha);
        logger.info("Autenticação via fallback (admin/123): {}", fallback);
        return fallback;
    }
}
