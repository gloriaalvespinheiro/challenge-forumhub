package com.forumhub.apitopicos.controller;


import com.forumhub.apitopicos.usuario.DadosAutenticacao; // Ajuste este pacote
import com.forumhub.apitopicos.usuario.DadosTokenJWT; // Ajuste este pacote
import com.forumhub.apitopicos.usuario.Usuario; // Ajuste este pacote
import com.forumhub.apitopicos.infra.security.TokenService; // Ajuste este pacote
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager; // Injetado pelo Spring Security

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // Cria um objeto de autenticação com login e senha
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        // Autentica o usuário
        var authentication = manager.authenticate(authenticationToken);

        // Gera o token JWT
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}