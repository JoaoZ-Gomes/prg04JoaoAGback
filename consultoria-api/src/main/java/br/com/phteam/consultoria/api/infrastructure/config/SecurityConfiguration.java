package br.com.phteam.consultoria.api.infrastructure.config;

import br.com.phteam.consultoria.api.infrastructure.auth.jwt.JwtAuthorizarionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthorizarionFilter jwtAuthorizarionFilter;

    /**
     * Define o SecurityFilterChain que configura as regras de segurança e o JWT.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. **CONFIGURAÇÃO CORS PARA SEGURANÇA**
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Desabilita CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Define as regras de autorização para as rotas
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas públicas (LOGIN e CADASTRO)
                        .requestMatchers("/api/auth/**", "/api/usuarios/cadastro").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Todas as outras requisições devem ser autenticadas
                        .anyRequest().authenticated()
                )

                // 4. Configura sessão STATELESS (essencial para JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Adiciona o nosso filtro JWT antes do filtro padrão do Spring Security
                .addFilterBefore(jwtAuthorizarionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean específico para configuração CORS para o Spring Security.
     * Esta configuração substitui a necessidade de injetar a classe CorsConfiguration antiga.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Define as origens permitidas (Seu frontend Vite/React)
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        // Define os métodos permitidos (GET, POST, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Define os cabeçalhos permitidos (incluindo 'Authorization' para o JWT)
        config.setAllowedHeaders(List.of("*"));

        // Permite o envio de cookies ou credenciais (se necessário)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica a todas as URLs
        return source;
    }


    /**
     * Bean para o AuthenticationManager, necessário para realizar o login no Controller.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean para o PasswordEncoder (BCrypt é o padrão recomendado).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}