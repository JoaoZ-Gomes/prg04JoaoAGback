package br.com.phteam.consultoria.api.features.consultor.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ConsultorRequestDTO {

    // VALIDAÇÕES ATIVADAS
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    private String nome;

    // VALIDAÇÕES ATIVADAS
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    // VALIDAÇÕES ATIVADAS
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") // Adicionei um mínimo para a senha
    private String senha;

    private String telefone;

    // VALIDAÇÕES ATIVADAS
    @NotBlank(message = "O CREF é obrigatório")
    private String numeroCref;

    private String especializacao;
}