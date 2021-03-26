package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.Base64;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.ValidacaoPrimaria;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.ValidacaoSecundaria;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@GroupSequence({BiometriaRequest.class, ValidacaoPrimaria.class, ValidacaoSecundaria.class})
public class BiometriaRequest {

    @NotBlank(groups = ValidacaoPrimaria.class)
    @Base64(groups = ValidacaoSecundaria.class)
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
