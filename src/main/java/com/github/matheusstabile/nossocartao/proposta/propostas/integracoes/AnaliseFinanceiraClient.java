package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "AnaliseFinanceiraClient", url = "${analiseFinanceira.url}", fallbackFactory = AnaliseFinanceiraClientFallback.class)
public interface AnaliseFinanceiraClient {

    @PostMapping("/api/solicitacao")
    AnaliseFinanceiraResponse analiza(AnaliseFinanceiraRequest analiseFinanceiraRequest);


}
