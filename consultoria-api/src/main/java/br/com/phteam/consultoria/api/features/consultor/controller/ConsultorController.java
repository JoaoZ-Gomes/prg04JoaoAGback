package br.com.phteam.consultoria.api.features.consultor.controller;

import br.com.phteam.consultoria.api.infrastructure.mapper.ObjectMapperUtil;
import br.com.phteam.consultoria.api.features.consultor.dto.ConsultorRequestDTO;
import br.com.phteam.consultoria.api.features.consultor.dto.ConsultorResponseDTO;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.service.ConsultorService;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

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

    // GET /api/consultores - AGORA COM PAGINAÇÃO
    @GetMapping
    public ResponseEntity<Page<ConsultorResponseDTO>> buscarTodosConsultores(
            // Recebe os parâmetros page, size e sort da URL, com valores padrão
            @PageableDefault(size = 10, page = 0, sort = "nome") Pageable pageable) {

        // 1. Busca a página do Service
        Page<Consultor> consultoresPage = consultorService.buscarTodos(pageable);

        // 2. Converte a Page de Model para Page de DTO
        // O método .map() do Page faz a conversão elemento por elemento
        Page<ConsultorResponseDTO> responsePage = consultoresPage.map(consultor ->
                mapper.map(consultor, ConsultorResponseDTO.class)
        );

        return ResponseEntity.ok(responsePage);
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