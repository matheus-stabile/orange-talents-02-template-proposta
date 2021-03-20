package com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AnaliseCartaoClient", url = "${analiseCartao.url}")
public interface CartaoClient {

    @GetMapping("/api/cartoes")
    AnaliseCartaoResponse buscarCartaoPeloIdDaProposta(@RequestParam String idProposta);
}
