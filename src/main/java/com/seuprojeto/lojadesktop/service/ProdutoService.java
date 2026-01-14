package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {
        produto.setAtivo(true);
        return repository.save(produto);
    }

    public List<Produto> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public void inativar(Integer id) {
        Produto produto = repository.findById(id).orElseThrow();
        produto.setAtivo(false);
        repository.save(produto);
    }
}
