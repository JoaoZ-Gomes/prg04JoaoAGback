package br.com.phteam.consultoria.api.auth.service;

import br.com.phteam.consultoria.api.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final ConsultorRepository consultorRepository;

    // Em produção, o ideal é usar PasswordEncoder (BCrypt)
    // private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(ClienteRepository clienteRepository, ConsultorRepository consultorRepository) {
        this.clienteRepository = clienteRepository;
        this.consultorRepository = consultorRepository;
    }

    /**
     * Tenta autenticar um usuário (Cliente ou Consultor) e, se bem-sucedido, gera um token.
     * @param email O email fornecido pelo usuário.
     * @param senha A senha fornecida pelo usuário.
     * @return O token de autenticação (simulado).
     */
    public String autenticarEGerarToken(String email, String senha) {

        // Busca primeiro em Cliente, se não achar, busca em Consultor
        Optional<Usuario> usuario = clienteRepository.findByEmail(email)
                .map(u -> (Usuario) u)
                .or(() -> consultorRepository.findByEmail(email)
                        .map(u -> (Usuario) u));

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario user = usuario.get();

        boolean senhaCorreta = verificarSenhaSimulada(senha, user.getHashSenha());

        if (!senhaCorreta) {
            throw new RuntimeException("Senha inválida.");
        }

        // Simulação de token JWT
        return "JWT_TOKEN_PARA_" + user.getNome().toUpperCase();
    }

    // Método auxiliar para simular a verificação de senha
    private boolean verificarSenhaSimulada(String senhaDigitada, String hashArmazenado) {
        // Apenas para testes
        return senhaDigitada.equals("123456");
    }
}
