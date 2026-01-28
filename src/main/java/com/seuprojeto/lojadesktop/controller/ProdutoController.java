package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.service.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Produto> listar() {
        logger.info("Listando produtos ativos");
        List<Produto> produtos = service.listarAtivos();
        logger.info("Encontrados {} produtos ativos", produtos.size());
        return produtos;
    }

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        try {
            logger.info("Salvando produto: {}", produto.getNome());
            Produto produtoSalvo = service.salvar(produto);
            logger.info("Produto salvo com ID: {}", produtoSalvo.getIdProduto());
            return ResponseEntity.ok(produtoSalvo);
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            logger.info("Inativando produto com ID: {}", id);
            service.inativar(id);
            logger.info("Produto {} inativado com sucesso", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro ao inativar produto {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
