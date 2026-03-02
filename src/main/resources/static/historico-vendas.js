// Utilitário para formatar valores monetários
function formatCurrency(value) {
    return value != null
        ? value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
        : 'R$ 0,00';
}

// ============================
// ABA 1 - HISTÓRICO DETALHADO
// ============================
async function carregarHistoricoDetalhado() {
    try {
        const response = await fetch("/api/historico");
        if (!response.ok) {
            throw new Error('Erro ao carregar vendas');
        }

        const vendas = await response.json();
        const tabela = document.getElementById("tabelaDetalhado");
        tabela.innerHTML = "";

        if (!vendas || vendas.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="4" style="text-align: center; color: #6b7280;">Nenhuma venda registrada</td>`;
            tabela.appendChild(row);
            return;
        }

        vendas.forEach(v => {
            const row = document.createElement("tr");
            const dataFormatada = new Date(v.dataVenda).toLocaleDateString('pt-BR');

            row.innerHTML = `
                <td>${v.cliente ? v.cliente.nome : 'N/A'}</td>
                <td>${v.funcionario ? v.funcionario.nome : 'N/A'}</td>
                <td>${dataFormatada}</td>
                <td>${formatCurrency(v.valorTotal || 0)}</td>
            `;

            tabela.appendChild(row);
        });
    } catch (error) {
        console.error('Erro ao carregar vendas:', error);
        const tabela = document.getElementById("tabelaDetalhado");
        tabela.innerHTML = `<tr><td colspan="4" style="text-align: center; color: #ff4d4d;">Erro ao carregar vendas: ${error.message}</td></tr>`;
    }
}

// ============================
// ABA 2 - RELATÓRIO POR PERÍODO
// ============================
async function carregarRelatorioPorPeriodo() {
    const inicio = document.getElementById("dataInicialPeriodo").value;
    const fim = document.getElementById("dataFinalPeriodo").value;

    if (!inicio || !fim) {
        alert("Informe data inicial e final.");
        return;
    }

    try {
        const response = await fetch(`/api/historico/por-periodo?inicio=${inicio}&fim=${fim}`);
        if (!response.ok) {
            throw new Error('Erro ao gerar relatório por período');
        }

        const relatorio = await response.json();
        const tabela = document.getElementById("tabelaPeriodo");
        tabela.innerHTML = "";

        if (!relatorio || relatorio.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="2" style="text-align: center; color: #6b7280;">Nenhuma venda no período informado</td>`;
            tabela.appendChild(row);
            return;
        }

        relatorio.forEach(r => {
            const row = document.createElement("tr");
            const dataFormatada = new Date(r.dataVenda).toLocaleDateString('pt-BR');
            row.innerHTML = `
                <td>${dataFormatada}</td>
                <td>${formatCurrency(r.totalVendas || 0)}</td>
            `;
            tabela.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        const tabela = document.getElementById("tabelaPeriodo");
        tabela.innerHTML = `<tr><td colspan="2" style="text-align: center; color: #ff4d4d;">${error.message}</td></tr>`;
    }
}

// ============================
// ABA 3 - PRODUTOS MAIS VENDIDOS
// ============================
async function carregarProdutosMaisVendidos() {
    const inicio = document.getElementById("dataInicialProdutos").value;
    const fim = document.getElementById("dataFinalProdutos").value;

    if (!inicio || !fim) {
        alert("Informe data inicial e final.");
        return;
    }

    try {
        const response = await fetch(`/api/historico/produtos-mais-vendidos?inicio=${inicio}&fim=${fim}`);
        if (!response.ok) {
            throw new Error('Erro ao gerar relatório de produtos mais vendidos');
        }

        const relatorio = await response.json();
        const tabela = document.getElementById("tabelaProdutosVendidos");
        tabela.innerHTML = "";

        if (!relatorio || relatorio.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="3" style="text-align: center; color: #6b7280;">Nenhuma venda no período informado</td>`;
            tabela.appendChild(row);
            return;
        }

        relatorio.forEach(r => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${r.nomeProduto}</td>
                <td>${r.tipoProduto}</td>
                <td>${r.quantidadeVendida}</td>
            `;
            tabela.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        const tabela = document.getElementById("tabelaProdutosVendidos");
        tabela.innerHTML = `<tr><td colspan="3" style="text-align: center; color: #ff4d4d;">${error.message}</td></tr>`;
    }
}

// ============================
// ABA 4 - RELATÓRIOS DE ESTOQUE
// ============================
async function carregarBaixoEstoque() {
    const limite = document.getElementById("limiteBaixoEstoque").value;
    if (!limite) {
        alert("Informe o limite em KG.");
        return;
    }

    try {
        const response = await fetch(`/api/estoque/baixo?limiteKg=${limite}`);
        if (!response.ok) {
            throw new Error('Erro ao gerar relatório de baixo estoque');
        }

        const itens = await response.json();
        const tabela = document.getElementById("tabelaBaixoEstoque");
        tabela.innerHTML = "";

        if (!itens || itens.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="3" style="text-align: center; color: #6b7280;">Nenhum produto com baixo estoque</td>`;
            tabela.appendChild(row);
            return;
        }

        itens.forEach(e => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${e.produto.nome}</td>
                <td>${e.produto.tipo}</td>
                <td>${e.quantidadeAtual}</td>
            `;
            tabela.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        const tabela = document.getElementById("tabelaBaixoEstoque");
        tabela.innerHTML = `<tr><td colspan="3" style="text-align: center; color: #ff4d4d;">${error.message}</td></tr>`;
    }
}

async function carregarProximosVencimento() {
    const dias = document.getElementById("diasParaVencer").value;
    if (!dias) {
        alert("Informe a quantidade de dias para vencer.");
        return;
    }

    try {
        const response = await fetch(`/api/estoque/proximos-vencimento?dias=${dias}`);
        if (!response.ok) {
            throw new Error('Erro ao gerar relatório de produtos próximos do vencimento');
        }

        const itens = await response.json();
        const tabela = document.getElementById("tabelaProximosVencimento");
        tabela.innerHTML = "";

        if (!itens || itens.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `<td colspan="3" style="text-align: center; color: #6b7280;">Nenhum produto próximo do vencimento</td>`;
            tabela.appendChild(row);
            return;
        }

        itens.forEach(p => {
            const row = document.createElement("tr");
            const dataFormatada = p.dataVencimento
                ? new Date(p.dataVencimento).toLocaleDateString('pt-BR')
                : '-';
            row.innerHTML = `
                <td>${p.nome}</td>
                <td>${p.tipo}</td>
                <td>${dataFormatada}</td>
            `;
            tabela.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        const tabela = document.getElementById("tabelaProximosVencimento");
        tabela.innerHTML = `<tr><td colspan="3" style="text-align: center; color: #ff4d4d;">${error.message}</td></tr>`;
    }
}

// ============================
// PDF
// ============================
async function baixarPdf(url, nomeArquivo) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Erro ao gerar PDF');
        }

        const blob = await response.blob();
        const link = document.createElement('a');
        const urlBlob = URL.createObjectURL(blob);
        link.href = urlBlob;
        link.download = nomeArquivo;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(urlBlob);
    } catch (error) {
        alert(error.message);
    }
}

// ============================
// TABS / EVENTOS
// ============================
function selecionarTab(ativa) {
    const tabs = [
        { btn: 'tab-detalhado-btn', content: 'tab-detalhado' },
        { btn: 'tab-periodo-btn', content: 'tab-periodo' },
        { btn: 'tab-produtos-btn', content: 'tab-produtos' },
        { btn: 'tab-estoque-btn', content: 'tab-estoque' },
    ];

    tabs.forEach(t => {
        const btn = document.getElementById(t.btn);
        const content = document.getElementById(t.content);
        if (!btn || !content) return;

        if (t.content === ativa) {
            btn.classList.add('active');
            btn.classList.remove('inactive');
            content.classList.add('active');
        } else {
            btn.classList.remove('active');
            btn.classList.add('inactive');
            content.classList.remove('active');
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    // Aba inicial
    carregarHistoricoDetalhado();

    // Tabs
    document.getElementById('tab-detalhado-btn')?.addEventListener('click', () => {
        selecionarTab('tab-detalhado');
        carregarHistoricoDetalhado();
    });

    document.getElementById('tab-periodo-btn')?.addEventListener('click', () => {
        selecionarTab('tab-periodo');
    });

    document.getElementById('tab-produtos-btn')?.addEventListener('click', () => {
        selecionarTab('tab-produtos');
    });

    document.getElementById('tab-estoque-btn')?.addEventListener('click', () => {
        selecionarTab('tab-estoque');
    });

    // Botões de relatório
    document.getElementById('btn-periodo-relatorio')?.addEventListener('click', carregarRelatorioPorPeriodo);
    document.getElementById('btn-produtos-relatorio')?.addEventListener('click', carregarProdutosMaisVendidos);
    document.getElementById('btn-baixo-estoque-relatorio')?.addEventListener('click', carregarBaixoEstoque);
    document.getElementById('btn-proximos-relatorio')?.addEventListener('click', carregarProximosVencimento);

    // Botões de PDF
    document.getElementById('btn-detalhado-pdf')?.addEventListener('click', () =>
        baixarPdf('/api/historico/pdf', 'historico-detalhado.pdf')
    );

    document.getElementById('btn-periodo-pdf')?.addEventListener('click', () => {
        const inicio = document.getElementById("dataInicialPeriodo").value;
        const fim = document.getElementById("dataFinalPeriodo").value;
        if (!inicio || !fim) {
            alert("Informe data inicial e final.");
            return;
        }
        baixarPdf(`/api/historico/por-periodo/pdf?inicio=${inicio}&fim=${fim}`, 'relatorio-periodo.pdf');
    });

    document.getElementById('btn-produtos-pdf')?.addEventListener('click', () => {
        const inicio = document.getElementById("dataInicialProdutos").value;
        const fim = document.getElementById("dataFinalProdutos").value;
        if (!inicio || !fim) {
            alert("Informe data inicial e final.");
            return;
        }
        baixarPdf(`/api/historico/produtos-mais-vendidos/pdf?inicio=${inicio}&fim=${fim}`, 'produtos-mais-vendidos.pdf');
    });

    document.getElementById('btn-baixo-estoque-pdf')?.addEventListener('click', () => {
        const limite = document.getElementById("limiteBaixoEstoque").value;
        if (!limite) {
            alert("Informe o limite em KG.");
            return;
        }
        baixarPdf(`/api/estoque/baixo/pdf?limiteKg=${limite}`, 'baixo-estoque.pdf');
    });

    document.getElementById('btn-proximos-pdf')?.addEventListener('click', () => {
        const dias = document.getElementById("diasParaVencer").value;
        if (!dias) {
            alert("Informe a quantidade de dias para vencer.");
            return;
        }
        baixarPdf(`/api/estoque/proximos-vencimento/pdf?dias=${dias}`, 'proximos-vencimento.pdf');
    });
});
