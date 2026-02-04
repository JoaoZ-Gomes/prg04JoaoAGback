package br.com.phteam.consultoria.api.features.avaliacao.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.com.phteam.consultoria.api.features.avaliacao.dto.request.AvaliacaoCreateRequestDTO;
import br.com.phteam.consultoria.api.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.phteam.consultoria.api.features.avaliacao.service.AvaliacaoIService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoIService avaliacaoService;

    // 🔹 Avaliações do CLIENTE logado
    @GetMapping("/meu")
    public ResponseEntity<List<AvaliacaoResponseDTO>> minhasAvaliacoes(Authentication auth) {
        return ResponseEntity.ok(
                avaliacaoService.listarPorClienteEmail(auth.getName())
        );
    }

    // 🔹 Criar avaliação (qualquer consultor pode avaliar qualquer cliente)
    @PostMapping
    @PreAuthorize("hasRole('CONSULTOR')")
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(
            @RequestBody AvaliacaoCreateRequestDTO dto,
            Authentication auth) {

        AvaliacaoResponseDTO response =
                avaliacaoService.criar(dto, auth.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 Avaliações feitas PELO consultor logado
    @GetMapping("/consultor")
    @PreAuthorize("hasRole('CONSULTOR')")
    public ResponseEntity<List<AvaliacaoResponseDTO>> avaliacoesDoConsultor(Authentication auth) {
        return ResponseEntity.ok(
                avaliacaoService.listarPorConsultor(auth.getName())
        );
    }
}
