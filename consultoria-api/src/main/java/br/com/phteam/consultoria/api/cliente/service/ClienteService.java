package br.com.phteam.consultoria.api.cliente.service;

import br.com.phteam.consultoria.api.cliente.model.Cliente;
import br.com.phteam.consultoria.api.cliente.repository.ClienteRepository;

import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class ClienteService implements ClienteIService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional // Garante a atomicidade da operação de salvamento
    public Cliente salvar(Cliente cliente) {
        // Lógica de Negócio: Verificar se o email já existe (lança 400 Bad Request)
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro cliente.");
        }

        // **A Fazer:** Aplicar a criptografia na senha.
        return clienteRepository.save(cliente);
    }

    @Override
    // Operações de leitura podem ser deixadas sem @Transactional, ou marcadas como read-only
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Busca todos os clientes de forma paginada.
     * @param pageable Objeto Pageable contendo informações de página, tamanho e ordenação.
     * @return Uma página (Page) de clientes.
     */
    @Override
    public Page<Cliente> buscarTodos(Pageable pageable) {
        // O JpaRepository fornece a implementação nativa de findAll(Pageable)
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional // Garante a atomicidade da operação de exclusão
    public void excluirPorId(Long id) {
        // Lógica de Exceção: Verifica a existência antes de deletar (lança 404 Not Found)
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id);
        }

        // **A Fazer (Lógica de Negócio):** Garantir que o cliente não tem vínculos ativos.
        clienteRepository.deleteById(id);
    }

    // Lógica para Atualização Parcial
    @Override
    @Transactional // Garante a atomicidade da operação de atualização
    public Optional<Cliente> atualizar(Long id, Cliente detalhesCliente) {

        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    // Campos que são Objetos (String, Double - Wrapper) - OK para != null
                    if (detalhesCliente.getNome() != null) {
                        clienteExistente.setNome(detalhesCliente.getNome());
                    }
                    if (detalhesCliente.getTelefone() != null) {
                        clienteExistente.setTelefone(detalhesCliente.getTelefone());
                    }
                    if (detalhesCliente.getObjetivo() != null) {
                        clienteExistente.setObjetivo(detalhesCliente.getObjetivo());
                    }


                    if (detalhesCliente.getPesoAtual() > 0.0) {
                        clienteExistente.setPesoAtual(detalhesCliente.getPesoAtual());
                    }

                    if (detalhesCliente.getAltura() > 0.0) {
                        clienteExistente.setAltura(detalhesCliente.getAltura());
                    }

                    return clienteRepository.save(clienteExistente);
                });

    }
}