package com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AnaliseCartaoResponse {

    @NotBlank
    @JsonProperty("id")
    private String numero;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotBlank
    private String titular;

    @NotNull
    private BigDecimal limite;

    @NotBlank
    @JsonProperty("idProposta")
    private String proposta;

    public AnaliseCartaoResponse(@NotBlank @CreditCardNumber String numero, @NotNull LocalDateTime emitidoEm, @NotBlank String titular, @NotNull BigDecimal limite, @NotBlank String proposta) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.proposta = proposta;
    }

    public Cartao toCartao() {
        return new Cartao(numero, emitidoEm, titular, limite);
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public String getProposta() {
        return proposta;
    }
}
