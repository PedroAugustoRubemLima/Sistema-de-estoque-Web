package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.dto.ProdutoMaisVendidoDTO;
import com.seuprojeto.lojadesktop.dto.RelatorioVendasPorPeriodoDTO;
import com.seuprojeto.lojadesktop.model.Estoque;
import com.seuprojeto.lojadesktop.model.ItemVenda;
import com.seuprojeto.lojadesktop.model.Venda;
import com.seuprojeto.lojadesktop.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        try {
            venda.setDataVenda(LocalDate.now());

            double total = 0.0;

            // Validar se há itens
            if (venda.getItens() == null || venda.getItens().isEmpty()) {
                throw new RuntimeException("A venda deve ter pelo menos um item");
            }

            for (ItemVenda item : venda.getItens()) {
                // Validar item
                if (item.getProduto() == null || item.getProduto().getIdProduto() == null) {
                    throw new RuntimeException("Produto inválido no item da venda");
                }
                
                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    throw new RuntimeException("Quantidade inválida no item da venda");
                }
                
                if (item.getPrecoUnitario() == null || item.getPrecoUnitario() <= 0) {
                    throw new RuntimeException("Preço inválido no item da venda");
                }

                total += item.getQuantidade() * item.getPrecoUnitario();
                item.setVenda(venda);
            }

            venda.setValorTotal(total);
            Venda vendaSalva = vendaRepository.save(venda);

            return vendaSalva;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar venda: " + e.getMessage(), e);
        }
    }

    public List<Venda> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public List<RelatorioVendasPorPeriodoDTO> relatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Venda> vendas = buscarPorPeriodo(inicio, fim);

        Map<LocalDate, DoubleSummaryStatistics> agrupado = vendas.stream()
                .collect(Collectors.groupingBy(
                        Venda::getDataVenda,
                        Collectors.summarizingDouble(v -> v.getValorTotal() != null ? v.getValorTotal() : 0.0)
                ));

        List<RelatorioVendasPorPeriodoDTO> resultado = new ArrayList<>();
        for (Map.Entry<LocalDate, DoubleSummaryStatistics> entry : agrupado.entrySet()) {
            resultado.add(new RelatorioVendasPorPeriodoDTO(
                    entry.getKey(),
                    entry.getValue().getSum()
            ));
        }

        resultado.sort(Comparator.comparing(RelatorioVendasPorPeriodoDTO::dataVenda));
        return resultado;
    }

    public List<ProdutoMaisVendidoDTO> relatorioProdutosMaisVendidos(LocalDate inicio, LocalDate fim) {
        List<Venda> vendas = buscarPorPeriodo(inicio, fim);

        Map<String, Double> quantidadePorProduto = vendas.stream()
                .flatMap(v -> v.getItens().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduto().getIdProduto() + "|" +
                                item.getProduto().getNome() + "|" +
                                item.getProduto().getTipo(),
                        Collectors.summingDouble(item -> item.getQuantidade() != null ? item.getQuantidade() : 0.0)
                ));

        List<ProdutoMaisVendidoDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, Double> entry : quantidadePorProduto.entrySet()) {
            String[] partes = entry.getKey().split("\\|", 3);
            String nome = partes.length > 1 ? partes[1] : "";
            String tipo = partes.length > 2 ? partes[2] : "";
            resultado.add(new ProdutoMaisVendidoDTO(
                    nome,
                    tipo,
                    entry.getValue()
            ));
        }

        resultado.sort(Comparator.comparing(ProdutoMaisVendidoDTO::quantidadeVendida).reversed());
        return resultado;
    }
}
