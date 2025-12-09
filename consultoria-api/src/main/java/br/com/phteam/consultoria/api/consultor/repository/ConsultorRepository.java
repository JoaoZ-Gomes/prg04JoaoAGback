package br.com.phteam.consultoria.api.consultor.repository;

import br.com.phteam.consultoria.api.consultor.model.Consultor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List; // Mantido por causa do findByEspecializacao

/**
 * Interface Repository para a entidade Consultor.
 * Estende JpaRepository para herdar os métodos CRUD, incluindo findAll(Pageable).
 */
@Repository
public interface ConsultorRepository extends JpaRepository<Consultor, Long> {

    // Método customizado para buscar um Consultor pelo número CREF (único)
    Optional<Consultor> findByNumeroCref(String numeroCref);

    // Método customizado para buscar Consultor pelo email (usado no login/autenticação)
    Optional<Consultor> findByEmail(String email);

    // Método customizado para buscar consultores por especialização
    List<Consultor> findByEspecializacao(String especializacao);
}