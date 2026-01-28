package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Compra;
import com.seuprojeto.lojadesktop.model.ComPro;
import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.repository.ComProRepository;
import com.seuprojeto.lojadesktop.repository.CompraRepository;
import com.seuprojeto.lojadesktop.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProdutoRepository produtoRepository;
    private final ComProRepository comProRepository;

    public CompraService(CompraRepository compraRepository,
                         ProdutoRepository produtoRepository,
                         ComProRepository comProRepository) {
        this.compraRepository = compraRepository;
        this.produtoRepository = produtoRepository;
        this.comProRepository = comProRepository;
    }

    public Compra registrarCompra(Compra compra) {
        Compra salva = compraRepository.save(compra);

        // Corrigido: Apenas produtos desta compra específica
        for (ComPro cp : salva.getItens()) {
            Produto produto = cp.getProduto();
            produto.setQuantidade(
                    produto.getQuantidade() + cp.getQuantidade()
            );
            produtoRepository.save(produto);
        }

        return salva;
    }
}

