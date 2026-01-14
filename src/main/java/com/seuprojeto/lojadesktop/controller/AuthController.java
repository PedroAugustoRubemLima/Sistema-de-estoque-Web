package com.seuprojeto.lojadesktop.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    @PostMapping("/login")
    public boolean login(
            @RequestParam String usuario,
            @RequestParam String senha
    ) {
        return "admin".equals(usuario) && "123".equals(senha);
    }
}
