package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

public class AnaliseFinanceiraResponse {


    private String documento;

    private String nome;

    private AnaliseFinanceiraReponseStatus resultadoSolicitacao;

    private String idProposta;

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public AnaliseFinanceiraReponseStatus getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public void setResultadoSolicitacao(AnaliseFinanceiraReponseStatus resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }
}