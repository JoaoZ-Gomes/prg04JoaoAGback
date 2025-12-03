package br.com.phteam.consultoria.api.cliente.service;

import br.com.phteam.consultoria.api.cliente.model.Cliente;
import br.com.phteam.consultoria.api.cliente.repository.ClienteRepository;
// Importando as exceções personalizadas para o tratamento padronizado de erros
import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService implements ClienteIService { // Implementa a interface ClienteIService

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        // Lógica de Negócio: Verificar se o email já existe (lança 400 Bad Request)
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro cliente.");
        }

        // **A Fazer:** Aplicar a criptografia na senha.
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
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
        // O Controller lida com o Optional vazio lançando RecursoNaoEncontradoException.
    }
}