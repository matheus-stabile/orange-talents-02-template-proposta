package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AnaliseFinanceiraClientFallback implements FallbackFactory<AnaliseFinanceiraClient> {

    @Override
    public AnaliseFinanceiraClient create(Throwable cause) {
        return request -> {
                AnaliseFinanceiraResponse analiseFinanceiraResponse = new AnaliseFinanceiraResponse();

            if (cause.getMessage().contains("COM_RESTRICAO"))
                analiseFinanceiraResponse.setResultadoSolicitacao(AnaliseFinanceiraReponseStatus.COM_RESTRICAO);
            else
                analiseFinanceiraResponse.setResultadoSolicitacao(AnaliseFinanceiraReponseStatus.NAO_PROCESSADO);

            return analiseFinanceiraResponse;
        };
    }
}
