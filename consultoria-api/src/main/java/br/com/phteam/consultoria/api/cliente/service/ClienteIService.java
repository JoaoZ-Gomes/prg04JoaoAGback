package br.com.phteam.consultoria.api.cliente.service;

import br.com.phteam.consultoria.api.cliente.model.Cliente;
import java.util.List;
import java.util.Optional;

// A interface é nomeada com o prefixo 'I'
public interface ClienteIService {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> buscarTodos();

    void excluirPorId(Long id);

    Optional<Cliente> atualizar(Long id, Cliente detalhesCliente);
}