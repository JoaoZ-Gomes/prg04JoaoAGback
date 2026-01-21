package br.com.phteam.consultoria.api.features.cliente.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUpdateDTO {

    @Positive(message = "Peso deve ser positivo")
    private Double pesoAtual;

    @Positive(message = "Altura deve ser positiva")
    private Double altura;


}
