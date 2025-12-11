package br.com.phteam.consultoria.api.features.treino.repository;

import br.com.phteam.consultoria.api.features.treino.model.Exercicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    // Consulta para garantir unicidade e buscar por nome.
    Optional<Exercicio> findByNome(String nome);

    // Consulta para filtragem do catálogo por grupo muscular - AGORA PAGINADA
    Page<Exercicio> findByGrupoMuscular(String grupoMuscular, Pageable pageable); // MÉTODO MODIFICADO
}