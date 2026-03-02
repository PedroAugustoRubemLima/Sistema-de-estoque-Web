package com.seuprojeto.lojadesktop.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class IndexController {

    /**
     * Redireciona a raiz (/) para a página de login.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    /**
     * Serve explicitamente a página de login para evitar 404 com segurança/recursos estáticos.
     */
    @GetMapping(value = "/index.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> loginPage() throws IOException {
        Resource resource = new ClassPathResource("static/index.html");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                .body(resource);
    }
}
