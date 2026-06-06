-- ═══════════════════════════════════════════════════════════
-- SISTEMA DE GESTÃO DE FRETES — Script completo e limpo
-- Execute no banco: sistema_gestao_frete
-- ═══════════════════════════════════════════════════════════

-- ── 1. LIMPAR TABELAS (ordem importa por causa das FKs) ────
DROP TABLE IF EXISTS solicitacao_frete  CASCADE;
DROP TABLE IF EXISTS ocorrencia_frete   CASCADE;
DROP TABLE IF EXISTS frete              CASCADE;
DROP TABLE IF EXISTS usuario            CASCADE;
DROP TABLE IF EXISTS veiculo            CASCADE;
DROP TABLE IF EXISTS motorista          CASCADE;
DROP TABLE IF EXISTS cliente            CASCADE;

-- ── 2. CRIAR TABELAS ───────────────────────────────────────

CREATE TABLE cliente (
    id                SERIAL PRIMARY KEY,
    razao_social      VARCHAR(150) NOT NULL,
    nome_fantasia     VARCHAR(150),
    cnpj              VARCHAR(18)  NOT NULL UNIQUE,
    inscricao_estadual VARCHAR(30),
    tipo              VARCHAR(20)  NOT NULL,
    logradouro        VARCHAR(150),
    numero            VARCHAR(20),
    complemento       VARCHAR(100),
    bairro            VARCHAR(100),
    cidade            VARCHAR(100),
    uf                CHAR(2),
    cep               VARCHAR(9),
    telefone          VARCHAR(20),
    email             VARCHAR(150),
    status            VARCHAR(20)  NOT NULL
);

CREATE TABLE motorista (
    id               SERIAL PRIMARY KEY,
    nome             VARCHAR(150) NOT NULL,
    cpf              VARCHAR(14)  NOT NULL UNIQUE,
    data_nascimento  DATE         NOT NULL,
    telefone         VARCHAR(20),
    cnh_numero       VARCHAR(30)  NOT NULL UNIQUE,
    cnh_categoria    VARCHAR(2)   NOT NULL,
    cnh_validade     DATE         NOT NULL,
    tipo_vinculo     VARCHAR(20)  NOT NULL,
    status           VARCHAR(20)  NOT NULL
);

CREATE TABLE veiculo (
    id              SERIAL PRIMARY KEY,
    placa           VARCHAR(10)    NOT NULL UNIQUE,
    rntrc           VARCHAR(20)    NOT NULL UNIQUE,
    ano_fabricacao  INTEGER        NOT NULL,
    tipo            VARCHAR(20)    NOT NULL,
    tara_kg         NUMERIC(10,2)  NOT NULL,
    capacidade_kg   NUMERIC(10,2)  NOT NULL,
    volume_m3       NUMERIC(10,2)  NOT NULL,
    status          VARCHAR(20)    NOT NULL
);

CREATE TABLE usuario (
    id         SERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    login      VARCHAR(50)  NOT NULL UNIQUE,
    senha      VARCHAR(100) NOT NULL,
    perfil     VARCHAR(20)  NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    cliente_id INTEGER REFERENCES cliente(id)
);

CREATE TABLE frete (
    id                    SERIAL PRIMARY KEY,
    numero                VARCHAR(20)    NOT NULL UNIQUE,
    id_remetente          INTEGER        NOT NULL REFERENCES cliente(id),
    id_destinatario       INTEGER        NOT NULL REFERENCES cliente(id),
    id_motorista          INTEGER        NOT NULL REFERENCES motorista(id),
    id_veiculo            INTEGER        NOT NULL REFERENCES veiculo(id),
    cidade_origem         VARCHAR(100)   NOT NULL,
    uf_origem             CHAR(2)        NOT NULL,
    cidade_destino        VARCHAR(100)   NOT NULL,
    uf_destino            CHAR(2)        NOT NULL,
    descricao_carga       VARCHAR(255)   NOT NULL,
    peso_kg               NUMERIC(10,2)  NOT NULL,
    volumes               INTEGER        NOT NULL,
    valor_frete           NUMERIC(12,2)  NOT NULL,
    aliquota_icms         NUMERIC(5,2)   NOT NULL,
    valor_icms            NUMERIC(12,2)  NOT NULL,
    valor_total           NUMERIC(12,2)  NOT NULL,
    status                VARCHAR(30)    NOT NULL,
    data_emissao          DATE           NOT NULL,
    data_previsao_entrega DATE           NOT NULL,
    data_saida            TIMESTAMP,
    data_entrega          TIMESTAMP
);

CREATE TABLE ocorrencia_frete (
    id                  SERIAL PRIMARY KEY,
    id_frete            INTEGER       NOT NULL REFERENCES frete(id),
    tipo                VARCHAR(30)   NOT NULL,
    data_hora           TIMESTAMP     NOT NULL,
    cidade              VARCHAR(100)  NOT NULL,
    uf                  CHAR(2)       NOT NULL,
    descricao           VARCHAR(255),
    nome_recebedor      VARCHAR(150),
    documento_recebedor VARCHAR(30)
);

CREATE TABLE solicitacao_frete (
    id                  SERIAL PRIMARY KEY,
    id_cliente          INTEGER        NOT NULL REFERENCES cliente(id),
    cidade_origem       VARCHAR(100)   NOT NULL,
    uf_origem           CHAR(2)        NOT NULL,
    cidade_destino      VARCHAR(100)   NOT NULL,
    uf_destino          CHAR(2)        NOT NULL,
    descricao_carga     VARCHAR(255)   NOT NULL,
    peso_kg             NUMERIC(10,2)  NOT NULL,
    volumes             INTEGER        NOT NULL,
    observacao          VARCHAR(500),
    status              VARCHAR(20)    NOT NULL,
    data_solicitacao    TIMESTAMP      NOT NULL DEFAULT NOW(),
    data_analise        TIMESTAMP,
    usuario_analise_id  INTEGER REFERENCES usuario(id),
    motivo_recusa       VARCHAR(500)
);

-- ── 3. DADOS DE EXEMPLO ────────────────────────────────────

INSERT INTO cliente (razao_social, nome_fantasia, cnpj, inscricao_estadual, tipo,
    logradouro, numero, complemento, bairro, cidade, uf, cep,
    telefone, email, status)
VALUES
('Comercial Recife LTDA',    'Comercial Recife',     '11.444.777/0001-61', '123456789', 'AMBOS',
 'Av. Recife',      '100', 'Sala 01',  'Boa Viagem',  'Recife',                 'PE', '51020-000', '(81)3333-1111', 'contato@comercialrecife.com', 'ATIVO'),
('Distribuidora Jaboatao SA','Distribuidora Jaboatao','45.723.174/0001-10', '987654321', 'DESTINATARIO',
 'Rua das Flores',  '250', NULL,       'Centro',       'Jaboatao dos Guararapes','PE', '54010-120', '(81)3333-2222', 'fiscal@djaboatao.com',        'ATIVO'),
('Atacado Nordeste ME',      'Atacado Nordeste',      '18.236.120/0001-58', '456123789', 'REMETENTE',
 'Av. Agamenon',    '500', 'Galpao B', 'Espinheiro',   'Recife',                 'PE', '52020-120', '(81)3333-3333', 'compras@atacadonordeste.com',  'ATIVO');

INSERT INTO motorista (nome, cpf, data_nascimento, telefone,
    cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, status)
VALUES
('Carlos Silva', '529.982.247-25', '1985-03-10', '(81)98888-1001', 'CNH12345601', 'E', '2028-06-30', 'FUNCIONARIO', 'ATIVO'),
('Marcos Souza', '111.444.777-35', '1990-08-15', '(81)98888-1002', 'CNH12345602', 'D', '2027-11-15', 'AGREGADO',    'ATIVO'),
('Joao Pereira', '935.411.347-80', '1982-12-01', '(81)98888-1003', 'CNH12345603', 'C', '2026-12-20', 'TERCEIRO',    'SUSPENSO');

INSERT INTO veiculo (placa, rntrc, ano_fabricacao, tipo,
    tara_kg, capacidade_kg, volume_m3, status)
VALUES
('ABC1D23', '12345678', 2019, 'TRUCK',   8500.00, 14000.00, 42.50, 'DISPONIVEL'),
('PEX2A45', '22334455', 2020, 'CARRETA', 12000.00,30000.00, 65.00, 'EM_VIAGEM'),
('KLM9B87', '99887766', 2021, 'VAN',     2500.00,  1800.00, 12.00, 'EM_MANUTENCAO');

-- usuário admin — senha: admin123
-- hash SHA-256 gerado pelo HashUtil do sistema
INSERT INTO usuario (nome, login, senha, perfil, status)
VALUES ('Administrador', 'admin',
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
        'ADMIN', 'ATIVO');

INSERT INTO frete (numero, id_remetente, id_destinatario, id_motorista, id_veiculo,
    cidade_origem, uf_origem, cidade_destino, uf_destino,
    descricao_carga, peso_kg, volumes, valor_frete, aliquota_icms,
    valor_icms, valor_total, status, data_emissao, data_previsao_entrega,
    data_saida, data_entrega)
VALUES
('FRT-2026-00001', 1, 2, 1, 1, 'Recife','PE','Caruaru','PE',
 'Alimentos', 3500.00, 120, 1800.00, 12.00, 216.00, 2016.00,
 'EMITIDO', '2026-03-15', '2026-03-18', NULL, NULL),

('FRT-2026-00002', 3, 2, 2, 2, 'Recife','PE','Maceio','AL',
 'Bebidas', 9000.00, 200, 3200.00, 12.00, 384.00, 3584.00,
 'SAIDA_CONFIRMADA', '2026-03-14', '2026-03-17', '2026-03-14 08:30:00', NULL),

('FRT-2026-00003', 1, 3, 2, 2, 'Recife','PE','Joao Pessoa','PB',
 'Material de limpeza', 8500.00, 180, 2800.00, 12.00, 336.00, 3136.00,
 'EM_TRANSITO', '2026-03-13', '2026-03-16', '2026-03-13 07:45:00', NULL),

('FRT-2026-00004', 3, 1, 1, 1, 'Recife','PE','Paulista','PE',
 'Higiene pessoal', 1200.00, 60, 700.00, 12.00, 84.00, 784.00,
 'ENTREGUE', '2026-03-10', '2026-03-11', '2026-03-10 09:00:00', '2026-03-10 15:10:00'),

('FRT-2026-00005', 2, 3, 1, 1, 'Jaboatao dos Guararapes','PE','Olinda','PE',
 'Eletronicos', 600.00, 25, 500.00, 12.00, 60.00, 560.00,
 'CANCELADO', '2026-03-12', '2026-03-13', NULL, NULL);

INSERT INTO ocorrencia_frete (id_frete, tipo, data_hora, cidade, uf,
    descricao, nome_recebedor, documento_recebedor)
VALUES
(2, 'SAIDA_DO_PATIO',    '2026-03-14 08:30:00', 'Recife',   'PE', 'Saida confirmada do patio',          NULL,           NULL),
(3, 'EM_ROTA',           '2026-03-13 10:15:00', 'Palmares', 'PE', 'Veiculo em rota para destino',        NULL,           NULL),
(4, 'ENTREGA_REALIZADA', '2026-03-10 15:10:00', 'Paulista', 'PE', 'Entrega concluida sem divergencias',  'Maria Oliveira','12345678900'),
(3, 'OUTROS',            '2026-03-13 13:40:00', 'Carpina',  'PE', 'Parada para conferencia da carga',    NULL,           NULL),
(5, 'OUTROS',            '2026-03-12 16:00:00', 'Jaboatao dos Guararapes','PE','Frete cancelado antes da saida', NULL, NULL);

INSERT INTO solicitacao_frete (id_cliente, cidade_origem, uf_origem, cidade_destino, uf_destino,
    descricao_carga, peso_kg, volumes, observacao, status)
VALUES
(1, 'Recife', 'PE', 'Fortaleza', 'CE', 'Produtos alimentícios', 2000.00, 80, NULL, 'PENDENTE'),
(2, 'Jaboatao dos Guararapes', 'PE', 'Natal', 'RN', 'Material de escritório', 500.00, 30, 'Entrega urgente', 'APROVADA');

-- ── 4. VERIFICAÇÃO FINAL ───────────────────────────────────
SELECT 'clientes'        AS tabela, COUNT(*) AS total FROM cliente
UNION ALL
SELECT 'motoristas',      COUNT(*) FROM motorista
UNION ALL
SELECT 'veiculos',        COUNT(*) FROM veiculo
UNION ALL
SELECT 'usuarios',        COUNT(*) FROM usuario
UNION ALL
SELECT 'fretes',          COUNT(*) FROM frete
UNION ALL
SELECT 'ocorrencias',     COUNT(*) FROM ocorrencia_frete
UNION ALL
SELECT 'solicitacoes',    COUNT(*) FROM solicitacao_frete;
