package br.com.phteam.consultoria.api.cliente.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String telefone;

}