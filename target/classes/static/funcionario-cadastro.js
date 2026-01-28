document.getElementById("funcionarioForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const funcionario = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        cargo: document.getElementById("cargo").value,
        usuario: document.getElementById("usuario").value,
        senha: document.getElementById("senha").value
    };

    try {
        const response = await fetch("http://localhost:8081/api/funcionarios", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(funcionario)
        });

        if (response.ok) {
            alert("Funcionário cadastrado com sucesso!");
            e.target.reset();
        } else {
            const error = await response.text();
            alert("Erro ao cadastrar funcionário: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});
