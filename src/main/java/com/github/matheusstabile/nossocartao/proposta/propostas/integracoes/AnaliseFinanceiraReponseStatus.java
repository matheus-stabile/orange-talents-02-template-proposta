package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaStatus;

public enum AnaliseFinanceiraReponseStatus {
    COM_RESTRICAO(PropostaStatus.NAO_ELEGIVEL),
    SEM_RESTRICAO(PropostaStatus.ELEGIVEL),
    NAO_PROCESSADO(PropostaStatus.NAO_PROCESSADO);

    private PropostaStatus propostaStatus;

    private AnaliseFinanceiraReponseStatus(PropostaStatus propostaStatus) {
        this.propostaStatus = propostaStatus;
    }

    public PropostaStatus getPropostaStatus() {
        return propostaStatus;
    }
}
