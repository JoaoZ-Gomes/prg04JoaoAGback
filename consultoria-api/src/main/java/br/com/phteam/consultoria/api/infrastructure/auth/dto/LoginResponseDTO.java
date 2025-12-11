package br.com.phteam.consultoria.api.infrastructure.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String tipoUsuario; // Ex: "Cliente" ou "Consultor"
}