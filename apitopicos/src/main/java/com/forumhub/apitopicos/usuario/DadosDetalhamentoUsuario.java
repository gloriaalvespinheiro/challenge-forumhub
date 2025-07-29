package com.forumhub.apitopicos.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login // Apenas o login (não a senha)
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin());
    }
}
