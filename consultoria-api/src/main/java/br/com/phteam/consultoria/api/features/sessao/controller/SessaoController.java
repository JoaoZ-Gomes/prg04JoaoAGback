package br.com.phteam.consultoria.api.features.sessao.controller;

import br.com.phteam.consultoria.api.features.sessao.dto.request.SessaoAtualizarRequestDTO;
import br.com.phteam.consultoria.api.features.sessao.dto.request.SessaoCriarRequestDTO;
import br.com.phteam.consultoria.api.features.sessao.dto.response.SessaoResponseDTO;
import br.com.phteam.consultoria.api.features.sessao.service.SessaoIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoIService service;

    @PostMapping
    public ResponseEntity<SessaoResponseDTO> criar(@RequestBody @Valid SessaoCriarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<SessaoResponseDTO>> listar(@PageableDefault(size = 10, sort = "dataSessao") Pageable pageable) {
        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SessaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/rotina/{rotinaId}")
    public ResponseEntity<List<SessaoResponseDTO>> buscarPorRotina(@PathVariable Long rotinaId) {
        return ResponseEntity.ok(service.findByRotinaId(rotinaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid SessaoAtualizarRequestDTO dto) {
        SessaoAtualizarRequestDTO withId = new SessaoAtualizarRequestDTO(id, dto.dataSessao(), dto.observacoes(), dto.status());
        return ResponseEntity.ok(service.update(withId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
