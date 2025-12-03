package br.com.phteam.consultoria.api.auth.service;

import br.com.phteam.consultoria.api.auth.dto.LoginRequestDTO;
import br.com.phteam.consultoria.api.auth.dto.LoginSuccessDTO; // Import alterado

public interface AuthIService {

    LoginSuccessDTO autenticar(LoginRequestDTO request);
}