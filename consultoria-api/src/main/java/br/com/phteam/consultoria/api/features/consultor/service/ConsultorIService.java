package br.com.phteam.consultoria.api.features.consultor.service;

import br.com.phteam.consultoria.api.features.consultor.model.Consultor;

import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT

import java.util.Optional;


public interface ConsultorIService {

    Consultor salvar(Consultor consultor);

    // MÉTODO MODIFICADO: Agora aceita Pageable e retorna Page
    Page<Consultor> buscarTodos(Pageable pageable);

    Optional<Consultor> buscarPorId(Long id);

    Optional<Consultor> atualizar(Long id, Consultor dadosAtualizados);

    boolean deletarConsultor(Long id);
}