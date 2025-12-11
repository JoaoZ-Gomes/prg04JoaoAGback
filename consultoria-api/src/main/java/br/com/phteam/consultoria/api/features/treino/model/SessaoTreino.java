package br.com.phteam.consultoria.api.features.treino.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidade que representa uma Sessão de Treino específica (ex: 'Peito e Tríceps').
 *  FichaTreino -> SessaoTreino -> RotinaExercicio.
 */
@Entity
@Table(name = "sessoes_treino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeSessao; // Ex: "Treino A - Inferiores" ou "Dia 1"

    @Column(length = 30)
    private String diaDaSemana; // Ex: "Segunda", "Quarta" (Opcional)

    @Column(length = 500)
    private String observacoes;

    // Relacionamento: Muitas Sessões pertencem a Uma Ficha de Treino (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_treino_id", nullable = false)
    private FichaTreino fichaTreino;

    // Relacionamento: Uma Sessão possui Muitas Rotinas de Exercício
    // Mapeamento que será resolvido quando criarmos a RotinaExercicio.java
    // @OneToMany(mappedBy = "sessaoTreino", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<RotinaExercicio> rotinas;
}