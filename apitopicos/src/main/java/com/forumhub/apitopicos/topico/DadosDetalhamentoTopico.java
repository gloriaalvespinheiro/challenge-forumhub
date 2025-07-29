package com.forumhub.apitopicos.topico;


import java.time.LocalDateTime;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String status, // Corresponde a estado_topico no banco
        String autor,
        String curso
        // Futuramente, você pode adicionar List<DadosDetalhamentoResposta> respostas aqui
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao(), topico.getStatus(), topico.getAutor(), topico.getCurso());
    }
}
