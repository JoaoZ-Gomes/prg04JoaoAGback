package br.com.phteam.consultoria.api.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração CORS (Cross-Origin Resource Sharing) para permitir que o frontend
 * acesse a API REST que roda em uma porta diferente.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Aplica a configuração a todos os endpoints da API (/**)
        registry.addMapping("/**")
                // Permite requisições originadas do seu frontend (Vite)
                .allowedOrigins("http://localhost:3000")

                // Define os métodos HTTP que são permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")

                // Permite o envio de credenciais
                .allowCredentials(true);
    }
}