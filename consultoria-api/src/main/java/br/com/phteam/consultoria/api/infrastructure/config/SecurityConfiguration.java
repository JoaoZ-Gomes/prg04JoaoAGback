package br.com.phteam.consultoria.api.infrastructure.config;

import br.com.phteam.consultoria.api.features.usuario.CustomUserDetailsService;
import br.com.phteam.consultoria.api.infrastructure.auth.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    // 🔐 FILTRO PRINCIPAL
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ✅ CORS NO SECURITY (
                .cors(cors -> {})

                // ❌ CSRF desabilitado (API stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 🔐 API SEM SESSÃO
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔑 REGRAS DE ACESSO
                .authorizeHttpRequests(authorize -> authorize

                        // 🔓 Públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                        // 🔒 CLIENTE
                        .requestMatchers(HttpMethod.GET, "/api/clientes/meu-perfil")
                        .hasRole("CLIENTE")

                        // 🔒 CONSULTOR
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**")
                        .hasRole("CONSULTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**")
                        .hasRole("CONSULTOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/clientes/**")
                        .hasRole("CONSULTOR")
                        .requestMatchers("/api/consultores/**")
                        .hasRole("CONSULTOR")


                        .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()

                        // 🔐 Qualquer outra precisa autenticação
                        .anyRequest().authenticated()
                )

                // 🔑 FILTRO JWT
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // 🔐 AUTH MANAGER
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {

        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    // 🔐 PASSWORD
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌍 CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration config =
                new org.springframework.web.cors.CorsConfiguration();

        config.setAllowedOriginPatterns(
                List.of("http://localhost:*")
        );

        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
