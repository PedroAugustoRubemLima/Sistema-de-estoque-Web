package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Venda;
import com.seuprojeto.lojadesktop.service.VendaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @PostMapping
    public Venda registrar(@RequestBody Venda venda) {
        return service.registrarVenda(venda);
    }

    @GetMapping("/periodo")
    public List<Venda> porPeriodo(@RequestParam String inicio,
                                  @RequestParam String fim) {

        return service.buscarPorPeriodo(
                LocalDate.parse(inicio),
                LocalDate.parse(fim)
        );
    }
}
