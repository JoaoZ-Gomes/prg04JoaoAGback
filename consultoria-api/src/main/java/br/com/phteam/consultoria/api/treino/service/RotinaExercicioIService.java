package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.RotinaExercicio;
import java.util.List;
import java.util.Optional;

public interface RotinaExercicioIService {

    RotinaExercicio salvar(RotinaExercicio rotina);

    Optional<RotinaExercicio> buscarPorId(Long id);

    List<RotinaExercicio> buscarPorSessaoId(Long sessaoTreinoId);

    void excluirPorId(Long id);
}