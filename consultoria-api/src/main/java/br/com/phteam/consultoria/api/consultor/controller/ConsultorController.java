package br.com.phteam.consultoria.api.consultor.controller;

import br.com.phteam.consultoria.api.config.mapper.ObjectMapperUtil;
import br.com.phteam.consultoria.api.consultor.dto.ConsultorRequestDTO;
import br.com.phteam.consultoria.api.consultor.dto.ConsultorResponseDTO;
import br.com.phteam.consultoria.api.consultor.model.Consultor;
import br.com.phteam.consultoria.api.consultor.service.ConsultorService;
import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

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

    // POST /api/consultores - Validação Adicionada
    @PostMapping
    public ResponseEntity<ConsultorResponseDTO> criarConsultor(@RequestBody @Valid ConsultorRequestDTO consultorRequest) { // <-- @Valid ADICIONADO AQUI
        // 1. Converte DTO -> Model
        Consultor consultor = mapper.map(consultorRequest, Consultor.class);

        // 2. Salva no Service (a lógica de criptografia de senha deve estar aqui)
        Consultor consultorSalvo = consultorService.salvar(consultor);

        // 3. Converte Model -> DTO de Resposta
        ConsultorResponseDTO response = mapper.map(consultorSalvo, ConsultorResponseDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    // GET /api/consultores
    @GetMapping
    public ResponseEntity<List<ConsultorResponseDTO>> buscarTodosConsultores() {
        List<Consultor> consultores = consultorService.buscarTodos();
        // Converte List<Consultor> -> List<ConsultorResponseDTO>
        List<ConsultorResponseDTO> responseList = mapper.mapAll(consultores, ConsultorResponseDTO.class);
        return ResponseEntity.ok(responseList);
    }

    // GET /api/consultores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> buscarConsultorPorId(@PathVariable Long id) {
        Consultor consultor = consultorService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consultor não encontrado com ID: " + id));

        // Converte Model -> DTO de Resposta
        ConsultorResponseDTO response = mapper.map(consultor, ConsultorResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // PUT /api/consultores/{id} - Validação Adicionada
    @PutMapping("/{id}")
    public ResponseEntity<ConsultorResponseDTO> atualizarConsultor(@PathVariable Long id, @RequestBody @Valid ConsultorRequestDTO detalhesConsultor) { // <-- @Valid ADICIONADO AQUI
        // O serviço precisa ser ajustado para receber o DTO
        // Por enquanto, vou manter a conversão aqui
        Consultor dadosAtualizados = mapper.map(detalhesConsultor, Consultor.class);

        Consultor consultorAtualizado = consultorService.atualizar(id, dadosAtualizados)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consultor não encontrado para atualização com ID: " + id));

        // Converte Model -> DTO de Resposta
        ConsultorResponseDTO response = mapper.map(consultorAtualizado, ConsultorResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/consultores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsultor(@PathVariable Long id) {
        // Supondo que o serviço lança RecursoNaoEncontradoException se o ID não existir
        // Caso contrário, você deve lançar a exceção aqui.
        if (consultorService.deletarConsultor(id)) {
            return ResponseEntity.noContent().build();
        } else {
            // Lança a exceção de Recurso Não Encontrado se não deletar
            throw new RecursoNaoEncontradoException("Consultor não encontrado para exclusão com ID: " + id);
        }
    }
}