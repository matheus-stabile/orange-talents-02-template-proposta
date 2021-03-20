package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "AnaliseFinanceiraClient", url = "${analiseFinanceira.url}")
public interface AnaliseFinanceiraClient {

    @PostMapping("/api/solicitacao")
    AnaliseFinanceiraResponse analiza(AnaliseFinanceiraRequest analiseFinanceiraRequest);
}
