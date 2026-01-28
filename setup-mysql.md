# 🗄️ Configuração MySQL para Sistema de Estoque

## ✅ Pré-requisitos

1. **MySQL Server** instalado e rodando
2. **MySQL Workbench** instalado
3. **Senha do usuário root** do MySQL

## 🔧 Configuração

### 1. Configurar senha no application.properties

Abra o arquivo `src/main/resources/application.properties` e ajuste a linha:

```properties
spring.datasource.password=SUA_SENHA_MYSQL_AQUI
```

Substitua `SUA_SENHA_MYSQL_AQUI` pela senha do seu usuário root do MySQL.

### 2. Criar o banco de dados (OPCIONAL)

O banco será criado automaticamente, mas você pode criar manualmente se preferir:

```sql
CREATE DATABASE estoque_jb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Iniciar a aplicação

```bash
mvn spring-boot:run
```

## 📊 Verificar no MySQL Workbench

1. Abra o **MySQL Workbench**
2. Conecte na sua **Local instance**
3. Você verá o banco `estoque_jb` com as tabelas:
   - `clientes`
   - `funcionarios` 
   - `produtos`
   - `vendas`
   - `itens_venda`
   - `estoque`
   - `compras`
   - `compras_produtos`

## 🔄 Alternar entre bancos

### Para usar H2 (desenvolvimento):
```bash
mvn spring-boot:run -Dspring.profiles.active=h2
```

### Para usar MySQL (produção):
```bash
mvn spring-boot:run
```

## 🚨 Problemas comuns

### Erro de conexão MySQL:
1. Verifique se o MySQL está rodando
2. Confirme a senha no application.properties
3. Verifique se a porta 3306 está disponível

### Erro de autenticação:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'sua_senha';
FLUSH PRIVILEGES;
```

## 📝 Logs

A aplicação mostra os comandos SQL executados no console para você acompanhar a criação das tabelas e inserção de dados.