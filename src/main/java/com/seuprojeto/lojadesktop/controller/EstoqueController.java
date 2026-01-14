package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Estoque;
import com.seuprojeto.lojadesktop.service.EstoqueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@CrossOrigin
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @GetMapping
    public List<Estoque> listar() {
        return service.listar();
    }

    @PostMapping("/retirar")
    public void retirar(@RequestParam Integer produtoId,
                        @RequestParam Double quantidade) {

        service.retirarEstoque(produtoId, quantidade);
    }
}
