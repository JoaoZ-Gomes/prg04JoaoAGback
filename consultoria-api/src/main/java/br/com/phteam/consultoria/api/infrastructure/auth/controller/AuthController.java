package br.com.phteam.consultoria.api.infrastructure.auth.controller;

import br.com.phteam.consultoria.api.infrastructure.auth.dto.LoginRequestDTO; // Seu DTO de Requisição
import br.com.phteam.consultoria.api.infrastructure.auth.dto.LoginResponseDTO; // Seu DTO de Resposta
import br.com.phteam.consultoria.api.infrastructure.auth.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller responsável pelo endpoint de Login e geração de JWT.
 * O mapeamento "/api/auth/**" está configurado em SecurityConfiguration para ser PÚBLICO.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Gerenciador de autenticação do Spring Security (usado para checar a senha)
    private final AuthenticationManager authenticationManager;

    // Serviço para gerar, ler e validar o JWT
    private final JwtTokenService jwtTokenService;

    /**
     * Endpoint de login. Recebe o email e a senha, valida e retorna o token JWT.
     * @param request O DTO contendo email e senha.
     * @return ResponseEntity contendo o token JWT, email e o tipo de usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        // 1. Autenticação Principal: Valida a combinação email/senha.
        // Se a senha estiver incorreta ou o usuário não existir, uma exceção (401 Unauthorized) será lançada.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // 2. Extrai os detalhes do usuário logado (UserDetails é o Princial)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Extrai o Tipo de Usuário (ROLE)
        // Busca a primeira role, remove o prefixo "ROLE_" e converte para Título (ex: CLIENTE -> Cliente)
        String tipoUsuario = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst() // Pega a primeira Role (ex: ROLE_CLIENTE)
                .map(role -> role.replace("ROLE_", "")) // Remove o prefixo
                .map(String::toLowerCase) // Ex: "CLIENTE" -> "cliente"
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)) // Ex: "cliente" -> "Cliente"
                .orElse("Indefinido");

        // 4. Gera o JWT
        String token = jwtTokenService.generateToken(userDetails.getUsername());

        // 5. Retorna a resposta completa
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                userDetails.getUsername(), // Email
                tipoUsuario
        );

        return ResponseEntity.ok(response);
    }
}