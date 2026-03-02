package com.seuprojeto.lojadesktop.dto;

public record ProdutoMaisVendidoDTO(
        String nomeProduto,
        String tipoProduto,
        Double quantidadeVendida
) {
}

