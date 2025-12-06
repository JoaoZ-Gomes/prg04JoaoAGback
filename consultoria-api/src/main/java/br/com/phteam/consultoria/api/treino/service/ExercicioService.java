package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.Exercicio;
import br.com.phteam.consultoria.api.treino.repository.ExercicioRepository;

import br.com.phteam.consultoria.api.exception.RegraDeNegocioException;
import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- NOVO IMPORT ADICIONADO

import java.util.List;
import java.util.Optional;

@Service
public class ExercicioService implements ExercicioIService {

    private final ExercicioRepository exercicioRepository;

    @Autowired
    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    @Override
    @Transactional // Garante que a operação de salvamento seja atômica
    public Exercicio salvar(Exercicio exercicio) {
        // Regra de Negócio: Valida unicidade do nome.
        if (exercicioRepository.findByNome(exercicio.getNome()).isPresent()) {
            // Substituído o IllegalArgumentException por RegraDeNegocioException (HTTP 400)
            throw new RegraDeNegocioException("Já existe um exercício com este nome no catálogo.");
        }
        return exercicioRepository.save(exercicio);
    }

    @Override
    public Optional<Exercicio> buscarPorId(Long id) {
        return exercicioRepository.findById(id);
    }

    @Override
    public List<Exercicio> buscarTodos() {
        return exercicioRepository.findAll();
    }

    @Override
    @Transactional // Garante que a operação de atualização seja atômica
    public Optional<Exercicio> atualizar(Long id, Exercicio detalhesExercicio) {
        // O Controller usará .orElseThrow() aqui para o 404
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

    @Override
    @Transactional // Garante que a operação de exclusão seja atômica
    public void excluirPorId(Long id) {
        // Verifica a existência antes de deletar e lança RecursoNaoEncontradoException (HTTP 404)
        if (!exercicioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Exercício não encontrado com ID: " + id);
        }

        // Nota: Considerar regra de negócio para impedir exclusão se o exercício estiver em uso.
        exercicioRepository.deleteById(id);
    }
}