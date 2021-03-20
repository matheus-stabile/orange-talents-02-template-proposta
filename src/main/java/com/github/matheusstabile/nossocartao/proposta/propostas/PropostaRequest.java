package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.validacoes.CPFouCNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank
    @CPFouCNPJ
    private final String documento;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String nome;

    @NotBlank
    private final String endereco;

    @NotNull
    @Positive
    private final BigDecimal salario;

    public PropostaRequest(@NotBlank @CPFouCNPJ String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
        this.documento = documento.replaceAll("[/.-]", "");
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel() {
        return new Proposta(documento, email, nome, endereco, salario);
    }
}
