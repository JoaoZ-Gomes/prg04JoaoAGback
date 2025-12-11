package br.com.phteam.consultoria.api.features.usuario.service;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import br.com.phteam.consultoria.api.features.consultor.repository.ConsultorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de Detalhes do Usuário Customizado.
 * Implementa a interface UserDetailsService do Spring Security para carregar um usuário (Cliente ou Consultor)
 * pelo nome de usuário (que é o email) durante o processo de autenticação.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Repositórios necessários para buscar os dois tipos de usuário
    private final ClienteRepository clienteRepository;
    private final ConsultorRepository consultorRepository;

    /**
     * Método obrigatório do Spring Security. Tenta carregar o usuário pelo email.
     * * @param email O nome de usuário (email) fornecido na tentativa de login.
     * @return Um objeto UserDetails do Spring Security.
     * @throws UsernameNotFoundException Se o email não for encontrado em nenhuma das tabelas.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Tenta buscar o usuário na tabela de Clientes
        Cliente cliente = clienteRepository.findByEmail(email).orElse(null);

        if (cliente != null) {
            // Se encontrado, constrói os detalhes com a ROLE CLIENTE
            return buildUserDetails(cliente.getEmail(), cliente.getSenha(), "CLIENTE");
        }

        // 2. Se não for Cliente, tenta buscar na tabela de Consultores
        Consultor consultor = consultorRepository.findByEmail(email).orElse(null);

        if (consultor != null) {
            // Se encontrado, constrói os detalhes com a ROLE CONSULTOR
            return buildUserDetails(consultor.getEmail(), consultor.getSenha(), "CONSULTOR");
        }

        // 3. Se não encontrar em nenhuma das fontes
        throw new UsernameNotFoundException("Usuário (Cliente ou Consultor) não encontrado com o email: " + email);
    }

    /**
     * Constrói o objeto UserDetails padrão do Spring Security a partir dos dados do seu modelo.
     */
    private UserDetails buildUserDetails(String email, String senhaHash, String role) {

        // As permissões devem seguir o formato "ROLE_NOME" no Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        // O objeto UserDetails que o Spring Security irá usar
        return new User(
                email,      // Username (usamos o email)
                senhaHash,  // Senha (deve ser o hash BCrypt do banco)
                authorities // Roles (ROLE_CLIENTE ou ROLE_CONSULTOR)
        );
    }
}