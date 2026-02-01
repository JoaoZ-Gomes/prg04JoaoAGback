package br.com.phteam.consultoria.api.features.cliente.mapper;

import br.com.phteam.consultoria.api.features.cliente.dto.ClienteRequestDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.ClienteResponseDTO;
import br.com.phteam.consultoria.api.features.cliente.dto.ClienteUpdateDTO;
import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());
        cliente.setCpf(dto.getCpf());
        cliente.setRg(dto.getRg());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setTelefone(dto.getTelefone());
        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setCpf(cliente.getCpf());
        dto.setRg(cliente.getRg());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setTelefone(cliente.getTelefone());
        dto.setPesoAtual(cliente.getPesoAtual());
        dto.setAltura(cliente.getAltura());
        return dto;
    }

    public void updateEntityFromDTO(ClienteUpdateDTO dto, Cliente cliente) {
        if (dto.getPesoAtual() != null) cliente.setPesoAtual(dto.getPesoAtual());
        if (dto.getAltura() != null) cliente.setAltura(dto.getAltura());
    }
}
