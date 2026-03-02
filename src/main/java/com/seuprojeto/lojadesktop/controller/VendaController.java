package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Venda;
import com.seuprojeto.lojadesktop.service.VendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin
public class VendaController {

    private static final Logger logger = LoggerFactory.getLogger(VendaController.class);
    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Venda venda) {
        try {
            logger.info("Recebendo venda: Cliente ID={}, Funcionario ID={}, Itens={}",
                    venda.getCliente() != null ? venda.getCliente().getIdCliente() : "null",
                    venda.getFuncionario() != null ? venda.getFuncionario().getIdFuncionario() : "null",
                    venda.getItens() != null ? venda.getItens().size() : 0);
            
            Venda vendaSalva = service.registrarVenda(venda);
            return ResponseEntity.ok(vendaSalva);
        } catch (Exception e) {
            logger.error("Erro ao registrar venda", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar venda: " + e.getMessage());
        }
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
