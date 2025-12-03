package br.com.phteam.consultoria.api.consultor.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
 import jakarta.validation.constraints.Size;

@Data
public class ConsultorRequestDTO {

    // @NotBlank(message = "O nome é obrigatório")
    // @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    private String nome;

    // @NotBlank(message = "O email é obrigatório")
    // @Email(message = "Formato de e-mail inválido")
    private String email;

    // @NotBlank(message = "A senha é obrigatória")
    private String senha; // Nota: A senha ainda será enviada e tratada no Service

    private String telefone;

    // @NotBlank(message = "O CREF é obrigatório")
    private String numeroCref;

    private String especializacao;
}