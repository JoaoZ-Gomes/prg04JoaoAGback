package br.com.phteam.consultoria.api.exception; // Ajuste o pacote conforme sua estrutura

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Indica ao Spring que esta classe deve tratar exceções globalmente
@RestControllerAdvice
public class ErrorHandler {

    // 1. Trata exceções de Validação de Campos (@Valid falhou)
    // Retorna HTTP 400 e uma lista de erros de campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            // Verifica se o erro é de campo para pegar o nome
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Trata a RegraDeNegocioException (Ex: E-mail ou CREF duplicado)
    // Retorna HTTP 400 e a mensagem de erro
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<String> handleRegraDeNegocioException(
            RegraDeNegocioException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Trata a RecursoNaoEncontradoException (Ex: Consulta por ID inexistente)
    // Retorna HTTP 404 e a mensagem de erro
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> handleRecursoNaoEncontradoException(
            RecursoNaoEncontradoException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}