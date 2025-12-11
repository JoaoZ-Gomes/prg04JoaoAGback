package br.com.phteam.consultoria.api.features.treino.service;

import br.com.phteam.consultoria.api.features.treino.model.RotinaExercicio;
import br.com.phteam.consultoria.api.features.treino.repository.RotinaExercicioRepository;

import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class RotinaExercicioService implements RotinaExercicioIService {

    private final RotinaExercicioRepository rotinaExercicioRepository;

    @Autowired
    public RotinaExercicioService(RotinaExercicioRepository rotinaExercicioRepository) {
        this.rotinaExercicioRepository = rotinaExercicioRepository;
    }

    @Override
    @Transactional // Garante que a operação de salvamento seja atômica
    public RotinaExercicio salvar(RotinaExercicio rotina) {
        // Regra de Negócio (A Fazer): Validar que o Exercicio e a SessaoTreino referenciados existam.
        // Se a validação falhar, deve lançar RegraDeNegocioException (HTTP 400).
        return rotinaExercicioRepository.save(rotina);
    }

    @Override
    public Optional<RotinaExercicio> buscarPorId(Long id) {
        return rotinaExercicioRepository.findById(id);
    }

    /**
     * Busca rotinas de exercício de uma Sessão de Treino específica, de forma paginada.
     * @param sessaoTreinoId ID da sessão de treino.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Uma página (Page) de RotinaExercicio.
     */
    @Override
    public Page<RotinaExercicio> buscarPorSessaoId(Long sessaoTreinoId, Pageable pageable) {
        // MÉTODO MODIFICADO: Delega a paginação ao Repository
        return rotinaExercicioRepository.findBySessaoTreinoId(sessaoTreinoId, pageable);
    }

    @Override
    @Transactional // Garante que a operação de exclusão seja atômica
    public void excluirPorId(Long id) {
        // Verifica a existência antes de deletar e lança RecursoNaoEncontradoException (HTTP 404)
        if (!rotinaExercicioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Rotina de Exercício não encontrada com ID: " + id);
        }
        rotinaExercicioRepository.deleteById(id);
    }
}