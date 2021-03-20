package com.github.matheusstabile.nossocartao.proposta.propostas;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private BigDecimal salario;

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        Assert.isTrue(StringUtils.hasText(documento), "Documento não pode estar em branco");
        Assert.isTrue(StringUtils.hasText(email), "Email não pode estar em branco");
        Assert.isTrue(StringUtils.hasText(nome), "Nome não pode estar em branco");
        Assert.isTrue(StringUtils.hasText(endereco), "Endereço não pode estar em branco");
        Assert.isTrue(salario.compareTo(BigDecimal.ZERO) > 0, "Salário deve ser positivo");

        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    @Deprecated
    public Proposta() {
    }

    public Long getId() {
        return id;
    }
}
