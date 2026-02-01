package br.com.phteam.consultoria.api.features.cliente.service;

import br.com.phteam.consultoria.api.features.cliente.dto.ClienteRequestDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.ClienteResponseDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.ClienteUpdateDTO;
import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteIService {

    ClienteResponseDTO salvar(ClienteRequestDTO dto);

    ClienteResponseDTO buscarPorId(Long id);

    Page<ClienteResponseDTO> buscarTodos(Pageable pageable);

    ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO dto);

    void excluirPorId(Long id);

    ClienteResponseDTO buscarPorEmail(String email);
}

