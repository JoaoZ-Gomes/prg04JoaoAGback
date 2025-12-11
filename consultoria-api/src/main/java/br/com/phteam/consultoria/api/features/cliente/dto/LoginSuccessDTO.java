package br.com.phteam.consultoria.api.features.cliente.dto;
// ... imports ...

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginSuccessDTO {
    private String status;
    private String email;
    private String tipoUsuario;
}