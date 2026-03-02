package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.dto.ProdutoMaisVendidoDTO;
import com.seuprojeto.lojadesktop.dto.RelatorioVendasPorPeriodoDTO;
import com.seuprojeto.lojadesktop.model.Venda;
import com.seuprojeto.lojadesktop.service.VendaService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/historico")
@CrossOrigin
public class HistoricoVendasController {

    private final VendaService service;

    public HistoricoVendasController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Venda> listarTudo() {
        return service.listarTodas();
    }

    @GetMapping("/por-periodo")
    public List<RelatorioVendasPorPeriodoDTO> relatorioPorPeriodo(
            @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim
    ) {
        return service.relatorioPorPeriodo(
                java.time.LocalDate.parse(inicio),
                java.time.LocalDate.parse(fim)
        );
    }

    @GetMapping("/produtos-mais-vendidos")
    public List<ProdutoMaisVendidoDTO> produtosMaisVendidos(
            @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim
    ) {
        return service.relatorioProdutosMaisVendidos(
                java.time.LocalDate.parse(inicio),
                java.time.LocalDate.parse(fim)
        );
    }

    // ============================
    // PDFs
    // ============================

    @GetMapping(value = "/pdf", produces = "application/pdf")
    public @ResponseBody byte[] pdfHistoricoDetalhado() throws IOException {
        List<Venda> vendas = service.listarTodas();
        return gerarPdfHistorico(vendas, "Histórico de Vendas");
    }

    @GetMapping(value = "/por-periodo/pdf", produces = "application/pdf")
    public @ResponseBody byte[] pdfRelatorioPorPeriodo(
            @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim
    ) throws IOException {
        var dados = service.relatorioPorPeriodo(
                java.time.LocalDate.parse(inicio),
                java.time.LocalDate.parse(fim)
        );
        return gerarPdfPeriodo(dados, "Relatório por Período");
    }

    @GetMapping(value = "/produtos-mais-vendidos/pdf", produces = "application/pdf")
    public @ResponseBody byte[] pdfProdutosMaisVendidos(
            @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim
    ) throws IOException {
        var dados = service.relatorioProdutosMaisVendidos(
                java.time.LocalDate.parse(inicio),
                java.time.LocalDate.parse(fim)
        );
        return gerarPdfProdutos(dados, "Produtos Mais Vendidos");
    }

    private byte[] gerarPdfHistorico(List<Venda> vendas, String titulo) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(50, 800);
                cs.showText(titulo);
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 10);
                float y = 780;

                for (Venda v : vendas) {
                    if (y < 60) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y = 800;
                    }
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    String linha = String.format(
                            "%s | Cliente: %s | Funcionário: %s | Total: %.2f",
                            v.getDataVenda(),
                            v.getCliente() != null ? v.getCliente().getNome() : "-",
                            v.getFuncionario() != null ? v.getFuncionario().getNome() : "-",
                            v.getValorTotal() != null ? v.getValorTotal() : 0.0
                    );
                    cs.showText(linha);
                    cs.endText();
                    y -= 14;
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }

    private byte[] gerarPdfPeriodo(List<RelatorioVendasPorPeriodoDTO> dados, String titulo) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(50, 800);
                cs.showText(titulo);
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 10);
                float y = 780;

                for (RelatorioVendasPorPeriodoDTO r : dados) {
                    if (y < 60) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y = 800;
                    }
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    String linha = String.format(
                            "Data: %s | Total: %.2f",
                            r.dataVenda(),
                            r.totalVendas() != null ? r.totalVendas() : 0.0
                    );
                    cs.showText(linha);
                    cs.endText();
                    y -= 14;
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }

    private byte[] gerarPdfProdutos(List<ProdutoMaisVendidoDTO> dados, String titulo) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(50, 800);
                cs.showText(titulo);
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 10);
                float y = 780;

                for (ProdutoMaisVendidoDTO r : dados) {
                    if (y < 60) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y = 800;
                    }
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    String linha = String.format(
                            "Produto: %s | Tipo: %s | Quantidade: %.2f",
                            r.nomeProduto(),
                            r.tipoProduto(),
                            r.quantidadeVendida() != null ? r.quantidadeVendida() : 0.0
                    );
                    cs.showText(linha);
                    cs.endText();
                    y -= 14;
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }
}
