package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.RotinaExercicio;

import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT

import java.util.Optional;

public interface RotinaExercicioIService {

    RotinaExercicio salvar(RotinaExercicio rotina);

    Optional<RotinaExercicio> buscarPorId(Long id);

    /**
     * Busca rotinas de exercício de uma Sessão de Treino específica, de forma paginada.
     * @param sessaoTreinoId ID da sessão de treino.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Uma página (Page) de RotinaExercicio.
     */
    Page<RotinaExercicio> buscarPorSessaoId(Long sessaoTreinoId, Pageable pageable); // MÉTODO MODIFICADO

    void excluirPorId(Long id);
}