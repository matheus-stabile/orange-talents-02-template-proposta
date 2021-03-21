package com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.Base64Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = Base64Validator.class)
public @interface Base64 {

    String message() default "não está em Base64";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
