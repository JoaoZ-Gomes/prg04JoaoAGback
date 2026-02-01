package br.com.phteam.consultoria.api.features.rotina.controller;

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

import br.com.phteam.consultoria.api.features.rotina.dto.request.RotinaAtualizarRequestDTO;
import br.com.phteam.consultoria.api.features.rotina.dto.request.RotinaCriarRequestDTO;
import br.com.phteam.consultoria.api.features.rotina.dto.response.RotinaResponseDTO;
import br.com.phteam.consultoria.api.features.rotina.service.RotinaIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rotinas")
@RequiredArgsConstructor
public class RotinaController {

    private final RotinaIService service;

    @PostMapping
    public ResponseEntity<RotinaResponseDTO> criar(@RequestBody @Valid RotinaCriarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<RotinaResponseDTO>> listar(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RotinaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotinaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ficha/{fichaId}")
    public ResponseEntity<List<RotinaResponseDTO>> buscarPorFicha(@PathVariable Long fichaId) {
        return ResponseEntity.ok(service.findByFichaId(fichaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotinaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid RotinaAtualizarRequestDTO dto) {
        RotinaAtualizarRequestDTO withId = new RotinaAtualizarRequestDTO(id, dto.fichaId(), dto.exercicioId(), dto.series(), dto.repeticoes(), dto.nome());
        return ResponseEntity.ok(service.update(withId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
