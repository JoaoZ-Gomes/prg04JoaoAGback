package br.com.phteam.consultoria.api.features.cliente.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.phteam.consultoria.api.features.cliente.dto.request.ClienteRequestDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.request.ClienteUpdateDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.response.ClienteResponseDTO;
import br.com.phteam.consultoria.api.features.cliente.service.ClienteIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteIService service;

    // ---------------------------
    // POST
    // ---------------------------
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(
            @RequestBody @Valid ClienteRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }

    // ---------------------------
    // GET PAGINADO
    // ---------------------------
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(service.buscarTodos(pageable));
    }

    // ---------------------------
    // GET POR ID
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // ---------------------------
    // PUT
    // ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClienteUpdateDTO dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // ---------------------------
    // DELETE
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // MEU PERFIL
    // ---------------------------
    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> meuPerfil(Authentication auth) {
        return ResponseEntity.ok(service.buscarPorEmail(auth.getName()));
    }
}
