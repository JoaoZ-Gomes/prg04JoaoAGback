package br.com.phteam.consultoria.api.treino.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório para associar o treino.")
    private Long clienteId; // ID do Cliente ao qual este treino pertence

    @NotBlank(message = "O nome do treino é obrigatório.")
    @Size(max = 100, message = "O nome do treino deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O objetivo do treino é obrigatório.")
    @Size(max = 255, message = "O objetivo deve ter no máximo 255 caracteres.")
    private String objetivo;

    @NotBlank(message = "A descrição/instruções do treino são obrigatórias.")
    private String descricao;
}