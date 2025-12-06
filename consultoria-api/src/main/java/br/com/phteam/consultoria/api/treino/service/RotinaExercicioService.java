package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.RotinaExercicio;
import br.com.phteam.consultoria.api.treino.repository.RotinaExercicioRepository;

import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public List<RotinaExercicio> buscarPorSessaoId(Long sessaoTreinoId) {
        return rotinaExercicioRepository.findBySessaoTreinoId(sessaoTreinoId);
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