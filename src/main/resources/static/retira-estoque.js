document.getElementById("retiraEstoqueForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const produtoId = document.getElementById("produtoId").value;
    const quantidade = document.getElementById("quantidade").value;

    await fetch(
        `http://localhost:8080/api/estoque/retirar?produtoId=${produtoId}&quantidade=${quantidade}`,
        { method: "POST" }
    );

    alert("Estoque atualizado!");
});
