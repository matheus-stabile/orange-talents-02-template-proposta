package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
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

    @Enumerated(EnumType.STRING)
    private PropostaStatus propostaStatus;

    @OneToOne(mappedBy = "proposta", cascade = CascadeType.MERGE)
    private Cartao cartao;


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
        this.propostaStatus = PropostaStatus.NAO_PROCESSADO;
    }

    @Deprecated
    public Proposta() {
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public PropostaStatus getStatusProposta() {
        return propostaStatus;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getEmail() {
        return email;
    }

    public void atualizaStatusAnalise(PropostaStatus propostaStatus) {
        Assert.isTrue(!this.propostaStatus.equals(PropostaStatus.ELEGIVEL), "a proposta já é elegivel");

        this.propostaStatus = propostaStatus;
    }

    public void associarCartao(Cartao cartao) {
        Assert.notNull(cartao, "cartão não pode ser nulo");
        Assert.isNull(this.cartao, "já existe um cartão associado a essa proposta");

        this.cartao = cartao;
    }
}
