package br.com.phteam.consultoria.api.features.avaliacao.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoCreateRequestDTO(
    @NotNull Long clienteId,
    @Min(0) double peso,
    @Min(0) double percentualGordura,
    String observacoes
) {}
