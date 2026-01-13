package br.com.phteam.consultoria.api.infrastructure.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Serviço responsável por toda a manipulação do Token JWT para a API de Consultoria.
 */
@Service
public class JwtTokenService {


    // Tempo de validade do token: 10 horas (em milissegundos)
    // OBS: Você pode mudar para injetar do application.properties, mas mantive o valor fixo da sua fonte.
    private static final long EXPIRATION_TIME_MS = 10 * 60 * 60 * 1000;

    /**
     * Chave secreta injetada do application.properties.
     * OBS: O nome da propriedade deve ser 'api.security.token.secret' no seu properties.
     */
    @Value("${api.security.token.secret}")
    private String secretKey;

    // --- Geração do Token ---

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Validação e extração de dados do Token ---

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- Métodos auxiliares ---

    private SecretKey getSigningKey() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("A chave secreta JWT não está configurada!");
        }
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // Na versão 0.11.5, o tamanho da chave pode ser um fator. Mantenha 256 bits (32 bytes)
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}