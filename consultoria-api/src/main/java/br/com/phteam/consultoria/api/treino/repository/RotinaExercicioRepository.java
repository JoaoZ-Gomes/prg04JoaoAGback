package br.com.phteam.consultoria.api.treino.repository;

import br.com.phteam.consultoria.api.treino.model.RotinaExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotinaExercicioRepository extends JpaRepository<RotinaExercicio, Long> {

    // Recupera todas as rotinas pertencentes a uma Sessão de Treino específica.
    List<RotinaExercicio> findBySessaoTreinoId(Long sessaoTreinoId);
}