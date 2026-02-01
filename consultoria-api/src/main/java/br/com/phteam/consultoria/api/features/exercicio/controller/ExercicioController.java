package br.com.phteam.consultoria.api.features.exercicio.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.phteam.consultoria.api.features.exercicio.dto.request.ExercicioAtualizarRequestDTO;
import br.com.phteam.consultoria.api.features.exercicio.dto.request.ExercicioCriarRequestDTO;
import br.com.phteam.consultoria.api.features.exercicio.dto.response.ExercicioResponseDTO;
import br.com.phteam.consultoria.api.features.exercicio.service.ExercicioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final ExercicioIService service;

    @PostMapping
    public ResponseEntity<ExercicioResponseDTO> criar(@RequestBody @Valid ExercicioCriarRequestDTO dto) {
        ExercicioResponseDTO saved = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<ExercicioResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExercicioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExercicioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ExercicioAtualizarRequestDTO dto) {

        ExercicioAtualizarRequestDTO withId = new ExercicioAtualizarRequestDTO(
                id,
                dto.nome(),
                dto.grupoMuscular(),
                dto.equipamento(),
                dto.urlVideo(),
                dto.descricao()
        );

        return ResponseEntity.ok(service.update(withId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
