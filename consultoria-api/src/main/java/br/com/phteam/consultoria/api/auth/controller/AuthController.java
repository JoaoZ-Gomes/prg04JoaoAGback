package br.com.phteam.consultoria.api.auth.controller;

import br.com.phteam.consultoria.api.auth.service.AuthService;
import br.com.phteam.consultoria.api.auth.dto.LoginRequestDTO; // DTO para receber email/senha
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login : Endpoint para login (Autenticação).
     * @param request DTO contendo email e senha.
     * @return Retorna um token JWT ou erro de autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        // o serviço de autenticação deve validar as credenciais.
        try {
            String token = authService.autenticarEGerarToken(request.getEmail(), request.getSenha());
            // retornaria um objeto com o token JWT.
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (RuntimeException e) {
            // Exemplo de retorno de erro de autenticação
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }
    }

    // POST /api/auth/register (Para criar novos usuários)
    // POST /api/auth/logout
}


class AuthResponseDTO {
    public String token;
    public AuthResponseDTO(String token) {
        this.token = token;
    }
}