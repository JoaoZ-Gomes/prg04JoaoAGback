package br.com.phteam.consultoria.api.features.cliente.controller;

import br.com.phteam.consultoria.api.features.cliente.dto.ClienteRequestDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.ClienteResponseDTO;
import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.service.ClienteService;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.mapper.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ObjectMapperUtil mapper;

    // ---------------------------
    // POST /api/clientes
    // ---------------------------
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criarCliente(
            @RequestBody @Valid ClienteRequestDTO clienteRequest) {

        Cliente cliente = mapper.map(clienteRequest, Cliente.class);
        Cliente clienteSalvo = clienteService.salvar(cliente);

        return ResponseEntity
                .status(201)
                .body(mapper.map(clienteSalvo, ClienteResponseDTO.class));
    }

    // ---------------------------
    // GET /api/clientes (paginado)
    // ---------------------------
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> buscarTodosClientes(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<Cliente> pagina = clienteService.buscarTodos(pageable);
        Page<ClienteResponseDTO> paginaDTO =
                pagina.map(c -> mapper.map(c, ClienteResponseDTO.class));

        return ResponseEntity.ok(paginaDTO);
    }

    // ---------------------------
    // GET /api/clientes/{id}
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id)
                );

        return ResponseEntity.ok(mapper.map(cliente, ClienteResponseDTO.class));
    }

    // ---------------------------
    // PUT /api/clientes/{id}
    // ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO detalhesCliente) {

        Cliente dadosAtualizados = mapper.map(detalhesCliente, Cliente.class);

        Cliente atualizado = clienteService.atualizar(id, dadosAtualizados)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado para atualização com ID: " + id)
                );

        return ResponseEntity.ok(mapper.map(atualizado, ClienteResponseDTO.class));
    }

    // ---------------------------
    // DELETE /api/clientes/{id}
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {

        clienteService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // GET /api/clientes/meu-perfil
    // ---------------------------
    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> meuPerfil(Authentication authentication) {
        // pega o email do cliente logado via JWT
        String email = authentication.getName();

        Cliente cliente = clienteService.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado para email: " + email)
                );

        return ResponseEntity.ok(mapper.map(cliente, ClienteResponseDTO.class));
    }
}
