package br.com.phteam.consultoria.api.treino.repository;

import br.com.phteam.consultoria.api.treino.model.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    // Consulta para garantir unicidade e buscar por nome.
    Optional<Exercicio> findByNome(String nome);

    // Consulta para filtragem do catálogo por grupo muscular.
    List<Exercicio> findByGrupoMuscular(String grupoMuscular);
}