package br.com.phteam.consultoria.api.features.treino.controller;

import br.com.phteam.consultoria.api.infrastructure.mapper.ObjectMapperUtil;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.features.treino.dto.ExercicioRequestDTO;
import br.com.phteam.consultoria.api.features.treino.dto.ExercicioResponseDTO;
import br.com.phteam.consultoria.api.features.treino.model.Exercicio;
import br.com.phteam.consultoria.api.features.treino.service.ExercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

// NOVOS IMPORTS PARA PAGINAÇÃO
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;
    private final ObjectMapperUtil mapper;

    @Autowired
    public ExercicioController(ExercicioService exercicioService, ObjectMapperUtil mapper) {
        this.exercicioService = exercicioService;
        this.mapper = mapper;
    }

    // POST /api/exercicios - Usa DTO e Ativa Validação
    @PostMapping
    public ResponseEntity<ExercicioResponseDTO> criarExercicio(@RequestBody @Valid ExercicioRequestDTO exercicioRequest) {
        // 1. Converte DTO -> Model
        Exercicio exercicio = mapper.map(exercicioRequest, Exercicio.class);

        // 2. Salva no Service (Regra de negócio é validada aqui)
        Exercicio exercicioSalvo = exercicioService.salvar(exercicio);

        // 3. Converte Model -> DTO de Resposta
        ExercicioResponseDTO response = mapper.map(exercicioSalvo, ExercicioResponseDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    // GET /api/exercicios - AGORA COM PAGINAÇÃO
    @GetMapping
    public ResponseEntity<Page<ExercicioResponseDTO>> buscarTodosExercicios(
            // Recebe os parâmetros page, size e sort da URL, com valores padrão
            @PageableDefault(size = 10, page = 0, sort = "nome") Pageable pageable) {

        // 1. Busca a página do Service
        Page<Exercicio> exerciciosPage = exercicioService.buscarTodos(pageable);

        // 2. Converte a Page de Model para Page de DTO
        // O método .map() do Page faz a conversão elemento por elemento
        Page<ExercicioResponseDTO> responsePage = exerciciosPage.map(exercicio ->
                mapper.map(exercicio, ExercicioResponseDTO.class)
        );

        return ResponseEntity.ok(responsePage);
    }

    // GET /api/exercicios/{id} - Lança 404 via Handler
    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponseDTO> buscarExercicioPorId(@PathVariable Long id) {
        Exercicio exercicio = exercicioService.buscarPorId(id)
                // Lança 404 se não encontrado
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado com ID: " + id));

        // Converte Model -> DTO de Resposta
        ExercicioResponseDTO response = mapper.map(exercicio, ExercicioResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // PUT /api/exercicios/{id} - Usa DTO, Ativa Validação e Lança 404 via Handler
    @PutMapping("/{id}")
    public ResponseEntity<ExercicioResponseDTO> atualizarExercicio(@PathVariable Long id, @RequestBody @Valid ExercicioRequestDTO detalhesExercicio) {
        // 1. Converte DTO -> Model
        Exercicio dadosAtualizados = mapper.map(detalhesExercicio, Exercicio.class);

        Exercicio exercicioAtualizado = exercicioService.atualizar(id, dadosAtualizados)
                // Lança 404 se não encontrado
                .orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado para atualização com ID: " + id));

        // Converte Model -> DTO de Resposta
        ExercicioResponseDTO response = mapper.map(exercicioAtualizado, ExercicioResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/exercicios/{id} - Serviço lida com a exceção 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirExercicio(@PathVariable Long id) {
        // O serviço (ExercicioService) já lança RecursoNaoEncontradoException se não encontrar o ID,
        // garantindo que o Handler retorne o 404.
        exercicioService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}