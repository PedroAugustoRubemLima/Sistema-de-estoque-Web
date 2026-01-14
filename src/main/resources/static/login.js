document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuario = document.getElementById("usuario").value;
    const senha = document.getElementById("senha").value;
    const msg = document.getElementById("mensagem");

    try {
        const response = await fetch(
            `http://localhost:8080/api/login?usuario=${usuario}&senha=${senha}`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {
            msg.innerText = "Erro ao comunicar com o servidor";
            msg.className = "mensagem-erro";
            return;
        }

        const autenticado = await response.json();

        if (autenticado === true) {
            msg.innerText = "Login realizado com sucesso!";
            msg.className = "mensagem-sucesso";
            window.location.href = "/view/telas/produto-listagem.html";
        } else {
            msg.innerText = "Usuário ou senha inválidos";
            msg.className = "mensagem-erro";
        }

    } catch (err) {
        msg.innerText = "Erro inesperado";
        msg.className = "mensagem-erro";
    }
});

