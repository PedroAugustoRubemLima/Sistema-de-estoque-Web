document.getElementById("produtoForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value,
        tipo: document.getElementById("tipo").value,
        preco: document.getElementById("preco").value,
        quantidade: document.getElementById("quantidade").value,
        dataVencimento: document.getElementById("dataVencimento").value
    };

    await fetch("http://localhost:8080/api/produtos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    });

    alert("Produto cadastrado com sucesso!");
    document.getElementById("produtoForm").reset();
});
