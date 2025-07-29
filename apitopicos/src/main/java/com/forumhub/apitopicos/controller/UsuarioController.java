package com.forumhub.apitopicos.controller;

import com.forumhub.apitopicos.usuario.DadosCadastroUsuario;
import com.forumhub.apitopicos.usuario.DadosDetalhamentoUsuario;
import com.forumhub.apitopicos.usuario.DadosListagemUsuario;
import com.forumhub.apitopicos.usuario.DadosAtualizacaoUsuario; // Importe este DTO, se já o tiver criado
import com.forumhub.apitopicos.usuario.Usuario;
import com.forumhub.apitopicos.usuario.UsuarioRepository;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        if (repository.findByLogin(dados.login()) != null) {
            return ResponseEntity.badRequest().body("Já existe um usuário com este login.");
        }

        var usuario = new Usuario(dados);
        repository.save(usuario);

        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping // Mapeia requisições GET para /usuarios
    @SecurityRequirement(name = "bearer-key")
    public Page<DadosListagemUsuario> listar(@ParameterObject @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemUsuario::new);
    }

    @GetMapping("/{id}") // NOVO: Endpoint para detalhar um usuário por ID
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id) {
        Usuario usuario = repository.findById(id).orElse(null);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @PutMapping("/{id}") // NOVO: Endpoint para atualizar um utilizador
    @SecurityRequirement(name = "bearer-key")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dados) {
        // Busca o utilizador pelo ID. Se não encontrar, retorna 404 Not Found.
        Usuario usuario = repository.findById(id).orElse(null);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza as informações do utilizador usando o método na entidade
        usuario.atualizarInformacoes(dados);

        // Retorna 200 OK com os dados detalhados do utilizador atualizado
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @DeleteMapping("/{id}") // NOVO: Endpoint para excluir um utilizador
    @SecurityRequirement(name = "bearer-key")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // Verifica se o utilizador existe antes de tentar excluir
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna 404 se não existir
        }

        repository.deleteById(id); // Exclui o utilizador
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para exclusão bem-sucedida
    }
}

    // TODO: Implementar os métodos de detalhamento (GET by ID), atualização (PUT) e exclusão (DELETE) para usuários
    // Assim como você fez no TopicoController, você pode adicionar:
    // @GetMapping("/{id}") para detalhar um usuário específico
    // @PutMapping("/{id}") para atualizar um usuário
    // @DeleteMapping("/{id}") para excluir um usuário

