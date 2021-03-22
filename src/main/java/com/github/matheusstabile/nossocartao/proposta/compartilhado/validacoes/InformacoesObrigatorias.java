package com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {InformacoesObrigatoriasValidator.class})
public @interface InformacoesObrigatorias {

    String message() default "A requisição não contém as informações obrigatórias";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
