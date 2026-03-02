// Carregar funcionários ao iniciar a página
document.addEventListener('DOMContentLoaded', function() {
    carregarFuncionarios();
});

// Cadastrar funcionário
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
        const response = await fetch("/api/funcionarios", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(funcionario)
        });

        if (response.ok) {
            alert("Funcionário cadastrado com sucesso!");
            e.target.reset();
            carregarFuncionarios(); // Recarregar lista
        } else {
            const error = await response.text();
            alert("Erro ao cadastrar funcionário: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});

// Carregar lista de funcionários
async function carregarFuncionarios() {
    try {
        const response = await fetch("/api/funcionarios");
        const funcionarios = await response.json();
        
        const tbody = document.querySelector(".table-wrapper tbody");
        
        if (funcionarios.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; padding: 20px; color: #6b7280;">
                        Nenhum funcionário cadastrado
                    </td>
                </tr>
            `;
            return;
        }
        
        tbody.innerHTML = funcionarios.map(func => `
            <tr>
                <td>${func.nome}</td>
                <td>${func.cpf || '-'}</td>
                <td>${func.cargo || '-'}</td>
                <td>
                    <button onclick="excluirFuncionario(${func.idFuncionario})" class="btn btn-red" style="padding: 5px 10px; font-size: 12px;">
                        Excluir
                    </button>
                </td>
            </tr>
        `).join('');
        
    } catch (error) {
        console.error("Erro ao carregar funcionários:", error);
    }
}

// Excluir funcionário
async function excluirFuncionario(id) {
    if (!confirm("Deseja realmente excluir este funcionário?")) {
        return;
    }
    
    try {
        const response = await fetch(`/api/funcionarios/${id}`, {
            method: "DELETE"
        });
        
        if (response.ok) {
            alert("Funcionário excluído com sucesso!");
            carregarFuncionarios();
        } else {
            alert("Erro ao excluir funcionário");
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
    }
}
