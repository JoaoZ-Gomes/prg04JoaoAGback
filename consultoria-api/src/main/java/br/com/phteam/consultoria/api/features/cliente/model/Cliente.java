package br.com.phteam.consultoria.api.features.cliente.model;

import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.model.Usuario;
import br.com.phteam.consultoria.api.features.treino.model.FichaTreino;
import br.com.phteam.consultoria.api.features.model.Avaliacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entidade Cliente.
 * Representa o usuário final da consultoria (que recebe treinos, avaliações e segue um consultor).
 * Herda informações básicas (id, nome, email, senha...) da classe Usuário.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Usuario {

    // ------ Atributos do Cliente ------

    @Column(name = "peso_atual")
    private double pesoAtual;

    @Column(name = "altura")
    private double altura;

    @Column(name = "objetivo", length = 50)
    private String objetivo; // Exemplo: "Hipertrofia", "Emagrecimento", etc.


    // ------ Relacionamentos ------

    /**
     * Muitos clientes podem estar associados a um único consultor.
     * A FK é armazenada na coluna consultor_id.
     * Fetch LAZY para melhorar performance (só busca o consultor quando necessário).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id")
    private Consultor consultor;

    /**
     * Um cliente pode ter várias fichas de treino.
     * A relação é controlada pelo atributo "cliente" dentro de FichaTreino.
     * orphanRemoval = true → remove fichas órfãs automaticamente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichaTreino> fichasTreino;

    /**
     * Um cliente pode ter várias avaliações físicas.
     * A relação é controlada pelo atributo "cliente" dentro de Avaliacao.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;


}
