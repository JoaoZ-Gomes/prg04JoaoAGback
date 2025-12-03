package br.com.phteam.consultoria.api.consultor.service;

import br.com.phteam.consultoria.api.consultor.model.Consultor;

import java.util.List;
import java.util.Optional;

// A interface é nomeada com o prefixo 'I'
public interface ConsultorIService {

    Consultor salvar(Consultor consultor);

    List<Consultor> buscarTodos();

    Optional<Consultor> buscarPorId(Long id);

    Optional<Consultor> atualizar(Long id, Consultor dadosAtualizados);

    boolean deletarConsultor(Long id);
}