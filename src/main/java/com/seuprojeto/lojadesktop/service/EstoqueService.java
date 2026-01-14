package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Estoque;
import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.repository.EstoqueRepository;
import com.seuprojeto.lojadesktop.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
