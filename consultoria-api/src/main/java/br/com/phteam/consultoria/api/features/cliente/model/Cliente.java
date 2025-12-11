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
 * Entidade que representa o Cliente/Usuário final da consultoria.
 * Herda dados de autenticação e contato da classe base Usuario.
 */
@Entity // Marca esta classe como uma entidade JPA
@Table(name = "clientes") // Define o nome da tabela no banco de dados
@Getter // Gera automaticamente os métodos getters para todos os campos (Lombok)
@Setter // Gera automaticamente os métodos setters para todos os campos (Lombok)
@NoArgsConstructor // Gera um construtor sem argumentos (Lombok)
@AllArgsConstructor // Gera um construtor com todos os argumentos (Lombok)
public class Cliente extends Usuario { // Herda campos como id, nome, email, hashSenha, telefone

    @Column(name = "peso_atual") // Nome da coluna no banco de dados
    private double pesoAtual;

    @Column(name = "altura")
    private double altura;

    @Column(name = "objetivo", length = 50)
    private String objetivo; // Ex: Hipertrofia, Emagrecimento, etc.

    // ================== RELACIONAMENTOS JPA ==================

    // Relacionamento: Muitos Clientes podem ter Um Consultor (Many-to-One)
    // fetch = FetchType.LAZY: Carrega o Consultor apenas quando necessário (otimização)
    // @JoinColumn: Define a chave estrangeira na tabela 'clientes' que referencia 'consultores'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id") // Nome da coluna FK no DB (ex: consultor_id)
    private Consultor consultor;

    // Relacionamento: Um Cliente pode ter Muitas Fichas de Treino (One-to-Many)
    // mappedBy = "cliente": Indica que o relacionamento é gerenciado pelo campo 'cliente' em FichaTreino
    // cascade = CascadeType.ALL: Operações (salvar, excluir) em Cliente afetam FichaTreino
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichaTreino> fichasTreino;

    // Relacionamento: Um Cliente pode ter Muitas Avaliações (One-to-Many)
    // mappedBy = "cliente": Indica que o relacionamento é gerenciado pelo campo 'cliente' em Avaliacao
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    // Nota: O relacionamento com a Assinatura (Subscription) é 1:1, mas foi omitido
    // para simplificar o modelo inicial.
}