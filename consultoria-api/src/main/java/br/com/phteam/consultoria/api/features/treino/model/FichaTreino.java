package br.com.phteam.consultoria.api.features.treino.model;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente; // Relacionamento com Cliente
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa uma Ficha de Treino atribuída a um cliente.
 */
@Entity
@Table(name = "fichas_treino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FichaTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, name = "data_inicio")
    private LocalDate dataInicio;

    @Column(nullable = false, name = "data_fim")
    private LocalDate dataFim;

    @Column(length = 500)
    private String observacoes;

    // Relacionamento: Muitas Fichas pertencem a Um Cliente (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relacionamento: Uma Ficha tem Muitas Sessões (Treino A, Treino B, etc.)
    @OneToMany(mappedBy = "fichaTreino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessaoTreino> sessoes;

    // Relacionamento: Uma Ficha tem Muitas Sessões (Treino A, Treino B, etc.)
    // Mapeamento que será resolvido quando criarmos a SessaoTreino.java
    // @OneToMany(mappedBy = "fichaTreino", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<SessaoTreino> sessoes;
}