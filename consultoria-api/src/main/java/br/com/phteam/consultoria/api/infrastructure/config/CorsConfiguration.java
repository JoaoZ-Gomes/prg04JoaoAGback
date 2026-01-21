package br.com.phteam.consultoria.api.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Permite qualquer porta do localhost
                .allowedOriginPatterns("http://localhost:*")

                // Permite todos os métodos HTTP comuns
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // Permite envio de cookies/token se necessário
                .allowCredentials(true)

                // Permite qualquer header
                .allowedHeaders("*");
    }
}
