package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.Exercicio;
import br.com.phteam.consultoria.api.treino.repository.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de domínio para a gestão do Catálogo de Exercícios (CRUD de Exercicios base).
 */
@Service
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;

    @Autowired
    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    /**
     * Persiste um novo Exercício no catálogo.
     * @param exercicio Objeto Exercicio a ser persistido.
     * @return Exercicio salvo.
     */
    public Exercicio salvar(Exercicio exercicio) {
        // Regra de Negócio: Valida unicidade do nome do exercício antes da persistência.
        if (exercicioRepository.findByNome(exercicio.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe um exercício com este nome no catálogo.");
        }
        return exercicioRepository.save(exercicio);
    }

    public Optional<Exercicio> buscarPorId(Long id) {
        return exercicioRepository.findById(id);
    }

    public List<Exercicio> buscarTodos() {
        return exercicioRepository.findAll();
    }

    /**
     * Atualiza os detalhes de um Exercício existente.
     * @param id ID do Exercício a ser atualizado.
     * @param detalhesExercicio Objeto contendo os dados de atualização.
     * @return Optional contendo o Exercício atualizado ou vazio.
     */
    public Optional<Exercicio> atualizar(Long id, Exercicio detalhesExercicio) {
        return exercicioRepository.findById(id)
                .map(exercicioExistente -> {
                    // Atualização parcial dos campos relevantes
                    if (detalhesExercicio.getNome() != null) {
                        exercicioExistente.setNome(detalhesExercicio.getNome());
                    }
                    if (detalhesExercicio.getGrupoMuscular() != null) {
                        exercicioExistente.setGrupoMuscular(detalhesExercicio.getGrupoMuscular());
                    }
                    if (detalhesExercicio.getUrlVideo() != null) {
                        exercicioExistente.setUrlVideo(detalhesExercicio.getUrlVideo());
                    }

                    return exercicioRepository.save(exercicioExistente);
                });
    }

    public void excluirPorId(Long id) {
        // Nota: Considerar regra de negócio para impedir exclusão se o exercício estiver em uso.
        exercicioRepository.deleteById(id);
    }
}