package com.seuprojeto.lojadesktop.controller;

import com.seuprojeto.lojadesktop.model.Estoque;
import com.seuprojeto.lojadesktop.model.Produto;
import com.seuprojeto.lojadesktop.service.EstoqueService;
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
@RequestMapping("/api/estoque")
@CrossOrigin
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @GetMapping
    public List<Estoque> listar() {
        return service.listar();
    }

    @PostMapping("/retirar")
    public void retirar(@RequestParam Integer produtoId,
                        @RequestParam Double quantidade) {

        service.retirarEstoque(produtoId, quantidade);
    }

    @GetMapping("/baixo")
    public List<Estoque> listarBaixoEstoque(@RequestParam("limiteKg") Double limiteKg) {
        return service.listarComBaixoEstoque(limiteKg);
    }

    @GetMapping("/proximos-vencimento")
    public List<Produto> listarProximosVencimento(@RequestParam("dias") Integer dias) {
        return service.produtosProximosVencimento(dias);
    }

    // PDFs
    @GetMapping(value = "/baixo/pdf", produces = "application/pdf")
    public @ResponseBody byte[] pdfBaixoEstoque(
            @RequestParam("limiteKg") Double limiteKg
    ) throws IOException {
        List<Estoque> itens = service.listarComBaixoEstoque(limiteKg);
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(50, 800);
                cs.showText("Produtos com Baixo Estoque");
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 10);
                float y = 780;
                for (Estoque e : itens) {
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
                            e.getProduto().getNome(),
                            e.getProduto().getTipo(),
                            e.getQuantidadeAtual() != null ? e.getQuantidadeAtual() : 0.0
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

    @GetMapping(value = "/proximos-vencimento/pdf", produces = "application/pdf")
    public @ResponseBody byte[] pdfProximosVencimento(
            @RequestParam("dias") Integer dias
    ) throws IOException {
        List<Produto> produtos = service.produtosProximosVencimento(dias);
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(50, 800);
                cs.showText("Produtos Próximos do Vencimento");
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 10);
                float y = 780;
                for (Produto p : produtos) {
                    if (y < 60) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y = 800;
                    }
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    String linha = String.format(
                            "Produto: %s | Tipo: %s | Vencimento: %s",
                            p.getNome(),
                            p.getTipo(),
                            p.getDataVencimento()
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
