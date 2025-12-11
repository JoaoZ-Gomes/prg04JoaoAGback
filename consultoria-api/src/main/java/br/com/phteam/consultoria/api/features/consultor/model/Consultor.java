package br.com.phteam.consultoria.api.features.consultor.model;

import br.com.phteam.consultoria.api.features.model.Usuario; // Classe base
import br.com.phteam.consultoria.api.features.cliente.model.Cliente; // Para o relacionamento com Cliente
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entidade que representa o Consultor/Personal Trainer.
 * Herda dados de autenticação de Usuario.
 */
@Entity
@Table(name = "consultores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consultor extends Usuario {

    @Column(name = "numero_cref", nullable = false, unique = true, length = 15)
    private String numeroCref;

    @Column(name = "especializacao", length = 50)
    private String especializacao;

    // Relacionamento: Um Consultor gerencia Muitos Clientes (One-to-Many)
    // mappedBy = "consultor": Indica que a chave estrangeira está na tabela 'clientes'
    @OneToMany(mappedBy = "consultor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cliente> clientes;
}