package br.com.phteam.consultoria.api.treino.service;

import br.com.phteam.consultoria.api.treino.model.RotinaExercicio;
import br.com.phteam.consultoria.api.treino.repository.RotinaExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de domínio para a gestão das rotinas de execução de exercícios dentro de uma sessão.
 */
@Service
public class RotinaExercicioService {

    private final RotinaExercicioRepository rotinaExercicioRepository;

    @Autowired
    public RotinaExercicioService(RotinaExercicioRepository rotinaExercicioRepository) {
        this.rotinaExercicioRepository = rotinaExercicioRepository;
    }

    /**
     * Persiste a rotina de execução (séries/reps) em uma Sessão de Treino.
     * @param rotina Objeto RotinaExercicio a ser persistido.
     * @return RotinaExercicio salva.
     */
    public RotinaExercicio salvar(RotinaExercicio rotina) {
        // Regra de Negócio: Validar que o Exercicio e a SessaoTreino referenciados existam.
        return rotinaExercicioRepository.save(rotina);
    }

    public Optional<RotinaExercicio> buscarPorId(Long id) {
        return rotinaExercicioRepository.findById(id);
    }

    /**
     * Recupera todas as rotinas de uma sessão específica.
     * @param sessaoTreinoId ID da sessão de treino.
     * @return Lista de RotinasExercicio.
     */
    public List<RotinaExercicio> buscarPorSessaoId(Long sessaoTreinoId) {
        return rotinaExercicioRepository.findBySessaoTreinoId(sessaoTreinoId);
    }

    public void excluirPorId(Long id) {
        rotinaExercicioRepository.deleteById(id);
    }
}