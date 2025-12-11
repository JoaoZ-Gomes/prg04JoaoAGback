package br.com.phteam.consultoria.api.infrastructure.auth.service;

import br.com.phteam.consultoria.api.infrastructure.auth.dto.LoginRequestDTO;
import br.com.phteam.consultoria.api.infrastructure.auth.dto.LoginSuccessDTO; // Import alterado

public interface AuthIService {

    LoginSuccessDTO autenticar(LoginRequestDTO request);
}