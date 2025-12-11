package br.com.phteam.consultoria.api.features.cliente.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;

import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT

import java.util.Optional;


public interface ClienteIService {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);


    Page<Cliente> buscarTodos(Pageable pageable);

    void excluirPorId(Long id);

    Optional<Cliente> atualizar(Long id, Cliente detalhesCliente);
}