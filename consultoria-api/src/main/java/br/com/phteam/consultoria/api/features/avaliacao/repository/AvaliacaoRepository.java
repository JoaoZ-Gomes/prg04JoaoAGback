package br.com.phteam.consultoria.api.features.avaliacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.phteam.consultoria.api.features.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByClienteEmailOrderByDataAvaliacaoDesc(String email);

    List<Avaliacao> findByClienteIdOrderByDataAvaliacaoDesc(Long clienteId);

    List<Avaliacao> findByClienteIdInOrderByDataAvaliacaoDesc(List<Long> clienteIds);

    List<Avaliacao> findByConsultorIdOrderByDataAvaliacaoDesc(Long consultorId);
}
