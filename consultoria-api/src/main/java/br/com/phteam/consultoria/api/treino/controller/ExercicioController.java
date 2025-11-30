package br.com.phteam.consultoria.api.treino.controller;

import br.com.phteam.consultoria.api.treino.model.Exercicio;
import br.com.phteam.consultoria.api.treino.service.ExercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    @Autowired
    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    // POST /api/exercicios
    @PostMapping
    public ResponseEntity<Exercicio> criarExercicio(@RequestBody Exercicio exercicio) {
        Exercicio exercicioSalvo = exercicioService.salvar(exercicio);
        return ResponseEntity.status(201).body(exercicioSalvo);
    }

    // GET /api/exercicios
    @GetMapping
    public ResponseEntity<List<Exercicio>> buscarTodosExercicios() {
        List<Exercicio> exercicios = exercicioService.buscarTodos();
        return ResponseEntity.ok(exercicios);
    }

    // PUT /api/exercicios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Exercicio> atualizarExercicio(@PathVariable Long id, @RequestBody Exercicio detalhesExercicio) {
        return exercicioService.atualizar(id, detalhesExercicio)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/exercicios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirExercicio(@PathVariable Long id) {
        if (exercicioService.buscarPorId(id).isPresent()) {
            exercicioService.excluirPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}