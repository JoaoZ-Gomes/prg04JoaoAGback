package br.com.phteam.consultoria.api.features.cliente.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClienteIService {

    // CREATE
    Cliente salvar(Cliente cliente);

    // READ
    Optional<Cliente> buscarPorId(Long id);

    Page<Cliente> buscarTodos(Pageable pageable);

    // UPDATE
    Optional<Cliente> atualizar(Long id, Cliente detalhesCliente);

    // DELETE
    void excluirPorId(Long id);
}
