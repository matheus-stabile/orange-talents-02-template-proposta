package com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.codec.binary.Base64.*;

public class Base64Validator implements ConstraintValidator<Base64, String> {
    @Override
    public void initialize(Base64 constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isBase64(value);
    }
}
