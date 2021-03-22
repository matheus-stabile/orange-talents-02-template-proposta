package com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InformacoesObrigatoriasValidator implements ConstraintValidator<InformacoesObrigatorias, HttpServletRequest> {

    @Override
    public void initialize(InformacoesObrigatorias constraintAnnotation) {

    }

    @Override
    public boolean isValid(HttpServletRequest value, ConstraintValidatorContext context) {
        return !value.getRemoteAddr().isBlank() && !value.getHeader("User-Agent").isBlank();
    }
}
