package br.com.phteam.consultoria.api.features.avaliacao.service;

import java.util.List;

import br.com.phteam.consultoria.api.features.avaliacao.dto.request.AvaliacaoCreateRequestDTO;
import br.com.phteam.consultoria.api.features.avaliacao.dto.response.AvaliacaoResponseDTO;

public interface AvaliacaoIService {

    AvaliacaoResponseDTO criar(AvaliacaoCreateRequestDTO dto, String emailConsultor);

    List<AvaliacaoResponseDTO> listarPorClienteEmail(String emailCliente);

    List<AvaliacaoResponseDTO> listarPorConsultor(String emailConsultor);
}
