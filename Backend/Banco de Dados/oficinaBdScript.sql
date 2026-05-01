DROP DATABASE IF EXISTS bdacvans;
CREATE DATABASE bdacvans;
USE bdacvans;

CREATE TABLE oficina(
	id_oficina INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN DEFAULT FALSE
);

CREATE TABLE usuario(
	id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    primeiro_login BOOLEAN NOT NULL DEFAULT TRUE,
    dois_fatores BOOLEAN NOT NULL DEFAULT TRUE,
    fk_oficina INT NULL,
    CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
);

CREATE TABLE role(
	id_role INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(30) NOT NULL UNIQUE
);
INSERT INTO role (nome) VALUES ('ADMIN'), ('FUNCIONARIO'), ('GERENTE');

CREATE TABLE usuario_role(
	fk_usuario BIGINT NOT NULL,
    fk_role INT NOT NULL,
    PRIMARY KEY (fk_usuario, fk_role),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (fk_role) REFERENCES role(id_role) ON DELETE CASCADE
);

CREATE TABLE auditoria(
	id_auditoria BIGINT PRIMARY KEY AUTO_INCREMENT,
    acao VARCHAR(50) NOT NULL,
    entidade VARCHAR(50),
    id_registro BIGINT,
    tempo DATETIME NOT NULL,
    endereco_ip VARCHAR(45),
    detalhes JSON,
    fk_usuario BIGINT NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
);

CREATE TABLE cliente (
	id_cliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    celular VARCHAR(14) NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
);

CREATE TABLE veiculo (
	id_veiculo BIGINT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(7) NOT NULL,
    marca VARCHAR(20) NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    fk_cliente BIGINT NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE,
    UNIQUE (placa, fk_oficina)
);

CREATE TABLE tipo_servico(
	id_tipo_servico BIGINT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(100) NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
);

CREATE TABLE etapa_servico (
    id_etapa_servico BIGINT PRIMARY KEY AUTO_INCREMENT,
    ordem INT NOT NULL DEFAULT 100,
    titulo VARCHAR(20) NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    fk_tipo_servico BIGINT NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_tipo_servico)
        REFERENCES tipo_servico (id_tipo_servico)
        ON DELETE CASCADE,
    FOREIGN KEY (fk_oficina)
        REFERENCES oficina (id_oficina)
        ON DELETE CASCADE,
    UNIQUE (fk_tipo_servico , ordem)
);

CREATE TABLE status_servico(
	id_status_servico INT PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(20) NOT NULL
);

INSERT INTO status_servico (descricao) VALUES ('AGENDADO'), ('INICIADO'), ('FINALIZADO');

CREATE TABLE Servico (
    id_servico BIGINT PRIMARY KEY AUTO_INCREMENT,
    receber_notificacao BOOLEAN DEFAULT TRUE,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME NULL,
    token_atualizacao VARCHAR(32) NOT NULL UNIQUE,
    token_consulta VARCHAR(6) NOT NULL UNIQUE,
    fk_veiculo BIGINT NOT NULL,
	fk_tipo_servico BIGINT NOT NULL,
    fk_status_servico INT NOT NULL,
    fk_etapa_servico BIGINT,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_veiculo) REFERENCES veiculo (id_veiculo) ON DELETE CASCADE,
    FOREIGN KEY (fk_tipo_servico) REFERENCES tipo_servico(id_tipo_servico) ON DELETE CASCADE,
    FOREIGN KEY (fk_status_servico) REFERENCES status_servico(id_status_servico) ON DELETE CASCADE,
    FOREIGN KEY (fk_etapa_servico) REFERENCES etapa_servico (id_etapa_servico) ON DELETE CASCADE,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
);

CREATE TABLE historico_etapa_servico (
    id_historico BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME NOT NULL,
    tempo_minutos INT NOT NULL,
    fk_servico BIGINT NOT NULL,
    fK_etapa_servico BIGINT NOT NULL,
    fk_oficina INT NOT NULL,
    FOREIGN KEY (fk_servico) REFERENCES servico(id_servico) ON DELETE CASCADE,
    FOREIGN KEY (fk_etapa_servico) REFERENCES etapa_servico(id_etapa_servico) ON DELETE CASCADE,
    FOREIGN KEY (fk_oficina) REFERENCES oficina(id_oficina) ON DELETE CASCADE
); 

/*Índices para todas as FK de oficina (garantindo performance de multi-tenancy)*/
CREATE INDEX idx_usuario_oficina ON usuario(fk_oficina);
CREATE INDEX idx_auditoria_oficina ON auditoria(fk_oficina);
CREATE INDEX idx_cliente_oficina ON cliente(fk_oficina);
CREATE INDEX idx_veiculo_oficina ON veiculo(fk_oficina);
CREATE INDEX idx_etapa_oficina ON etapa_servico(fk_oficina);
CREATE INDEX idx_tipo_servico_oficina ON tipo_servico(fk_oficina);
CREATE INDEX idx_servico_oficina ON servico(fk_oficina);
CREATE INDEX idx_historico_servico_oficina ON historico_etapa_servico(fk_oficina);

/*Índices para a tabela de auditoria*/
CREATE INDEX idx_auditoria_usuario ON auditoria(fk_usuario);
CREATE INDEX idx_auditoria_tempo ON auditoria(tempo);
CREATE INDEX idx_auditoria_entidade_registro ON auditoria(entidade, id_registro);

/*Índices para a tabela de cliente*/
CREATE INDEX idx_cliente_nome ON cliente(nome);

/*Índices para a tabela de veículo*/
CREATE INDEX idx_veiculo_cliente ON veiculo(fk_cliente);

/*Índices para a tabela de serviço*/
CREATE INDEX idx_servico_veiculo ON servico(fk_veiculo);
CREATE INDEX idx_servico_tipo ON servico(fk_tipo_servico);
CREATE INDEX idx_servico_etapa ON servico(fk_etapa_servico);
CREATE INDEX idx_servico_data_inicio ON servico(data_inicio);
CREATE INDEX idx_servico_data_fim ON servico(data_fim);
CREATE INDEX idx_servico_token_atualizacao ON servico(token_atualizacao);
CREATE INDEX idx_servico_token_consulta ON servico(token_consulta);

/*Índices para a tabela de histórico de etapas*/
CREATE INDEX idx_hist_servico ON historico_etapa_servico(fk_servico);
CREATE INDEX idx_hist_etapa ON historico_etapa_servico(fk_etapa_servico);

CREATE PROCEDURE valida_celular(INOUT p_celular VARCHAR(20))
BEGIN
  -- Remove tudo que não for número
  SET p_celular = REGEXP_REPLACE(p_celular, '[^0-9]', '');

  -- Validação: DDD + 9 + 8 dígitos
  IF p_celular NOT REGEXP '^[1-9]{2}9[0-9]{8}$' THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Número de celular inválido';
  END IF;

  -- Formatação: (XX) 9XXXXXXXX
  SET p_celular = CONCAT(
    '(',
    SUBSTRING(p_celular, 1, 2),
    ') ',
    SUBSTRING(p_celular, 3)
  );
END;

CREATE TRIGGER trg_celular_bi
BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN
  CALL valida_celular(NEW.celular);
END;

CREATE TRIGGER trg_celular_bu
BEFORE UPDATE ON cliente
FOR EACH ROW
BEGIN
		CALL valida_celular(NEW.celular);
END;

CREATE TRIGGER trg_upper_placa
BEFORE INSERT ON Veiculo
FOR EACH ROW
BEGIN
  SET NEW.placa = UPPER(NEW.placa);
END;

ALTER TABLE Veiculo ADD CONSTRAINT chk_placa
	CHECK (
    placa REGEXP '^[A-Z]{3}[0-9][A-Z][0-9]{2}$'
    OR
	placa REGEXP '^[A-Z]{3}[0-9]{4}$'
);