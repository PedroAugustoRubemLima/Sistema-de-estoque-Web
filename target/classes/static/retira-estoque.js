document.getElementById("retiraEstoqueForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const produtoId = document.getElementById("produtoId").value;
    const quantidade = document.getElementById("quantidade").value;

    try {
        const response = await fetch(
            `http://localhost:8081/api/estoque/retirar?produtoId=${produtoId}&quantidade=${quantidade}`,
            { method: "POST" }
        );

        if (response.ok) {
            alert("Estoque atualizado com sucesso!");
            document.getElementById("retiraEstoqueForm").reset();
        } else {
            const error = await response.text();
            alert("Erro ao atualizar estoque: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});
