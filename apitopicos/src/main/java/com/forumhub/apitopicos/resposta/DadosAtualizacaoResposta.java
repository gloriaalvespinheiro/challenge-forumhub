package com.forumhub.apitopicos.resposta;

import jakarta.validation.constraints.NotBlank; // Importe NotBlank

// Record para dados de atualização de uma resposta
public record DadosAtualizacaoResposta(
        @NotBlank // A mensagem é o campo principal a ser atualizado
        String mensagem
) {}
