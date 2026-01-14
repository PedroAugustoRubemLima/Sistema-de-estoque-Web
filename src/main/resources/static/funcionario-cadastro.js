document.getElementById("funcionarioForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const funcionario = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        cargo: document.getElementById("cargo").value,
        usuario: document.getElementById("usuario").value,
        senha: document.getElementById("senha").value
    };

    await fetch("http://localhost:8080/api/funcionarios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(funcionario)
    });

    alert("Funcionário cadastrado com sucesso!");
    e.target.reset();
});
