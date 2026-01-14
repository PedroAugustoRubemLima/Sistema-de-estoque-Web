package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.*;
import com.seuprojeto.lojadesktop.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final EstoqueRepository estoqueRepository;

    public VendaService(VendaRepository vendaRepository,
                        ItemVendaRepository itemVendaRepository,
                        EstoqueRepository estoqueRepository) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    public Venda registrarVenda(Venda venda) {
        venda.setDataVenda(LocalDate.now());

        double total = 0.0;

        for (ItemVenda item : venda.getItens()) {
            total += item.getQuantidade() * item.getPrecoUnitario();

            Estoque estoque = estoqueRepository
                    .findByProduto(item.getProduto())
                    .orElseThrow();

            estoque.setQuantidadeAtual(
                    estoque.getQuantidadeAtual() - item.getQuantidade()
            );

            estoqueRepository.save(estoque);
            item.setVenda(venda);
        }

        venda.setValorTotal(total);
        Venda vendaSalva = vendaRepository.save(venda);

        itemVendaRepository.saveAll(venda.getItens());

        return vendaSalva;
    }

    public List<Venda> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }
}
