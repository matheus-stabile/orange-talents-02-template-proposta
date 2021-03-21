package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.Base64;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    @Base64
    private String digital;

    public Biometria toModel() {
        return new Biometria(digital);
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }
}
