async function carregarProdutos() {
    const response = await fetch("http://localhost:8081/api/produtos");
    const produtos = await response.json();

    const tabela = document.getElementById("tabelaProdutos");
    tabela.innerHTML = "";

    produtos.forEach(p => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${p.nome}</td>
            <td>${p.tipo}</td>
            <td>R$ ${p.preco}</td>
            <td>${p.quantidade}</td>
            <td>
                <button onclick="deletar(${p.idProduto})">Excluir</button>
            </td>
        `;

        tabela.appendChild(row);
    });
}

async function deletar(id) {
    await fetch(`http://localhost:8081/api/produtos/${id}`, {
        method: "DELETE"
    });
    carregarProdutos();
}

carregarProdutos();
