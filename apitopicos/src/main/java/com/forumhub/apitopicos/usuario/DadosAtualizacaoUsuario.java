package com.forumhub.apitopicos.usuario;

public record DadosAtualizacaoUsuario(
        String login, // O login pode ser atualizado (opcional)
        String senha // A senha pode ser atualizada (opcional)
) {}
