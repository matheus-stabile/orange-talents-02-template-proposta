package com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroPadronizado> handleConstraintViolationException(ConstraintViolationException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getConstraintViolations().forEach(constraintViolation -> {
            erros.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        });

        ErroPadronizado erroPadronizado = new ErroPadronizado(erros);

        return ResponseEntity.badRequest().body(erroPadronizado);

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadronizado> handleResponseStatusException(ResponseStatusException exception) {

        ErroPadronizado erroPadronizado = new ErroPadronizado(Map.of("erro", exception.getReason()));

        return ResponseEntity.status(exception.getStatus()).body(erroPadronizado);
    }
}
