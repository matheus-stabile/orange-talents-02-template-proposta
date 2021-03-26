package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.Crypto;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca.JwtDecoder;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.CPFouCNPJ;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String documento;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String endereco;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private PropostaStatus propostaStatus;

    @OneToOne(mappedBy = "proposta", cascade = CascadeType.MERGE)
    private Cartao cartao;


    public Proposta(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull @Positive BigDecimal salario) {
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
        Assert.isTrue(this.propostaStatus.equals(PropostaStatus.NAO_PROCESSADO), "a proposta já foi processada");

        this.propostaStatus = propostaStatus;
    }

    public void associarCartao(Cartao cartao) {
        Assert.notNull(cartao, "cartão não pode ser nulo");
        Assert.isNull(this.cartao, "já existe um cartão associado a essa proposta");

        this.cartao = cartao;
    }

    public boolean pertenceAoUsuario(String token) {
        Assert.isTrue(StringUtils.hasText(token), "token não pode estar em branco");

        return this.email.equals(JwtDecoder.pegaEmail(token));
    }

    public void encriptaDocumento() {
        documento = Crypto.encrypt(documento);
    }
}
