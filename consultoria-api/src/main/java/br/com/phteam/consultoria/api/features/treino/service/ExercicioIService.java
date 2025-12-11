package br.com.phteam.consultoria.api.features.treino.service;

import br.com.phteam.consultoria.api.features.treino.model.Exercicio;

import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT

import java.util.Optional;

public interface ExercicioIService {

    Exercicio salvar(Exercicio exercicio);

    Optional<Exercicio> buscarPorId(Long id);

    // MÉTODO MODIFICADO: Agora aceita Pageable e retorna Page
    Page<Exercicio> buscarTodos(Pageable pageable);

    Optional<Exercicio> atualizar(Long id, Exercicio detalhesExercicio);

    void excluirPorId(Long id);
}