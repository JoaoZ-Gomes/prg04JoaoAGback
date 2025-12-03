package br.com.phteam.consultoria.api.consultor.dto;

import lombok.Data;

@Data
public class ConsultorResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String numeroCref;
    private String especializacao;

    // NOTA: 'hashSenha' não está incluído, garantindo a segurança.
}