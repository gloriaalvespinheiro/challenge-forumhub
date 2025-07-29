package com.forumhub.apitopicos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping // Mapeia o método para requisições GET em /test
    public ResponseEntity<String> helloWorld() {
        // Este endpoint só será acessível se o usuario estiver autenticado com um token JWT válido
        return ResponseEntity.ok("Olá, mundo! Você está autenticado e acessou um recurso protegido!");
    }
}
