package br.com.phteam.consultoria.api.infrastructure.exception;


public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}