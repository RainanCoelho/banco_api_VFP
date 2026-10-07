# 🏦 Sistema Bancário Transacional com PostgreSQL

## 👤 Identificação
* **Integrantes do Grupo:** Rainan Araújo Coelho
* **Disciplina:** Projeto de Banco de Dados
* **Professor:** Anderson Soares

---

## 📖 Sobre o Projeto
A aplicação consiste em uma API REST desenvolvida em Java com Spring Boot integrada a um banco de dados relacional PostgreSQL. O objetivo principal é simular o núcleo de um sistema bancário transacional, resolvendo problemas de concorrência, consistência e integridade de saldos bancários ao delegar o processamento de regras financeiras críticas diretamente aos recursos nativos do SGBD[cite: 1]. A aplicação implementa cadastros, emissão de relatórios de correntistas, consulta otimizada de saldo e transferências de valores de maneira atômica e segura[cite: 1].

---

## 🛠️ Tecnologias Utilizadas
* **Linguagem:** Java 17+
* **Framework:** Spring Boot (Spring Web, Spring Data JPA, Validation)
* **Banco de Dados:** PostgreSQL[cite: 1]
* **Bibliotecas Auxiliares:** Lombok
* **Ferramenta de Testes e Requisições:** HTTPie

---

## 🗄️ Banco de Dados

* **SGBD Utilizado:** PostgreSQL[cite: 1]

### Principais Tabelas
* **`clientes`:** Armazena os dados cadastrais dos clientes (`id`, `nome`, `cpf`, `email`)[cite: 1].
* **`contas`:** Registra as contas vinculadas aos clientes, com tipo (`CORRENTE` ou `POUPANCA`) e saldo com restrição de valor não negativo `CHECK (saldo >= 0)`[cite: 1].
* **`transacoes`:** Registra o histórico e extrato contábil das movimentações financeiras (`conta_id`, `tipo`, `valor`, `descricao`, `criado_em`)[cite: 1].

### View Criada
* **`vw_relatorio_contas`:** Realiza um `INNER JOIN` entre as tabelas `contas` e `clientes`, consolidando em uma visão virtual o nome e CPF do titular, número da conta, tipo de conta e saldo atual[cite: 1]. Na aplicação Spring Boot, é consumida pelo endpoint `GET /relatorios/contas` mapeada via interface de projeção `RelatorioContaProjection`[cite: 1].

### Function Criada
* **`fn_consultar_saldo(p_conta_id BIGINT)`:** Função escrita em PL/pgSQL que recebe o identificador da conta e retorna pontualmente um valor numérico (`NUMERIC` / `BigDecimal`) com o saldo atual da conta, tratando valores nulos via `COALESCE`[cite: 1]. Na aplicação, é exposta pelo endpoint `GET /contas/{id}/saldo`.

### Procedure Criada
* **`sp_realizar_transferencia(p_origem_id, p_destino_id, p_valor)`:** Stored Procedure responsável por orquestrar a transferência entre duas contas[cite: 1]. Executa validações de contas e saldo, realiza o bloqueio de linha concorrente via `SELECT ... FOR UPDATE`, efetua o débito na origem, o crédito no destino e grava os dois registros de auditoria em partidas dobradas (`TRANSFERENCIA_ENVIADA` e `TRANSFERENCIA_RECEBIDA`) na tabela `transacoes`[cite: 1]. Na aplicação, é acionada pelo endpoint `POST /transacoes`.
---
Vídeo: https://drive.google.com/drive/folders/1zjdg2EPOQh_LWOYQqFCkM6u5uJpY8yCK?usp=drive_link
---
## 🚀 Como Executar

### 1. Configuração do Banco de Dados
1. Certifique-se de que o **PostgreSQL** está instalado e em execução[cite: 1].
2. Crie uma base de dados (exemplo: `banco_db`).
3. Execute o script SQL de criação das tabelas base (`clientes`, `contas`, `transacoes`), da View `vw_relatorio_contas`, da Function `fn_consultar_saldo` e da Procedure `sp_realizar_transferencia`[cite: 1].

### 2. Configuração da Aplicação
No arquivo `src/main/resources/application.properties` (ou `application.yml`), configure as credenciais de acesso ao seu banco de dados local:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banco_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true


