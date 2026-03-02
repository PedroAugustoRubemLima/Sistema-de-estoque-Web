// Aguardar o DOM carregar completamente
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById("loginForm");
    
    if (!loginForm) {
        console.error("Formulário de login não encontrado!");
        return;
    }

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const usuario = document.getElementById("usuario").value;
        const senha = document.getElementById("senha").value;
        const msg = document.getElementById("mensagem");

        try {
            const response = await fetch(
                `/api/login?usuario=${usuario}&senha=${senha}`,
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
                setTimeout(() => {
                    window.location.href = "/menu.html";
                }, 1000);
            } else {
                msg.innerText = "Usuário ou senha inválidos";
                msg.className = "mensagem-erro";
            }

        } catch (err) {
            msg.innerText = "Erro inesperado: " + err.message;
            msg.className = "mensagem-erro";
            console.error("Erro no login:", err);
        }
    });
});

