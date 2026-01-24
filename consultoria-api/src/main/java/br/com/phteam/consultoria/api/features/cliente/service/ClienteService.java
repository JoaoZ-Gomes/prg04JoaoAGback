package br.com.phteam.consultoria.api.features.cliente.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService implements ClienteIService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClienteService(
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------- CREATE --------------------
    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {

        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro cliente.");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    // -------------------- READ --------------------
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> buscarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    // -------------------- UPDATE --------------------
    @Override
    @Transactional
    public Optional<Cliente> atualizar(Long id, Cliente detalhesCliente) {

        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    // ✔ Strings
                    if (detalhesCliente.getNome() != null) {
                        clienteExistente.setNome(detalhesCliente.getNome());
                    }

                    if (detalhesCliente.getTelefone() != null) {
                        clienteExistente.setTelefone(detalhesCliente.getTelefone());
                    }

                    if (detalhesCliente.getObjetivo() != null) {
                        clienteExistente.setObjetivo(detalhesCliente.getObjetivo());
                    }

                    // ✔ Numéricos
                    if (detalhesCliente.getPesoAtual() > 0) {
                        clienteExistente.setPesoAtual(detalhesCliente.getPesoAtual());
                    }

                    if (detalhesCliente.getAltura() > 0) {
                        clienteExistente.setAltura(detalhesCliente.getAltura());
                    }

                    return clienteRepository.save(clienteExistente);
                });
    }


    // -------------------- DELETE --------------------
    @Override
    @Transactional
    public void excluirPorId(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Cliente não encontrado com ID: " + id
            );
        }

        clienteRepository.deleteById(id);
    }
}
