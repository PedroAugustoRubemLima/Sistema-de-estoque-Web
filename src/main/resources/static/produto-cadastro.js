document.getElementById("produtoForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value,
        tipo: document.getElementById("tipo").value,
        preco: parseFloat(document.getElementById("preco").value),
        quantidade: parseFloat(document.getElementById("quantidade").value),
        dataVencimento: document.getElementById("dataVencimento").value
    };

    try {
        const response = await fetch("/api/produtos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        });

        if (response.ok) {
            alert("Produto cadastrado com sucesso!");
            document.getElementById("produtoForm").reset();
        } else {
            const error = await response.text();
            alert("Erro ao cadastrar produto: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});
