package br.com.phteam.consultoria.api.cliente.service;

import br.com.phteam.consultoria.api.cliente.model.Cliente;
import br.com.phteam.consultoria.api.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio da entidade Cliente.
 * Implementa as regras de negócio para os Casos de Uso UC-100 a UC-105 e UC-201.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    // Opcionalmente, pode ser injetado o ConsultorService aqui se houver lógica de atribuição.

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Cria ou atualiza um cliente (USO INTERNO ou PELO CONSULTOR)
    public Cliente salvar(Cliente cliente) {
        // **Lógica de Negócio:**
        // 1. Verificar se o email já existe (se for uma nova criação).
        // 2. Aplicar a criptografia na senha (se for uma nova criação ou alteração de senha).
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    // Método para Excluir
    public void excluirPorId(Long id) {
        clienteRepository.deleteById(id);
    }

    // Lógica para Atualização Parcial (UC-201 / UC-103)
    public Optional<Cliente> atualizar(Long id, Cliente detalhesCliente) {

        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    // Aplica as mudanças nos campos que NÃO são nulos (protegendo o ID, FKs, etc.)
                    if (detalhesCliente.getNome() != null) {
                        clienteExistente.setNome(detalhesCliente.getNome());
                    }
                    if (detalhesCliente.getTelefone() != null) {
                        clienteExistente.setTelefone(detalhesCliente.getTelefone());
                    }
                    if (detalhesCliente.getObjetivo() != null) {
                        clienteExistente.setObjetivo(detalhesCliente.getObjetivo());
                    }

                    // Métrica de Peso e Altura (Atualizadas geralmente pelo cliente ou consultor)
                    if (detalhesCliente.getPesoAtual() > 0) {
                        clienteExistente.setPesoAtual(detalhesCliente.getPesoAtual());
                    }
                    if (detalhesCliente.getAltura() > 0) {
                        clienteExistente.setAltura(detalhesCliente.getAltura());
                    }

                    // A lógica de atualização de senha deve ser separada e mais segura!

                    return clienteRepository.save(clienteExistente);
                });
    }
}