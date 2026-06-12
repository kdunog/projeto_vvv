-- =====================================
-- CIDADE
-- =====================================
CREATE TABLE Cidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL
);

-- =====================================
-- PASSAGEIRO
-- =====================================
CREATE TABLE Passageiro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(255) NOT NULL,
    idade INT
);

-- =====================================
-- TRANSPORTADORA
-- =====================================
CREATE TABLE Transportadora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE
);

-- =====================================
-- MODAL
-- =====================================
CREATE TABLE Modal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_da_transportadora INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    capacidade INT NOT NULL,
    ultima_manutencao DATE,

    FOREIGN KEY (id_da_transportadora)
        REFERENCES Transportadora(id)
);

-- =====================================
-- ENDERECO
-- =====================================
CREATE TABLE Endereco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_da_cidade INT NOT NULL,
    logradouro VARCHAR(150) NOT NULL,
    numero INT NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cep VARCHAR(10),

    FOREIGN KEY (id_da_cidade)
        REFERENCES Cidade(id)
);

-- =====================================
-- PONTO DE VENDA
-- =====================================
CREATE TABLE PontoVenda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_endereco INT NOT NULL,
    telefone VARCHAR(20),
    cnpj VARCHAR(18),

    FOREIGN KEY (id_endereco)
        REFERENCES Endereco(id)
);

-- =====================================
-- FUNCIONARIO
-- =====================================
CREATE TABLE Funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_do_ponto_venda INT NOT NULL,
    id_endereco_residencia INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(255),
    cargo VARCHAR(50),

    FOREIGN KEY (id_do_ponto_venda)
        REFERENCES PontoVenda(id),

    FOREIGN KEY (id_endereco_residencia)
        REFERENCES Endereco(id)
);

-- =====================================
-- RESERVA
-- =====================================
CREATE TABLE Reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_do_passageiro INT NOT NULL,
    id_da_cidade_origem INT NOT NULL,
    id_da_cidade_destino INT NOT NULL,
    id_do_modal INT NOT NULL,
    data DATE NOT NULL,
    status VARCHAR(50),

    FOREIGN KEY (id_do_passageiro)
        REFERENCES Passageiro(id),

    FOREIGN KEY (id_da_cidade_origem)
        REFERENCES Cidade(id),

    FOREIGN KEY (id_da_cidade_destino)
        REFERENCES Cidade(id),

    FOREIGN KEY (id_do_modal)
        REFERENCES Modal(id)
);

-- =====================================
-- TICKET
-- =====================================
CREATE TABLE Ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_da_reserva INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (id_da_reserva)
        REFERENCES Reserva(id)
);

-- =====================================
-- PAGAMENTO
-- =====================================
CREATE TABLE Pagamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_da_reserva INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10,2) DEFAULT 0,
    juros DECIMAL(10,2) DEFAULT 0,
    valor_final DECIMAL(10,2) NOT NULL,
    status VARCHAR(50),

    FOREIGN KEY (id_da_reserva)
        REFERENCES Reserva(id)
);

-- =====================================
-- BANCO
-- =====================================
CREATE TABLE Banco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- =====================================
-- OPERADORA
-- =====================================
CREATE TABLE Operadora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- =====================================
-- PIX
-- =====================================
CREATE TABLE Pix (
    id INT PRIMARY KEY,
    id_do_pagamento INT NOT NULL,
    id_do_banco INT NOT NULL,

    FOREIGN KEY (id)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_do_pagamento)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_do_banco)
        REFERENCES Banco(id)
);

-- =====================================
-- BOLETO
-- =====================================
CREATE TABLE Boleto (
    id INT PRIMARY KEY,
    id_do_pagamento INT NOT NULL,
    id_do_banco INT NOT NULL,

    FOREIGN KEY (id)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_do_pagamento)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_do_banco)
        REFERENCES Banco(id)
);

-- =====================================
-- CARTAO
-- =====================================
CREATE TABLE Cartao (
    id INT PRIMARY KEY,
    id_do_pagamento INT NOT NULL,
    id_da_operadora INT NOT NULL,

    FOREIGN KEY (id)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_do_pagamento)
        REFERENCES Pagamento(id),

    FOREIGN KEY (id_da_operadora)
        REFERENCES Operadora(id)
);

-- =====================================
-- DEBITO
-- =====================================
CREATE TABLE Debito (
    id INT PRIMARY KEY,
    id_do_cartao INT NOT NULL,

    FOREIGN KEY (id)
        REFERENCES Cartao(id),

    FOREIGN KEY (id_do_cartao)
        REFERENCES Cartao(id)
);

-- =====================================
-- CREDITO
-- =====================================
CREATE TABLE Credito (
    id INT PRIMARY KEY,
    id_do_cartao INT NOT NULL,
    numero_de_parcelas INT NOT NULL,

    FOREIGN KEY (id)
        REFERENCES Cartao(id),

    FOREIGN KEY (id_do_cartao)
        REFERENCES Cartao(id)
);