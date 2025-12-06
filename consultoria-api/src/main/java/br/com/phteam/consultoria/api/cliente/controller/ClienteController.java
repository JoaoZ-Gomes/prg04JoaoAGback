package br.com.phteam.consultoria.api.cliente.controller;

import br.com.phteam.consultoria.api.cliente.dto.ClienteRequestDTO;
import br.com.phteam.consultoria.api.cliente.dto.ClienteResponseDTO;
import br.com.phteam.consultoria.api.cliente.model.Cliente;
import br.com.phteam.consultoria.api.cliente.service.ClienteService;
import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.config.mapper.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ObjectMapperUtil mapper; // Adicionar o Mapper

    @Autowired
    public ClienteController(ClienteService clienteService, ObjectMapperUtil mapper) {
        this.clienteService = clienteService;
        this.mapper = mapper;
    }

    // POST /api/clientes - Usa DTO e Ativa Validação
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criarCliente(@RequestBody @Valid ClienteRequestDTO clienteRequest) {
        // 1. Converte DTO -> Model
        Cliente cliente = mapper.map(clienteRequest, Cliente.class);

        // 2. Salva no Service
        Cliente clienteSalvo = clienteService.salvar(cliente);

        // 3. Converte Model -> DTO de Resposta
        ClienteResponseDTO response = mapper.map(clienteSalvo, ClienteResponseDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    // GET /api/clientes
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> buscarTodosClientes() {
        List<Cliente> clientes = clienteService.buscarTodos();
        // Converte List<Cliente> -> List<ClienteResponseDTO>
        List<ClienteResponseDTO> responseList = mapper.mapAll(clientes, ClienteResponseDTO.class);
        return ResponseEntity.ok(responseList);
    }

    // GET /api/clientes/{id} - Lança 404 via Handler
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                // Lança a exceção que o ErrorHandler captura e transforma em 404
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));

        // Converte Model -> DTO de Resposta
        ClienteResponseDTO response = mapper.map(cliente, ClienteResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // PUT /api/clientes/{id} - Usa DTO, Ativa Validação e Lança 404 via Handler
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO detalhesCliente) {
        // 1. Converte DTO -> Model
        Cliente dadosAtualizados = mapper.map(detalhesCliente, Cliente.class);

        Cliente clienteAtualizado = clienteService.atualizar(id, dadosAtualizados)
                // Lança a exceção que o ErrorHandler captura e transforma em 404
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para atualização com ID: " + id));

        // Converte Model -> DTO de Resposta
        ClienteResponseDTO response = mapper.map(clienteAtualizado, ClienteResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/clientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        // O ClienteService já lança RecursoNaoEncontradoException, então a chamada direta é o suficiente:
        clienteService.excluirPorId(id);

        // Se a linha acima for bem-sucedida, retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}