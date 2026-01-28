let itemCount = 1;

// Carregar dados iniciais
document.addEventListener('DOMContentLoaded', async () => {
    await carregarClientes();
    await carregarFuncionarios();
    await carregarProdutos();
});

async function carregarClientes() {
    try {
        const response = await fetch('http://localhost:8081/api/clientes');
        const clientes = await response.json();
        
        const select = document.getElementById('clienteId');
        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.idCliente;
            option.textContent = `${cliente.nome} - ${cliente.cpf}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar clientes:', error);
    }
}

async function carregarFuncionarios() {
    try {
        const response = await fetch('http://localhost:8081/api/funcionarios');
        const funcionarios = await response.json();
        
        const select = document.getElementById('funcionarioId');
        funcionarios.forEach(funcionario => {
            const option = document.createElement('option');
            option.value = funcionario.idFuncionario;
            option.textContent = `${funcionario.nome} - ${funcionario.cargo}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar funcionários:', error);
    }
}

async function carregarProdutos() {
    try {
        const response = await fetch('http://localhost:8081/api/produtos');
        const produtos = await response.json();
        
        // Atualizar todos os selects de produto
        const selects = document.querySelectorAll('select[name="produtoId"]');
        selects.forEach(select => {
            // Limpar opções existentes (exceto a primeira)
            while (select.children.length > 1) {
                select.removeChild(select.lastChild);
            }
            
            produtos.forEach(produto => {
                const option = document.createElement('option');
                option.value = produto.idProduto;
                option.textContent = `${produto.nome} - R$ ${produto.preco}`;
                option.dataset.preco = produto.preco;
                select.appendChild(option);
            });
        });
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
    }
}

// Adicionar novo item à venda
document.getElementById('adicionarItem').addEventListener('click', () => {
    const itensDiv = document.getElementById('itensVenda');
    const novoItem = document.createElement('div');
    novoItem.className = 'item-venda';
    novoItem.innerHTML = `
        <label for="produtoId${itemCount}">Produto:</label>
        <select id="produtoId${itemCount}" name="produtoId" required>
            <option value="">Selecione um produto</option>
        </select>
        
        <label for="quantidade${itemCount}">Quantidade:</label>
        <input type="number" id="quantidade${itemCount}" name="quantidade" step="0.01" min="0.01" required>
        
        <label for="precoUnitario${itemCount}">Preço Unitário:</label>
        <input type="number" id="precoUnitario${itemCount}" name="precoUnitario" step="0.01" min="0.01" required>
        
        <button type="button" onclick="removerItem(this)">❌ Remover</button>
    `;
    
    itensDiv.appendChild(novoItem);
    itemCount++;
    
    // Recarregar produtos para o novo select
    carregarProdutos();
});

function removerItem(button) {
    button.parentElement.remove();
}

// Auto-preencher preço quando produto for selecionado
document.addEventListener('change', (e) => {
    if (e.target.name === 'produtoId') {
        const selectedOption = e.target.selectedOptions[0];
        if (selectedOption && selectedOption.dataset.preco) {
            const itemDiv = e.target.closest('.item-venda');
            const precoInput = itemDiv.querySelector('input[name="precoUnitario"]');
            precoInput.value = selectedOption.dataset.preco;
        }
    }
});

// Submeter venda
document.getElementById('vendaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const clienteId = document.getElementById('clienteId').value;
    const funcionarioId = document.getElementById('funcionarioId').value;
    
    // Coletar itens
    const itens = [];
    const produtoSelects = document.querySelectorAll('select[name="produtoId"]');
    const quantidadeInputs = document.querySelectorAll('input[name="quantidade"]');
    const precoInputs = document.querySelectorAll('input[name="precoUnitario"]');
    
    for (let i = 0; i < produtoSelects.length; i++) {
        if (produtoSelects[i].value) {
            itens.push({
                produto: { idProduto: parseInt(produtoSelects[i].value) },
                quantidade: parseFloat(quantidadeInputs[i].value),
                precoUnitario: parseFloat(precoInputs[i].value)
            });
        }
    }
    
    if (itens.length === 0) {
        alert('Adicione pelo menos um item à venda!');
        return;
    }
    
    const venda = {
        cliente: { idCliente: parseInt(clienteId) },
        funcionario: { idFuncionario: parseInt(funcionarioId) },
        itens: itens
    };
    
    try {
        const response = await fetch('http://localhost:8081/api/vendas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(venda)
        });
        
        if (response.ok) {
            alert('Venda registrada com sucesso!');
            document.getElementById('vendaForm').reset();
            // Resetar itens para apenas um
            const itensDiv = document.getElementById('itensVenda');
            while (itensDiv.children.length > 1) {
                itensDiv.removeChild(itensDiv.lastChild);
            }
            itemCount = 1;
        } else {
            const error = await response.text();
            alert('Erro ao registrar venda: ' + error);
        }
    } catch (error) {
        alert('Erro de conexão: ' + error.message);
        console.error('Erro:', error);
    }
});