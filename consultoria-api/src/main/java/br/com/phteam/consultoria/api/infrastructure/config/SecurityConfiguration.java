package br.com.phteam.consultoria.api.infrastructure.config;

import br.com.phteam.consultoria.api.infrastructure.auth.jwt.JwtAuthorizarionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração central de segurança da aplicação.
 *
 * Responsabilidades:
 * - Definir quais rotas são públicas e protegidas
 * - Configurar autenticação via JWT
 * - Garantir política STATELESS
 * - Integrar o filtro de autorização JWT
 *
 * Observação:
 * A configuração de CORS está definida separadamente via WebMvcConfigurer.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    /**
     * Filtro responsável por interceptar requisições
     * e validar o JWT enviado no header Authorization.
     */
    private final JwtAuthorizarionFilter jwtAuthorizarionFilter;

    /**
     * Define a cadeia de filtros de segurança da aplicação.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // --------------------------------------------------
                // Desabilita CSRF (necessário para APIs REST com JWT)
                // --------------------------------------------------
                .csrf(AbstractHttpConfigurer::disable)

                // --------------------------------------------------
                // Define regras de autorização por endpoint
                // --------------------------------------------------
                .authorizeHttpRequests(authorize -> authorize

                        // -------- ROTAS PÚBLICAS --------

                        // Autenticação (login, refresh token, etc)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Cadastro de cliente (SEM JWT)
                        .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()

                        // Console do H2
                        .requestMatchers("/h2-console/**").permitAll()

                        // -------- ROTAS PROTEGIDAS --------
                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )

                // --------------------------------------------------
                // Configuração de sessão STATELESS (JWT)
                // --------------------------------------------------
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // --------------------------------------------------
                // Adiciona o filtro JWT antes do filtro padrão
                // --------------------------------------------------
                .addFilterBefore(
                        jwtAuthorizarionFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * AuthenticationManager usado no fluxo de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Encoder de senha padrão da aplicação.
     * BCrypt é seguro e recomendado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
