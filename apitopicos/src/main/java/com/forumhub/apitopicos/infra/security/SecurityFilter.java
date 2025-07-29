package com.forumhub.apitopicos.infra.security;

import com.forumhub.apitopicos.usuario.UsuarioRepository; // Ajuste este pacote
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("### DEBUG SECURITY FILTER - INÍCIO ###");
        System.out.println("DEBUG: URL da Requisição: " + request.getRequestURI());
        System.out.println("DEBUG: Método HTTP: " + request.getMethod());
        System.out.println("DEBUG: Headers de Autorização: " + request.getHeader("Authorization"));

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            System.out.println("DEBUG: Token JWT recuperado: Sim");
            try {
                var subject = tokenService.getSubject(tokenJWT);
                System.out.println("DEBUG: Subject (login) do token: " + subject);

                var usuario = usuarioRepository.findByLogin(subject);

                if (usuario == null) {
                    System.err.println("ERRO: Usuário não encontrado para o subject: " + subject);
                } else {
                    System.out.println("DEBUG: Usuário encontrado: " + usuario.getUsername()); // Ou usuario.getUsername()
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("DEBUG: Autenticação definida no SecurityContextHolder.");
                }

            } catch (Exception e) {
                System.err.println("ERRO no SecurityFilter ao processar token: " + e.getMessage());
            }
        } else {
            System.out.println("DEBUG: Token JWT recuperado: Não (ou nulo)");
        }

        // NOVO LOG: Verifica o estado da autenticação ANTES de passar para o próximo filtro
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            System.out.println("DEBUG: Usuário está autenticado no SecurityContextHolder.");
        } else {
            System.out.println("DEBUG: Usuário NÃO está autenticado no SecurityContextHolder.");
        }

        System.out.println("### DEBUG SECURITY FILTER - FIM ###");
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}