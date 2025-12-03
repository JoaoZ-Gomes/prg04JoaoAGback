package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.Exercicio;
import java.util.List;
import java.util.Optional;

public interface ExercicioIService {

    Exercicio salvar(Exercicio exercicio);

    Optional<Exercicio> buscarPorId(Long id);

    List<Exercicio> buscarTodos();

    Optional<Exercicio> atualizar(Long id, Exercicio detalhesExercicio);

    void excluirPorId(Long id);
}