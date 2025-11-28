package br.com.phteam.consultoria.api.consultor.controller;

import br.com.phteam.consultoria.api.consultor.model.Consultor;
import br.com.phteam.consultoria.api.consultor.service.ConsultorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultores")
public class ConsultorController {

    private final ConsultorService consultorService;

    @Autowired
    public ConsultorController(ConsultorService consultorService) {
        this.consultorService = consultorService;
    }

    // POST /api/consultores
    @PostMapping
    public ResponseEntity<Consultor> criarConsultor(@RequestBody Consultor consultor) {
        Consultor consultorSalvo = consultorService.salvar(consultor);
        return ResponseEntity.status(201).body(consultorSalvo);
    }

    // GET /api/consultores
    @GetMapping
    public ResponseEntity<List<Consultor>> buscarTodosConsultores() {
        List<Consultor> consultores = consultorService.buscarTodos();
        return ResponseEntity.ok(consultores);
    }

    // GET /api/consultores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Consultor> buscarConsultorPorId(@PathVariable Long id) {
        return consultorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/consultores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Consultor> atualizarConsultor(@PathVariable Long id, @RequestBody Consultor detalhesConsultor) {
        return consultorService.atualizar(id, detalhesConsultor)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/consultores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConsultor(@PathVariable Long id) {
        if (consultorService.buscarPorId(id).isPresent()) {
            consultorService.excluirPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}