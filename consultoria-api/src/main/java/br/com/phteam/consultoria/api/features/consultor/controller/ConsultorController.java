package br.com.phteam.consultoria.api.features.consultor.controller;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.dto.ConsultorRequestDTO;
import br.com.phteam.consultoria.api.features.consultor.dto.ConsultorResponseDTO;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.service.ConsultorService;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.mapper.ObjectMapperUtil;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelas operações relacionadas aos Consultores.
 */
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

    /**
     * Cria um novo consultor.
     */
    @PostMapping
    public ResponseEntity<ConsultorResponseDTO> criarConsultor(
            @RequestBody @Valid ConsultorRequestDTO consultorRequest
    ) {
        Consultor consultor = mapper.map(consultorRequest, Consultor.class);
        Consultor consultorSalvo = consultorService.salvar(consultor);
        ConsultorResponseDTO response = mapper.map(consultorSalvo, ConsultorResponseDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    // =====================================================
    // READ
    // =====================================================

    /**
     * Lista todos os consultores com paginação.
     */
    @GetMapping
    public ResponseEntity<Page<ConsultorResponseDTO>> buscarTodosConsultores(
            @PageableDefault(size = 10, page = 0, sort = "nome") Pageable pageable
    ) {
        Page<Consultor> consultores = consultorService.buscarTodos(pageable);

        Page<ConsultorResponseDTO> response = consultores.map(
                consultor -> mapper.map(consultor, ConsultorResponseDTO.class)
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca um consultor por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> buscarConsultorPorId(@PathVariable Long id) {

        Consultor consultor = consultorService.buscarPorId(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Consultor não encontrado com ID: " + id)
                );

        ConsultorResponseDTO response = mapper.map(consultor, ConsultorResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ENDPOINT PRINCIPAL DO DASHBOARD DO CONSULTOR
    // =====================================================

    /**
     * Retorna os clientes vinculados ao consultor LOGADO.
     * Apenas usuários com ROLE_CONSULTOR podem acessar.
     */
    @GetMapping("/meus-clientes")
    @PreAuthorize("hasRole('CONSULTOR')")
    public List<Cliente> buscarMeusClientes(Authentication authentication) {

        // Email vem diretamente do JWT (subject)
        String emailConsultor = authentication.getName();

        // 🔥 DEBUG (pode remover depois)
        System.out.println("🔥 CONSULTOR LOGADO: " + emailConsultor);
        authentication.getAuthorities()
                .forEach(a -> System.out.println("🔥 ROLE: " + a.getAuthority()));

        return consultorService.buscarClientesDoConsultor(emailConsultor);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    /**
     * Atualiza os dados de um consultor.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> atualizarConsultor(
            @PathVariable Long id,
            @RequestBody @Valid ConsultorRequestDTO detalhesConsultor
    ) {
        Consultor dadosAtualizados = mapper.map(detalhesConsultor, Consultor.class);

        Consultor consultorAtualizado = consultorService.atualizar(id, dadosAtualizados)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Consultor não encontrado para atualização com ID: " + id)
                );

        ConsultorResponseDTO response = mapper.map(consultorAtualizado, ConsultorResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // DELETE
    // =====================================================

    /**
     * Deleta um consultor pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsultor(@PathVariable Long id) {

        if (consultorService.deletarConsultor(id)) {
            return ResponseEntity.noContent().build();
        }

        throw new RecursoNaoEncontradoException(
                "Consultor não encontrado para exclusão com ID: " + id
        );
    }
}
