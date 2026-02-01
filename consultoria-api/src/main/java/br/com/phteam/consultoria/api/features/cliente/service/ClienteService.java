package br.com.phteam.consultoria.api.features.cliente.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phteam.consultoria.api.features.cliente.dto.request.ClienteRequestDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.request.ClienteUpdateDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.response.ClienteResponseDTO;
import br.com.phteam.consultoria.api.features.cliente.mapper.ClienteMapper;
import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService implements ClienteIService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {

        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }

        Cliente cliente = mapper.toEntity(dto);
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        return mapper.toResponseDTO(repository.save(cliente));
    }



    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));
        return mapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> buscarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));

        mapper.updateEntityFromDTO(dto, cliente);

        return mapper.toResponseDTO(repository.save(cliente));
    }

    @Override
    public void excluirPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorEmail(String email) {
        Cliente cliente = repository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para email: " + email));
        return mapper.toResponseDTO(cliente);
    }
}
