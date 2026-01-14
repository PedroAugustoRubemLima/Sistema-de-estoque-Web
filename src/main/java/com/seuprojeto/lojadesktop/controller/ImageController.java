package com.seuprojeto.lojadesktop.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imagens")
@CrossOrigin
public class ImageController {

  @GetMapping
  public String[] listarImagens() {
    return new String[]{
            "/assets/product_images/queijo1.png",
            "/assets/product_images/queijo mussarela.jpg"
    };
  }
}

