package br.com.phteam.consultoria.api.consultor.service;

import br.com.phteam.consultoria.api.consultor.model.Consultor;
import br.com.phteam.consultoria.api.consultor.repository.ConsultorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio da entidade Consultor.
 * Implementa as regras de negócio para os Casos de Uso UC-200 a UC-205.
 */
@Service
public class ConsultorService {

    private final ConsultorRepository consultorRepository;

    @Autowired
    public ConsultorService(ConsultorRepository consultorRepository) {
        this.consultorRepository = consultorRepository;
    }

    // Cria ou atualiza um consultor
    public Consultor salvar(Consultor consultor) {
        // **Lógica de Negócio:**
        // 1. Verificar se o numeroCref já está cadastrado.
        // 2. Aplicar criptografia na senha.
        return consultorRepository.save(consultor);
    }

    public Optional<Consultor> buscarPorId(Long id) {
        return consultorRepository.findById(id);
    }

    public List<Consultor> buscarTodos() {
        return consultorRepository.findAll();
    }

    public void excluirPorId(Long id) {
        // **Lógica de Negócio:** Antes de excluir, garantir que o consultor não tem clientes ativos.
        consultorRepository.deleteById(id);
    }

    // Lógica para Atualização (UC-201)
    public Optional<Consultor> atualizar(Long id, Consultor detalhesConsultor) {

        return consultorRepository.findById(id)
                .map(consultorExistente -> {

                    // Aplica as mudanças nos campos
                    if (detalhesConsultor.getNome() != null) {
                        consultorExistente.setNome(detalhesConsultor.getNome());
                    }
                    if (detalhesConsultor.getTelefone() != null) {
                        consultorExistente.setTelefone(detalhesConsultor.getTelefone());
                    }
                    if (detalhesConsultor.getEspecializacao() != null) {
                        consultorExistente.setEspecializacao(detalhesConsultor.getEspecializacao());
                    }

                    // O número CREF geralmente não é alterado.

                    return consultorRepository.save(consultorExistente);
                });
    }
}