document.getElementById("clienteForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const cliente = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        telefone: document.getElementById("telefone").value,
        email: document.getElementById("email").value
    };

    try {
        const response = await fetch("http://localhost:8081/api/clientes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cliente)
        });

        if (response.ok) {
            alert("Cliente cadastrado com sucesso!");
            e.target.reset();
        } else {
            const error = await response.text();
            alert("Erro ao cadastrar cliente: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});
