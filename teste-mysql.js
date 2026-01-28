// Teste de conexão com MySQL
async function testarConexao() {
    console.log('🔍 Testando conexão com MySQL...');
    
    try {
        const response = await fetch('http://localhost:8081/api/test/database-info');
        const info = await response.json();
        
        console.log('✅ Conexão bem-sucedida!');
        console.log('📊 Informações do banco:');
        console.log('- Banco:', info.databaseProductName);
        console.log('- Versão:', info.databaseProductVersion);
        console.log('- URL:', info.url);
        console.log('- Usuário:', info.userName);
        
        return true;
    } catch (error) {
        console.log('❌ Erro na conexão:', error.message);
        return false;
    }
}

// Teste de cadastro de produto
async function testarCadastroProduto() {
    console.log('\n📦 Testando cadastro de produto...');
    
    const produto = {
        nome: 'Produto Teste MySQL ' + new Date().getTime(),
        tipo: 'Teste',
        preco: 15.99,
        quantidade: 50,
        dataVencimento: '2025-12-31'
    };
    
    try {
        const response = await fetch('http://localhost:8081/api/produtos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto)
        });
        
        if (response.ok) {
            const produtoSalvo = await response.json();
            console.log('✅ Produto cadastrado com sucesso!');
            console.log('- ID:', produtoSalvo.idProduto);
            console.log('- Nome:', produtoSalvo.nome);
            console.log('- Preço: R$', produtoSalvo.preco);
            console.log('🔍 Verifique no MySQL Workbench na tabela "produto"!');
            return true;
        } else {
            const error = await response.text();
            console.log('❌ Erro no cadastro:', error);
            return false;
        }
    } catch (error) {
        console.log('❌ Erro na requisição:', error.message);
        return false;
    }
}

// Executar testes
async function executarTestes() {
    console.log('🚀 Iniciando testes do sistema...\n');
    
    const conexaoOk = await testarConexao();
    
    if (conexaoOk) {
        await testarCadastroProduto();
    }
    
    console.log('\n✨ Testes concluídos!');
    console.log('💡 Abra o MySQL Workbench e verifique a tabela "produto" no banco "estoque_jb"');
}

// Executar se estiver no Node.js
if (typeof window === 'undefined') {
    // Node.js environment
    const fetch = require('node-fetch');
    executarTestes();
}