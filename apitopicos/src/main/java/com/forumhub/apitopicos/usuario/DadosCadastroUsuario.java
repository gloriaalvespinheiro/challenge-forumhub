package com.forumhub.apitopicos.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroUsuario(
        @NotBlank // Garante que o campo não seja nulo ou vazio
        String login, // O login do usuário (que é o que você tem na entidade)
        @NotBlank
        @Pattern(regexp = "\\d{6,10}") // Exemplo: senha de 6 a 10 dígitos. Ajuste conforme sua política de senha.
        String senha
) {}
