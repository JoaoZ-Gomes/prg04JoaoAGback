package br.com.phteam.consultoria.api.auth.controller;

import br.com.phteam.consultoria.api.auth.dto.LoginRequestDTO;
import br.com.phteam.consultoria.api.auth.dto.LoginSuccessDTO; // Import alterado
import br.com.phteam.consultoria.api.auth.service.AuthIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthIService authService;

    /**
     * Endpoint para autenticar um Cliente ou Consultor.
     * URL: /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessDTO> autenticar(@RequestBody @Valid LoginRequestDTO request) {

        // Chama o AuthService
        LoginSuccessDTO response = authService.autenticar(request);

        // Retorna o status de sucesso
        return ResponseEntity.ok(response);
    }
}