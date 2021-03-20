package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;

import javax.validation.constraints.NotBlank;

public class AnaliseFinanceiraRequest {

    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    @NotBlank
    private String idProposta;

    public AnaliseFinanceiraRequest(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId().toString();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
