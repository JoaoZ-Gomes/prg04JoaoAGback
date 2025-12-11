package br.com.phteam.consultoria.api.features.cliente.repository;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Interface Repository para a entidade Cliente.
 * <Cliente, Long> -> O primeiro parâmetro é a Entidade, o segundo é o tipo da Chave Primária (id).
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar um cliente pelo email
    Optional<Cliente> findByEmail(String email);

    // Buscar todos os clientes vinculados a um consultor (campo consultorId no Cliente)
    List<Cliente> findByConsultorId(Long consultorId);

    // Buscar clientes pelo objetivo (ex: hipertrofia, emagrecimento)
    List<Cliente> findByObjetivo(String objetivo);
}
