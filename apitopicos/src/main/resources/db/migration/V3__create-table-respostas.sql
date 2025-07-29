CREATE TABLE respostas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensagem VARCHAR(500) NOT NULL,
    data_criacao DATETIME NOT NULL,
    topico_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT fk_respostas_topico_id FOREIGN KEY(topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respostas_autor_id FOREIGN KEY(autor_id) REFERENCES usuarios(id)
);