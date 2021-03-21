package com.github.matheusstabile.nossocartao.proposta.propostas;

public class PropostaResponse {

    private final String email;

    private final String nome;

    private final PropostaStatus propostaStatus;


    public PropostaResponse(Proposta proposta) {
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.propostaStatus = proposta.getStatusProposta();
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public PropostaStatus getPropostaStatus() {
        return propostaStatus;
    }
}
