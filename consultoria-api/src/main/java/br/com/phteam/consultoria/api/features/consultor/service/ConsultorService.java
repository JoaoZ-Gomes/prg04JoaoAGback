package br.com.phteam.consultoria.api.features.consultor.service;

import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // NOVO IMPORT: Para retornar a página de dados
import org.springframework.data.domain.Pageable; // NOVO IMPORT: Para receber os parâmetros de paginação
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio da entidade Consultor.
 * Implementa a interface ConsultorIService e lança exceções personalizadas.
 */
@Service
public class ConsultorService implements ConsultorIService {

    private final ConsultorRepository consultorRepository;

    @Autowired
    public ConsultorService(ConsultorRepository consultorRepository) {
        this.consultorRepository = consultorRepository;
    }

    @Override
    @Transactional
    public Consultor salvar(Consultor consultor) {
        // Lógica de Negócio (UC-200): E-mail e CREF duplicados
        if (consultorRepository.findByEmail(consultor.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }
        if (consultorRepository.findByNumeroCref(consultor.getNumeroCref()).isPresent()) {
            throw new RegraDeNegocioException("CREF já cadastrado.");
        }

        // **A Fazer:** Aplicar criptografia na senha.
        return consultorRepository.save(consultor);
    }

    @Override
    public Optional<Consultor> buscarPorId(Long id) {
        return consultorRepository.findById(id);
    }

    @Override
    // MÉTODO ALTERADO PARA USAR PAGINAÇÃO:
    public Page<Consultor> buscarTodos(Pageable pageable) {
        // Chama o método nativo do JpaRepository que aceita Pageable
        return consultorRepository.findAll(pageable);
    }
    // OBS: O método original List<Consultor> buscarTodos() foi substituído.

    // Lógica para Atualização (UC-201)
    @Override
    @Transactional
    public Optional<Consultor> atualizar(Long id, Consultor detalhesConsultor) {
        return consultorRepository.findById(id)
                .map(consultorExistente -> {
                    // Aplica as mudanças apenas nos campos permitidos (mantendo a lógica NULL-check)
                    if (detalhesConsultor.getNome() != null) {
                        consultorExistente.setNome(detalhesConsultor.getNome());
                    }
                    if (detalhesConsultor.getTelefone() != null) {
                        consultorExistente.setTelefone(detalhesConsultor.getTelefone());
                    }
                    if (detalhesConsultor.getEspecializacao() != null) {
                        consultorExistente.setEspecializacao(detalhesConsultor.getEspecializacao());
                    }
                    // E-mail e CREF não são atualizados neste endpoint para simplificar.

                    return consultorRepository.save(consultorExistente);
                });
    }

    @Override
    @Transactional
    public boolean deletarConsultor(Long id) {
        if (!consultorRepository.existsById(id)) {
            // Lógica de Exceção: Lança 404 NOT FOUND se o recurso não existir
            throw new RecursoNaoEncontradoException("Consultor não encontrado para exclusão com ID: " + id);
        }

        // **A Fazer (Regra de Negócio):** Antes de excluir, garantir que o consultor não tem clientes ativos.
        consultorRepository.deleteById(id);
        return true;
    }
}