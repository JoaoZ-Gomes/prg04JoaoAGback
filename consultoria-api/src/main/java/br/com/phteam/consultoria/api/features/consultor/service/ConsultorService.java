package br.com.phteam.consultoria.api.features.consultor.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultorService implements ConsultorIService {

    private final ConsultorRepository consultorRepository;

    @Autowired
    public ConsultorService(ConsultorRepository consultorRepository) {
        this.consultorRepository = consultorRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    @Transactional
    public Consultor salvar(Consultor consultor) {

        if (consultorRepository.findByEmail(consultor.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }

        if (consultorRepository.findByNumeroCref(consultor.getNumeroCref()).isPresent()) {
            throw new RegraDeNegocioException("CREF já cadastrado.");
        }

        return consultorRepository.save(consultor);
    }

    // =====================================================
    // READ
    // =====================================================

    @Override
    public Optional<Consultor> buscarPorId(Long id) {
        return consultorRepository.findById(id);
    }

    @Override
    public Page<Consultor> buscarTodos(Pageable pageable) {
        return consultorRepository.findAll(pageable);
    }

    // =====================================================
    // DASHBOARD DO CONSULTOR
    // =====================================================

    /**
     * Retorna os clientes vinculados ao consultor logado (via email do JWT)
     */
    public List<Cliente> buscarClientesDoConsultor(String emailConsultor) {

        Consultor consultor = consultorRepository.findByEmail(emailConsultor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Consultor não encontrado com email: " + emailConsultor
                        )
                );

        return consultor.getClientes();
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    @Transactional
    public Optional<Consultor> atualizar(Long id, Consultor detalhesConsultor) {

        return consultorRepository.findById(id)
                .map(consultorExistente -> {

                    if (detalhesConsultor.getNome() != null) {
                        consultorExistente.setNome(detalhesConsultor.getNome());
                    }

                    if (detalhesConsultor.getTelefone() != null) {
                        consultorExistente.setTelefone(detalhesConsultor.getTelefone());
                    }

                    if (detalhesConsultor.getEspecializacao() != null) {
                        consultorExistente.setEspecializacao(detalhesConsultor.getEspecializacao());
                    }

                    return consultorRepository.save(consultorExistente);
                });
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Override
    @Transactional
    public boolean deletarConsultor(Long id) {

        if (!consultorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Consultor não encontrado para exclusão com ID: " + id
            );
        }

        consultorRepository.deleteById(id);
        return true;
    }
}
