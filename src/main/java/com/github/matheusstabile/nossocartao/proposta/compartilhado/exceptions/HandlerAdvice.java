package com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadronizado> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErroPadronizado erroPadronizado = new ErroPadronizado(erros);

        return ResponseEntity.badRequest().body(erroPadronizado);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadronizado> handleResponseStatusException(ResponseStatusException exception) {

        Map<String, String> erros = new HashMap<>();

        List<ObjectError> objectErrors = List.of(new ObjectError("erro", exception.getReason() != null ? exception.getReason() : "Os dados não puderam ser processados"));

        objectErrors.forEach(objectError -> {
            erros.put(objectError.getObjectName(), objectError.getDefaultMessage());
        });

        ErroPadronizado erroPadronizado = new ErroPadronizado(erros);

        return ResponseEntity.status(exception.getStatus()).body(erroPadronizado);
    }

}
