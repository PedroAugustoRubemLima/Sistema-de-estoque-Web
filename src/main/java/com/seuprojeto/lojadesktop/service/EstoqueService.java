package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Estoque;
import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.repository.EstoqueRepository;
import com.seuprojeto.lojadesktop.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository,
                          ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Estoque> listar() {
        return estoqueRepository.findAll();
    }

    public void retirarEstoque(Integer produtoId, Double quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow();
        Estoque estoque = estoqueRepository.findByProduto(produto)
                .orElseThrow();

        estoque.setQuantidadeAtual(
                estoque.getQuantidadeAtual() - quantidade
        );

        estoqueRepository.save(estoque);
    }

    public List<Estoque> listarComBaixoEstoque(Double limiteKg) {
        return estoqueRepository.findAll()
                .stream()
                .filter(e -> e.getQuantidadeAtual() != null && e.getQuantidadeAtual() <= limiteKg)
                .collect(Collectors.toList());
    }

    public List<Produto> produtosProximosVencimento(Integer dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);

        return produtoRepository.findAll()
                .stream()
                .filter(p -> p.getDataVencimento() != null
                        && !p.getDataVencimento().isBefore(hoje)
                        && !p.getDataVencimento().isAfter(limite))
                .collect(Collectors.toList());
    }
}
