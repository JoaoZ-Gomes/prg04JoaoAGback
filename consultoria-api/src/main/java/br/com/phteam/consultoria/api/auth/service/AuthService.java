package br.com.phteam.consultoria.api.auth.service;

import br.com.phteam.consultoria.api.auth.dto.LoginRequestDTO;
import br.com.phteam.consultoria.api.auth.dto.LoginSuccessDTO;
import br.com.phteam.consultoria.api.cliente.repository.ClienteRepository;
import br.com.phteam.consultoria.api.consultor.repository.ConsultorRepository;
import br.com.phteam.consultoria.api.exception.RegraDeNegocioException;
import br.com.phteam.consultoria.api.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements AuthIService {

    private final ClienteRepository clienteRepository;
    private final ConsultorRepository consultorRepository;

    @Autowired
    public AuthService(ClienteRepository clienteRepository,
                       ConsultorRepository consultorRepository) {
        this.clienteRepository = clienteRepository;
        this.consultorRepository = consultorRepository;
    }

    @Override
    public LoginSuccessDTO autenticar(LoginRequestDTO request) {

        // 1. Tenta encontrar o usuário como Cliente
        Optional<Usuario> usuarioOptional = clienteRepository.findByEmail(request.getEmail())
                .map(cliente -> (Usuario) cliente);

        // 2. CORREÇÃO: Usando !isPresent() ao invés de isEmpty() para compatibilidade com Java 8
        if (!usuarioOptional.isPresent()) {
            usuarioOptional = consultorRepository.findByEmail(request.getEmail())
                    .map(consultor -> (Usuario) consultor);
        }

        Usuario usuario = usuarioOptional.orElseThrow(() ->
                new RegraDeNegocioException("Credenciais inválidas: Usuário não encontrado ou e-mail incorreto."));

        // 3. Validação de Senha em Texto Puro (TEMPORÁRIA!)
        if (!usuario.getSenha().equals(request.getSenha())) {
            throw new RegraDeNegocioException("Credenciais inválidas: Senha incorreta.");
        }

        String tipo = usuario.getClass().getSimpleName();

        // 4. Retorna o DTO de Sucesso
        return new LoginSuccessDTO("Autenticado com sucesso.", usuario.getEmail(), tipo);
    }
}