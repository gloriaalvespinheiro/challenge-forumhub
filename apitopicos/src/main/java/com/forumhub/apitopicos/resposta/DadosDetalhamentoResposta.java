package com.forumhub.apitopicos.resposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        Long topicoId,
        Long autorId,
        String autorLogin // Para facilitar a visualização do autor
) {
    public DadosDetalhamentoResposta(Resposta resposta) {
        this(resposta.getId(), resposta.getMensagem(), resposta.getDataCriacao(),
                resposta.getTopico().getId(), resposta.getAutor().getId(), resposta.getAutor().getLogin());
    }
}
