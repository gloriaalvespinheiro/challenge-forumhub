package com.forumhub.apitopicos.controller;

import com.forumhub.apitopicos.topico.*;
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

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional // Importante para operações de escrita no banco de dados
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
        // Regra de Negócio: Não permitir tópicos duplicados
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest().body("Já existe um tópico com este título e mensagem.");
        }

        var topico = new Topico(dados); // Cria a entidade a partir do DTO
        repository.save(topico); // Persiste os dados no banco

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico)); // Alterado para DadosDetalhamentoTopico
    }

    @GetMapping
    public Page<DadosListagemTopico> listar(@ParameterObject @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemTopico::new);
    }

    @GetMapping("/{id}") // Mapeia para /topicos/{id}
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        // Busca o tópico pelo ID
        Topico topico = repository.findById(id)
                .orElse(null); // Retorna null se não encontrar

        // Verifica se o tópico foi encontrado
        if (topico == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se não encontrar
        }

        // Retorna 200 OK com o DTO do tópico encontrado
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}") // Novo endpoint PUT para atualização
    @Transactional // Garante que as alterações sejam salvas no banco de dados
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
        // Verifica se o tópico existe
        Topico topico = repository.findById(id).orElse(null);

        if (topico == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o tópico não for encontrado
        }

        // Regra de Negócio: Verificar duplicidade apenas se título ou mensagem forem alterados
        if (dados.titulo() != null || dados.mensagem() != null) {
            // Se o título OU a mensagem foram alterados E já existe um tópico com o novo título E nova mensagem
            // que NÃO seja o próprio tópico que está sendo atualizado (para evitar falso positivo)
            if (repository.existsByTituloAndMensagem(
                    dados.titulo() != null ? dados.titulo() : topico.getTitulo(), // Usa o novo título ou o antigo
                    dados.mensagem() != null ? dados.mensagem() : topico.getMensagem() // Usa a nova mensagem ou a antiga
            ) && !(topico.getTitulo().equals(dados.titulo()) && topico.getMensagem().equals(dados.mensagem()))) {
                return ResponseEntity.badRequest().body("Já existe um tópico com este título e mensagem.");
            }
        }

        // Aplica as atualizações na entidade
        topico.atualizarInformacoes(dados);

        // Retorna 200 OK com o DTO do tópico atualizado
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}") // Novo endpoint DELETE para exclusão
    @Transactional // Garante que a operação de exclusão seja transacional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        // Verifica se o tópico existe antes de tentar excluir
        if (!repository.existsById(id)) { // Usamos existsById para uma verificação mais eficiente
            return ResponseEntity.notFound().build(); // Retorna 404 se o tópico não for encontrado
        }

        repository.deleteById(id); // Exclui o tópico pelo ID

        // Retorna 204 No Content para indicar sucesso na exclusão sem corpo de resposta
        return ResponseEntity.noContent().build();
    }
}
