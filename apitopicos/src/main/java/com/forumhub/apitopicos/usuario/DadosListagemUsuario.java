package com.forumhub.apitopicos.usuario;

public record DadosListagemUsuario(
        Long id,
        String login // Apenas o login (não a senha)
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin());
    }
}
