package br.com.phteam.consultoria.api.features.model;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que registra o progresso e as métricas do Cliente em um determinado momento.
 */
@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "data_avaliacao")
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    @Column(nullable = false)
    private double peso; // Peso registrado na avaliação

    @Column(nullable = false)
    private double percentualGordura; // % de gordura

    @Column(length = 200)
    private String observacoes;

    // ================================
    // RELACIONAMENTO COM CLIENTE
    // ================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // ================================
    // RELACIONAMENTO COM CONSULTOR
    // ================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private Consultor consultor;
}
