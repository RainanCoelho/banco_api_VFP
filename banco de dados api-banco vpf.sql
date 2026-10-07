CREATE TABLE clientes (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  cpf VARCHAR(11) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE contas (
  id SERIAL PRIMARY KEY,
  cliente_id INT NOT NULL REFERENCES clientes(id),
  numero VARCHAR(10) UNIQUE NOT NULL,
  tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('CORRENTE', 'POUPANCA')),
  saldo NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (saldo >= 0)
);

CREATE TABLE transacoes (
  id SERIAL PRIMARY KEY,
  conta_id INT NOT NULL REFERENCES contas(id),
  tipo VARCHAR(30) NOT NULL
    CHECK (tipo IN ('DEPOSITO', 'SAQUE', 'TRANSFERENCIA_ENVIADA', 'TRANSFERENCIA_RECEBIDA')),
  valor NUMERIC(12,2) NOT NULL CHECK (valor > 0),
  descricao VARCHAR(200),
  criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE OR REPLACE VIEW vw_relatorio_contas AS
SELECT 
    c.nome AS nome_cliente,
    c.cpf,
    ct.numero AS numero_conta,
    ct.tipo AS tipo_conta,
    ct.saldo
FROM contas ct
INNER JOIN clientes c ON ct.cliente_id = c.id;

//---------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION fn_consultar_saldo(p_conta_id BIGINT)
RETURNS NUMERIC AS $$
DECLARE
    v_saldo NUMERIC;
BEGIN
    SELECT saldo INTO v_saldo FROM contas WHERE id = p_conta_id;
    RETURN COALESCE(v_saldo, 0.00);
END;
$$ LANGUAGE plpgsql;

//--------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE sp_realizar_transferencia(
    p_origem_id INT,
    p_destino_id INT,
    p_valor NUMERIC(12,2)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_saldo_origem NUMERIC(12,2);
BEGIN
    -- Validação: contas iguais
    IF p_origem_id = p_destino_id THEN
        RAISE EXCEPTION 'A conta de origem e destino não podem ser iguais.';
    END IF;

    -- Validação: valor positivo
    IF p_valor <= 0 THEN
        RAISE EXCEPTION 'O valor da transferência deve ser maior que zero.';
    END IF;

    -- Validação: existência da conta de destino
    IF NOT EXISTS (SELECT 1 FROM contas WHERE id = p_destino_id) THEN
        RAISE EXCEPTION 'Conta de destino com ID % não encontrada.', p_destino_id;
    END IF;

    -- Bloqueia a conta de origem para leitura concorrente e verifica saldo
    SELECT saldo INTO v_saldo_origem
    FROM contas
    WHERE id = p_origem_id
    FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Conta de origem com ID % não encontrada.', p_origem_id;
    END IF;

    IF v_saldo_origem < p_valor THEN
        RAISE EXCEPTION 'Saldo insuficiente para realizar a transferência.';
    END IF;

    -- 1. Debitar da origem
    UPDATE contas 
    SET saldo = saldo - p_valor 
    WHERE id = p_origem_id;

    -- 2. Creditar no destino
    UPDATE contas 
    SET saldo = saldo + p_valor 
    WHERE id = p_destino_id;

    -- 3. Registrar movimentação na conta de origem
    INSERT INTO transacoes (conta_id, tipo, valor, descricao, criado_em)
    VALUES (p_origem_id, 'TRANSFERENCIA_ENVIADA', p_valor, 'Transferência enviada para a conta ID ' || p_destino_id, now());

    -- 4. Registrar movimentação na conta de destino
    INSERT INTO transacoes (conta_id, tipo, valor, descricao, criado_em)
    VALUES (p_destino_id, 'TRANSFERENCIA_RECEBIDA', p_valor, 'Transferência recebida da conta ID ' || p_origem_id, now());
END;
$$;


