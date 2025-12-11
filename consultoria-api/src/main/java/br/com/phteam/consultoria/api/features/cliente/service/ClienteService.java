package br.com.phteam.consultoria.api.features.cliente.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder; // NOVO: Importação do PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio da entidade Cliente.
 * Gerencia operações CRUD e validações específicas de Clientes.
 */
@Service
public class ClienteService implements ClienteIService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder; // ADICIONADO: Injeção do componente de criptografia

    /**
     * Construtor que injeta as dependências obrigatórias: Repository e PasswordEncoder.
     */
    @Autowired
    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um novo cliente no banco de dados, aplicando regras de negócio e criptografia.
     * * @param cliente A entidade Cliente a ser salva.
     * @return O Cliente persistido.
     * @throws RegraDeNegocioException Se o e-mail já estiver cadastrado.
     */
    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        // Lógica de Negócio: Impedir cadastro com e-mail duplicado
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro cliente.");
        }

        // AÇÃO ESSENCIAL PARA O JWT/SPRING SECURITY:
        // Criptografa a senha usando o BCryptPasswordEncoder antes de salvar.
        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        return clienteRepository.save(cliente);
    }

    /**
     * Busca um cliente pelo ID.
     * * @param id O ID do cliente.
     * @return Um Optional contendo o cliente, se encontrado.
     */
    @Override
    @Transactional(readOnly = true) // Otimização para operações de leitura
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Busca todos os clientes de forma paginada.
     * * @param pageable Objeto Pageable contendo informações de página, tamanho e ordenação.
     * @return Uma página (Page) de clientes.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> buscarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    /**
     * Exclui um cliente pelo ID, após verificar sua existência.
     * * @param id O ID do cliente a ser excluído.
     * @throws RecursoNaoEncontradoException Se o cliente não existir.
     */
    @Override
    @Transactional
    public void excluirPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id);
        }

        // TODO (Lógica de Negócio): Implementar verificação de vínculos ativos antes da exclusão.
        clienteRepository.deleteById(id);
    }

    /**
     * Atualiza um cliente existente com os detalhes fornecidos (atualização parcial/PATCH).
     * Campos nulos nos detalhesCliente são ignorados.
     * * @param id O ID do cliente a ser atualizado.
     * @param detalhesCliente O objeto com os dados de atualização.
     * @return Um Optional contendo o cliente atualizado, se encontrado.
     */
    @Override
    @Transactional
    public Optional<Cliente> atualizar(Long id, Cliente detalhesCliente) {

        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    // Atualiza campos de string e objetos
                    if (detalhesCliente.getNome() != null) {
                        clienteExistente.setNome(detalhesCliente.getNome());
                    }
                    if (detalhesCliente.getTelefone() != null) {
                        clienteExistente.setTelefone(detalhesCliente.getTelefone());
                    }
                    if (detalhesCliente.getObjetivo() != null) {
                        clienteExistente.setObjetivo(detalhesCliente.getObjetivo());
                    }

                    // Atualiza campos numéricos com validação simples (assumindo que 0.0 é um valor default ou inválido)
                    if (detalhesCliente.getPesoAtual() > 0.0) {
                        clienteExistente.setPesoAtual(detalhesCliente.getPesoAtual());
                    }

                    if (detalhesCliente.getAltura() > 0.0) {
                        clienteExistente.setAltura(detalhesCliente.getAltura());
                    }

                    // OBS: Senha não deve ser atualizada aqui. Deve-se usar um endpoint /reset-password dedicado.

                    return clienteRepository.save(clienteExistente);
                });

    }
}