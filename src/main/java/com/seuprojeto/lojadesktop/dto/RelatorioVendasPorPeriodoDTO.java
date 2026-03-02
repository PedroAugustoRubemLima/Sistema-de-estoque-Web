package com.seuprojeto.lojadesktop.dto;

import java.time.LocalDate;

public record RelatorioVendasPorPeriodoDTO(
        LocalDate dataVenda,
        Double totalVendas
) {
}

