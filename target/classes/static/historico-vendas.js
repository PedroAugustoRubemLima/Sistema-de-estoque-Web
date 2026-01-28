async function carregarVendas() {
    try {
        const response = await fetch("http://localhost:8081/api/historico");
        if (!response.ok) {
            throw new Error('Erro ao carregar vendas');
        }
        
        const vendas = await response.json();
        const tabela = document.getElementById("tabelaVendas");
        tabela.innerHTML = "";

        if (vendas.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="5" style="text-align: center; color: #e0e0e0;">Nenhuma venda registrada</td>`;
            tabela.appendChild(row);
            return;
        }

        vendas.forEach(v => {
            const row = document.createElement("tr");
            
            // Formatando a data
            const dataFormatada = new Date(v.dataVenda).toLocaleDateString('pt-BR');
            
            row.innerHTML = `
                <td>${v.idVenda}</td>
                <td>${dataFormatada}</td>
                <td>${v.cliente ? v.cliente.nome : 'N/A'}</td>
                <td>${v.funcionario ? v.funcionario.nome : 'N/A'}</td>
                <td>R$ ${v.valorTotal ? v.valorTotal.toFixed(2) : '0.00'}</td>
            `;

            tabela.appendChild(row);
        });
    } catch (error) {
        console.error('Erro ao carregar vendas:', error);
        const tabela = document.getElementById("tabelaVendas");
        tabela.innerHTML = `<tr><td colspan="5" style="text-align: center; color: #ff4d4d;">Erro ao carregar vendas: ${error.message}</td></tr>`;
    }
}

carregarVendas();
