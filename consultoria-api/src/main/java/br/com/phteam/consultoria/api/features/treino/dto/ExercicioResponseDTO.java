package br.com.phteam.consultoria.api.features.treino.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExercicioResponseDTO {

    private Long id;
    private Long clienteId; // ID do Cliente associado
    private String nome;
    private String objetivo;
    private String descricao;


    private LocalDateTime dataCriacao;


}