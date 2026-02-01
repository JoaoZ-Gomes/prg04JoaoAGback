package br.com.phteam.consultoria.api.features.consultor.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultorService implements ConsultorIService {

    private final ConsultorRepository consultorRepository;

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
    public Optional<Consultor> buscarPorEmail(String email) {
        return consultorRepository.findByEmail(email);
    }

    @Override
    public Page<Consultor> buscarTodos(Pageable pageable) {
        return consultorRepository.findAll(pageable);
    }
    @Override
    public List<Cliente> buscarClientesDoConsultorPorId(Long consultorId) {
        Consultor consultor = consultorRepository.findById(consultorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Consultor não encontrado com ID: " + consultorId
                ));
        return consultor.getClientes();
    }


    // =====================================================
    // DASHBOARD DO CONSULTOR (PEGA DO TOKEN → EMAIL)
    // =====================================================

    @Override
    public List<Cliente> buscarClientesDoConsultor(String email) {

        Consultor consultor = consultorRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Consultor não encontrado com email: " + email
                        )
                );

        return consultor.getClientes();
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    @Transactional
    public Optional<Consultor> atualizar(Long id, Consultor dadosAtualizados) {

        return consultorRepository.findById(id)
                .map(consultorExistente -> {

                    // 🔒 REGRA: CREF não pode duplicar
                    if (dadosAtualizados.getNumeroCref() != null &&
                            !dadosAtualizados.getNumeroCref().equals(consultorExistente.getNumeroCref())) {

                        consultorRepository.findByNumeroCref(dadosAtualizados.getNumeroCref())
                                .filter(c -> !c.getId().equals(id))
                                .ifPresent(c -> {
                                    throw new RegraDeNegocioException("CREF já cadastrado.");
                                });

                        consultorExistente.setNumeroCref(dadosAtualizados.getNumeroCref());
                    }

                    if (dadosAtualizados.getNome() != null) {
                        consultorExistente.setNome(dadosAtualizados.getNome());
                    }

                    if (dadosAtualizados.getTelefone() != null) {
                        consultorExistente.setTelefone(dadosAtualizados.getTelefone());
                    }

                    if (dadosAtualizados.getEspecializacao() != null) {
                        consultorExistente.setEspecializacao(dadosAtualizados.getEspecializacao());
                    }

                    return consultorRepository.save(consultorExistente);
                });
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Override
    @Transactional
    public void deletarConsultor(Long id) {

        Consultor consultor = consultorRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Consultor não encontrado para exclusão com ID: " + id
                        )
                );

        if (!consultor.getClientes().isEmpty()) {
            throw new RegraDeNegocioException(
                    "Consultor possui clientes vinculados e não pode ser removido."
            );
        }

        consultorRepository.delete(consultor);
    }
}
