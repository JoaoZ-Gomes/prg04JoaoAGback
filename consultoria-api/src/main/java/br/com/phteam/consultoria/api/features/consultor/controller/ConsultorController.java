package br.com.phteam.consultoria.api.features.consultor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.dto.request.ConsultorRequestDTO;
import br.com.phteam.consultoria.api.features.consultor.dto.response.ConsultorResponseDTO;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.service.ConsultorService;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.mapper.ObjectMapperUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consultores")
public class ConsultorController {

    private final ConsultorService consultorService;
    private final ObjectMapperUtil mapper;

    @Autowired
    public ConsultorController(ConsultorService consultorService, ObjectMapperUtil mapper) {
        this.consultorService = consultorService;
        this.mapper = mapper;
    }

    // =====================================================
    // CREATE
    // =====================================================
    @PostMapping
    public ResponseEntity<ConsultorResponseDTO> criarConsultor(
            @RequestBody @Valid ConsultorRequestDTO consultorRequest) {

        Consultor consultor = mapper.map(consultorRequest, Consultor.class);
        Consultor salvo = consultorService.salvar(consultor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.map(salvo, ConsultorResponseDTO.class));
    }

    // =====================================================
    // READ - LISTA PAGINADA
    // =====================================================
    @GetMapping
    public ResponseEntity<Page<ConsultorResponseDTO>> buscarTodos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ConsultorResponseDTO> response = consultorService.buscarTodos(pageable)
                .map(consultor -> mapper.map(consultor, ConsultorResponseDTO.class));

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // READ - POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> buscarPorId(@PathVariable Long id) {

        Consultor consultor = consultorService.buscarPorId(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Consultor não encontrado com ID: " + id));

        return ResponseEntity.ok(mapper.map(consultor, ConsultorResponseDTO.class));
    }

    // =====================================================
    // DASHBOARD DO CONSULTOR LOGADO
    // =====================================================
    @GetMapping("/meus-clientes")
    @PreAuthorize("hasRole('CONSULTOR')")
    public ResponseEntity<List<Cliente>> buscarMeusClientes(Authentication authentication) {

        String emailConsultor = authentication.getName();

        // Buscar o consultor pelo email para descobrir o ID
        Consultor consultor = consultorService.buscarPorEmail(emailConsultor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Consultor não encontrado com email: " + emailConsultor
                        )
                );

        List<Cliente> clientes = consultorService.buscarClientesDoConsultorPorId(consultor.getId());

        return ResponseEntity.ok(clientes);
    }



    // =====================================================
    // UPDATE
    // =====================================================
    @PutMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ConsultorRequestDTO request) {

        Consultor dadosAtualizados = mapper.map(request, Consultor.class);

        Consultor atualizado = consultorService.atualizar(id, dadosAtualizados)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Consultor não encontrado com ID: " + id));

        return ResponseEntity.ok(mapper.map(atualizado, ConsultorResponseDTO.class));
    }

    // =====================================================
    // DELETE
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {


        consultorService.deletarConsultor(id);


        return ResponseEntity.noContent().build();
    }

}
