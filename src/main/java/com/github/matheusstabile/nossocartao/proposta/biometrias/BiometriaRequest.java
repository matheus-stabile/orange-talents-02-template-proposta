package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.Base64;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    @Base64
    private String digital;

    public Biometria toModel(Cartao cartao) {
        return new Biometria(digital, cartao);
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }
}
