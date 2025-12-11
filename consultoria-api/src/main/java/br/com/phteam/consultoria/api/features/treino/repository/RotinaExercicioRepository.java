package br.com.phteam.consultoria.api.features.treino.repository;

import br.com.phteam.consultoria.api.features.treino.model.RotinaExercicio;
import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotinaExercicioRepository extends JpaRepository<RotinaExercicio, Long> {

    // Recupera todas as rotinas pertencentes a uma Sessão de Treino específica - AGORA PAGINADA
    Page<RotinaExercicio> findBySessaoTreinoId(Long sessaoTreinoId, Pageable pageable); // MÉTODO MODIFICADO

    // O método findAll(Pageable) já está disponível via JpaRepository
}