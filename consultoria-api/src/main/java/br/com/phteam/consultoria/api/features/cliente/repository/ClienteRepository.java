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

    // Método customizado para buscar um cliente pelo email (útil para login e Auth Service)
    Optional<Cliente> findByEmail(String email);

    // Método customizado para buscar todos os clientes de um consultor específico (relacionamento)
    List<Cliente> findByConsultorId(Long consultorId);

    // Método customizado para buscar clientes por objetivo (e.g., "Hipertrofia")
    List<Cliente> findByObjetivo(String objetivo);
}