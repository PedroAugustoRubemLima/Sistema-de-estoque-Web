package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Venda;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfGeneratorService {

  public byte[] gerarRelatorioVendas(List<Venda> vendas) throws IOException {

    PDDocument document = new PDDocument();
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream content =
            new PDPageContentStream(document, page);

    content.setFont(PDType1Font.HELVETICA, 12);
    content.beginText();
    content.newLineAtOffset(50, 750);

    for (Venda venda : vendas) {
      content.showText(
              "Venda ID: " + venda.getIdVenda()
                      + " Total: R$ " + venda.getValorTotal()
      );
      content.newLineAtOffset(0, -20);
    }

    content.endText();
    content.close();

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    document.save(output);
    document.close();

    return output.toByteArray();
  }
}
