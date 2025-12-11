package br.com.phteam.consultoria.api.infrastructure.exception;

// Lançada quando um recurso (Consultor, Cliente) não é encontrado por ID.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}