package br.com.phteam.consultoria.api.features.treino.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidade que define as séries, repetições e carga para um Exercicio dentro de uma SessaoTreino.
 * É o detalhe da prescrição do treino.
 */
@Entity
@Table(name = "rotinas_exercicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RotinaExercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false, length = 20)
    private String repeticoes;

    @Column(name = "tempo_descanso", length = 20)
    private String tempoDescanso;

    @Column(length = 200)
    private String observacoes;

    // Relacionamento 1: Muitas rotinas usam o mesmo Exercicio do catálogo (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;

    // Relacionamento 2: Muitas rotinas pertencem a Uma Sessão (Many-to-One)
    // Assume que a entidade SessaoTreino já existe.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_treino_id", nullable = false)
    private SessaoTreino sessaoTreino;
}