package br.com.phteam.consultoria.api.consultor.service;

import br.com.phteam.consultoria.api.consultor.model.Consultor;
import br.com.phteam.consultoria.api.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Consultor salvar(Consultor consultor) {
        // Lógica de Negócio (UC-200): E-mail e CREF duplicados
        if (consultorRepository.findByEmail(consultor.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado."); // Lança 400
        }
        if (consultorRepository.findByNumeroCref(consultor.getNumeroCref()).isPresent()) {
            throw new RegraDeNegocioException("CREF já cadastrado."); // Lança 400
        }

        // **A Fazer:** Aplicar criptografia na senha.
        return consultorRepository.save(consultor);
    }

    @Override
    public Optional<Consultor> buscarPorId(Long id) {
        return consultorRepository.findById(id);
    }

    @Override
    public List<Consultor> buscarTodos() {
        return consultorRepository.findAll();
    }

    // Lógica para Atualização (UC-201)
    @Override
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
        // O Controller lida com o Optional vazio lançando a RecursoNaoEncontradoException (404).
    }

    @Override
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