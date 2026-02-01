package br.com.phteam.consultoria.api.features.cliente.repository;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.model.ObjetivoCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository da entidade Cliente.
 * Responsável apenas por acesso a dados — nenhuma regra de negócio aqui.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    List<Cliente> findByConsultorId(Long consultorId);

    List<Cliente> findByObjetivo(String objetivo);
}
