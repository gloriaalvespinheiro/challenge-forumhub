package com.forumhub.apitopicos.controller;

import com.forumhub.apitopicos.resposta.DadosAtualizacaoResposta;
import com.forumhub.apitopicos.resposta.DadosCadastroResposta;
import com.forumhub.apitopicos.resposta.DadosDetalhamentoResposta;
import com.forumhub.apitopicos.resposta.Resposta;
import com.forumhub.apitopicos.resposta.RespostaRepository;
import com.forumhub.apitopicos.topico.TopicoRepository; // Para buscar o tópico
import com.forumhub.apitopicos.usuario.UsuarioRepository; // Para buscar o autor
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key") // Mapeia este controlador para o caminho /respostas
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository; // Repositório de Respostas

    @Autowired
    private TopicoRepository topicoRepository; // Repositório de Tópicos (para relacionamento)

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositório de Usuários (para relacionamento)

    @PostMapping // Endpoint para cadastrar uma nova resposta
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> cadastrar(@RequestBody @Valid DadosCadastroResposta dados, UriComponentsBuilder uriBuilder) {
        // Verifica se o tópico existe
        var topico = topicoRepository.findById(dados.topicoId()).orElse(null);
        if (topico == null) {
            // Retorna 404 Not Found se o tópico não existir
            return ResponseEntity.notFound().build();
        }

        // Verifica se o autor existe
        var autor = usuarioRepository.findById(dados.autorId()).orElse(null);
        if (autor == null) {
            // Retorna 404 Not Found se o autor não existir
            return ResponseEntity.notFound().build();
        }

        // Cria a entidade Resposta com os dados do DTO e a data de criação atual
        var resposta = new Resposta(null, dados.mensagem(), LocalDateTime.now(), topico, autor);
        respostaRepository.save(resposta); // Salva no banco de dados

        // Constrói a URI para o novo recurso criado (ex: /respostas/1)
        URI uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        // Retorna uma resposta 201 Created com a URI do novo recurso e o DTO de detalhamento
        return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));
    }

    @GetMapping // Endpoint para listar todas as respostas (com paginação)
    public Page<DadosDetalhamentoResposta> listar(@ParameterObject @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        // Retorna uma página de DTOs de detalhamento de respostas
        return respostaRepository.findAll(paginacao).map(DadosDetalhamentoResposta::new);
    }

    @GetMapping("/{id}") // Endpoint para detalhar uma resposta por ID
    public ResponseEntity<DadosDetalhamentoResposta> detalhar(@PathVariable Long id) {
        // Busca a resposta pelo ID. Se não encontrar, retorna 404 Not Found.
        Resposta resposta = respostaRepository.findById(id).orElse(null);

        if (resposta == null) {
            return ResponseEntity.notFound().build();
        }

        // Retorna 200 OK com os dados detalhados da resposta
        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }

    @PutMapping("/{id}") // NOVO: Endpoint para atualizar uma resposta
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoResposta dados) {
        // Busca a resposta pelo ID. Se não encontrar, retorna 404 Not Found.
        Resposta resposta = respostaRepository.findById(id).orElse(null);

        if (resposta == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza a mensagem da resposta usando o método na entidade
        resposta.atualizarMensagem(dados);

        // Retorna 200 OK com os dados detalhados da resposta atualizada
        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }

    @DeleteMapping("/{id}") // NOVO: Endpoint para excluir uma resposta
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        // Verifica se a resposta existe antes de tentar excluir
        if (!respostaRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna 404 se não existir
        }

        respostaRepository.deleteById(id); // Exclui a resposta
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para exclusão bem-sucedida
    }
}

    // TODO: Implementar os métodos de atualização (PUT) e exclusão (DELETE) para respostas

