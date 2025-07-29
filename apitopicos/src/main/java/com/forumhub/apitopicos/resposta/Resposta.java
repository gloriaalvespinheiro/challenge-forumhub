package com.forumhub.apitopicos.resposta;

import com.forumhub.apitopicos.topico.Topico; // Importe a entidade Topico
import com.forumhub.apitopicos.usuario.Usuario; // Importe a entidade Usuario
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Importe LocalDateTime

@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY) // Muitas respostas para um tópico
    @JoinColumn(name = "topico_id") // Coluna na tabela 'respostas' que referencia 'topicos'
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY) // Muitas respostas para um utilizador (autor)
    @JoinColumn(name = "autor_id") // Coluna na tabela 'respostas' que referencia 'usuarios'
    private Usuario autor;



    // NOVO: Método para atualizar a mensagem da resposta
    public void atualizarMensagem(DadosAtualizacaoResposta dados) {
        if (dados.mensagem() != null && !dados.mensagem().isBlank()) {
            this.mensagem = dados.mensagem();
        }
    }
}

    // Construtor para criar uma nova resposta a partir de um DTO de cadastro (a ser criado)
    // Este construtor será adicionado depois que criarmos o DTO de cadastro.

