// Carregar clientes ao iniciar a página
document.addEventListener('DOMContentLoaded', function() {
    carregarClientes();
});

// Cadastrar cliente
document.getElementById("clienteForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const cliente = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        telefone: document.getElementById("telefone").value,
        email: document.getElementById("email").value
    };

    try {
        const response = await fetch("/api/clientes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cliente)
        });

        if (response.ok) {
            alert("Cliente cadastrado com sucesso!");
            e.target.reset();
            carregarClientes(); // Recarregar lista
        } else {
            const error = await response.text();
            alert("Erro ao cadastrar cliente: " + error);
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
        console.error("Erro:", error);
    }
});

// Carregar lista de clientes
async function carregarClientes() {
    try {
        const response = await fetch("/api/clientes");
        const clientes = await response.json();
        
        const tbody = document.querySelector(".table-wrapper tbody");
        
        if (clientes.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; padding: 20px; color: #6b7280;">
                        Nenhum cliente cadastrado
                    </td>
                </tr>
            `;
            return;
        }
        
        tbody.innerHTML = clientes.map(cliente => `
            <tr>
                <td>${cliente.nome}</td>
                <td>${cliente.cpf || '-'}</td>
                <td>${cliente.telefone || '-'}</td>
                <td>
                    <button onclick="excluirCliente(${cliente.idCliente})" class="btn btn-red" style="padding: 5px 10px; font-size: 12px;">
                        Excluir
                    </button>
                </td>
            </tr>
        `).join('');
        
    } catch (error) {
        console.error("Erro ao carregar clientes:", error);
    }
}

// Excluir cliente
async function excluirCliente(id) {
    if (!confirm("Deseja realmente excluir este cliente?")) {
        return;
    }
    
    try {
        const response = await fetch(`/api/clientes/${id}`, {
            method: "DELETE"
        });
        
        if (response.ok) {
            alert("Cliente excluído com sucesso!");
            carregarClientes();
        } else {
            alert("Erro ao excluir cliente");
        }
    } catch (error) {
        alert("Erro de conexão: " + error.message);
    }
}
