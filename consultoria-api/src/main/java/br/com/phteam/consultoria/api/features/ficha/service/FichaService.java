package br.com.phteam.consultoria.api.features.ficha.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.features.ficha.dto.request.FichaAtualizarRequestDTO;
import br.com.phteam.consultoria.api.features.ficha.dto.request.FichaCriarRequestDTO;
import br.com.phteam.consultoria.api.features.ficha.dto.response.FichaResponseDTO;
import br.com.phteam.consultoria.api.features.ficha.mapper.FichaMapper;
import br.com.phteam.consultoria.api.features.ficha.model.Ficha;
import br.com.phteam.consultoria.api.features.ficha.repository.FichaRepository;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FichaService implements FichaIService {

    private final FichaRepository repository;
    private final FichaMapper mapper;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public FichaResponseDTO save(FichaCriarRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + dto.clienteId()));

        Ficha entity = mapper.toEntity(dto, cliente);
        
        // Também atualizar o objetivo do cliente quando criar a ficha
        if (dto.objetivo() != null) {
            try {
                br.com.phteam.consultoria.api.features.cliente.model.ObjetivoCliente objetivoEnum = 
                    br.com.phteam.consultoria.api.features.cliente.model.ObjetivoCliente.valueOf(dto.objetivo());
                cliente.setObjetivo(objetivoEnum);
                clienteRepository.save(cliente);
                System.out.println("Objetivo do cliente definido para: " + objetivoEnum);
            } catch (IllegalArgumentException e) {
                System.err.println("Aviso: Valor de objetivo inválido para enum: " + dto.objetivo());
            }
        }
        
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public List<FichaResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<FichaResponseDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public FichaResponseDTO findById(Long id) {
        Ficha f = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ficha não encontrada com ID: " + id));
        return mapper.toResponse(f);
    }

    @Override
    @Transactional
    public FichaResponseDTO update(FichaAtualizarRequestDTO dto) {
        System.out.println("=== ATUALIZANDO FICHA ===");
        System.out.println("ID: " + dto.id());
        System.out.println("Nome: " + dto.nome());
        System.out.println("Objetivo: " + dto.objetivo());
        
        Ficha f = repository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ficha não encontrada com ID: " + dto.id()));

        System.out.println("Ficha encontrada: " + f.getId());
        
        if (dto.nome() != null) f.setNome(dto.nome());
        if (dto.objetivo() != null) {
            f.setObjetivo(dto.objetivo());
            
            // Também atualizar o objetivo do cliente
            if (f.getCliente() != null) {
                try {
                    // Converter a String do objetivo para o Enum ObjetivoCliente
                    br.com.phteam.consultoria.api.features.cliente.model.ObjetivoCliente objetivoEnum = 
                        br.com.phteam.consultoria.api.features.cliente.model.ObjetivoCliente.valueOf(dto.objetivo());
                    f.getCliente().setObjetivo(objetivoEnum);
                    clienteRepository.save(f.getCliente());
                    System.out.println("Objetivo do cliente atualizado para: " + objetivoEnum);
                } catch (IllegalArgumentException e) {
                    System.err.println("Aviso: Valor de objetivo inválido para enum: " + dto.objetivo());
                }
            }
        }

        System.out.println("Ficha antes de salvar - Objetivo: " + f.getObjetivo());
        
        Ficha salva = repository.save(f);
        
        System.out.println("Ficha salva - Objetivo: " + salva.getObjetivo());
        System.out.println("=== FIM ATUALIZAÇÃO ===");

        return mapper.toResponse(salva);
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Ficha não encontrada com ID: " + id);
        }

        repository.deleteById(id);
        return id;
    }
}
