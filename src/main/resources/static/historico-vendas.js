async function carregarVendas() {
    const response = await fetch("http://localhost:8080/api/historico");
    const vendas = await response.json();

    const tabela = document.getElementById("tabelaVendas");
    tabela.innerHTML = "";

    vendas.forEach(v => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${v.idVenda}</td>
            <td>${v.dataVenda}</td>
            <td>R$ ${v.valorTotal}</td>
        `;

        tabela.appendChild(row);
    });
}

carregarVendas();
