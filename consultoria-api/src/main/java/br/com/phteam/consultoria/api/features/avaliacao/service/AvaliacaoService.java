package br.com.phteam.consultoria.api.features.avaliacao.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phteam.consultoria.api.features.avaliacao.dto.request.AvaliacaoCreateRequestDTO;
import br.com.phteam.consultoria.api.features.avaliacao.dto.response.AvaliacaoResponseDTO;
import br.com.phteam.consultoria.api.features.avaliacao.repository.AvaliacaoRepository;
import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.service.ConsultorService;
import br.com.phteam.consultoria.api.features.model.Avaliacao;
import br.com.phteam.consultoria.api.infrastructure.exception.RecursoNaoEncontradoException;
import br.com.phteam.consultoria.api.infrastructure.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AvaliacaoService implements AvaliacaoIService {

    private final AvaliacaoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ConsultorService consultorService;

    // =====================================================
    // CREATE
    // =====================================================
    @Override
    public AvaliacaoResponseDTO criar(AvaliacaoCreateRequestDTO dto, String emailConsultor) {

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com id: " + dto.clienteId()));

        Consultor consultor = consultorService.buscarPorEmail(emailConsultor)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Consultor não encontrado para o usuário autenticado."));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCliente(cliente);
        avaliacao.setConsultor(consultor);
        avaliacao.setPeso(dto.peso());
        avaliacao.setPercentualGordura(dto.percentualGordura());
        avaliacao.setObservacoes(dto.observacoes());

        Avaliacao salvo = repository.save(avaliacao);

        return toResponseDTO(salvo);
    }

    // =====================================================
    // READ — CLIENTE LOGADO
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorClienteEmail(String emailCliente) {
        return repository.findByClienteEmailOrderByDataAvaliacaoDesc(emailCliente)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // READ — CONSULTOR (AVALIAÇÕES FEITAS POR ELE)
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorConsultor(String emailConsultor) {

        Consultor consultor = consultorService.buscarPorEmail(emailConsultor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consultor não encontrado."));

        return repository.findByConsultorIdOrderByDataAvaliacaoDesc(consultor.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================================================
    // MAPPER INTERNO
    // =====================================================
    private AvaliacaoResponseDTO toResponseDTO(Avaliacao a) {
        return new AvaliacaoResponseDTO(
                a.getId(),
                a.getDataAvaliacao(),
                a.getPeso(),
                a.getPercentualGordura(),
                a.getObservacoes(),
                a.getCliente() != null ? a.getCliente().getId() : null,
                a.getCliente() != null ? a.getCliente().getNome() : null
        );
    }
}
