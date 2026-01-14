package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Venda;
import com.seuprojeto.lojadesktop.service.VendaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
@CrossOrigin
public class HistoricoVendasController {

    private final VendaService service;

    public HistoricoVendasController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Venda> listarTudo() {
        return service.listarTodas();
    }
}
