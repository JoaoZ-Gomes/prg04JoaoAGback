package br.com.phteam.consultoria.api.features.avaliacao.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Avaliação. Inclui informações básicas da avaliação e
 * metadados do cliente associada quando necessário.
 */
public record AvaliacaoResponseDTO(
    Long id,
    LocalDateTime dataAvaliacao,
    double peso,
    double percentualGordura,
    String observacoes,
    Long clienteId,
    String clienteNome
) {}
