package com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes;

import com.github.matheusstabile.nossocartao.proposta.avisos.AvisoViagemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "AnaliseCartaoClient", url = "${analiseCartao.url}")
public interface CartaoClient {

    @GetMapping("/api/cartoes")
    AnaliseCartaoResponse buscarCartaoPeloIdDaProposta(@RequestParam String idProposta);

    @PostMapping("/api/cartoes/{idCartao}/bloqueios")
    ResponseEntity bloqueiaCartao(@PathVariable String idCartao, @RequestBody Map bloqueioRequest);

    @PostMapping("/api/cartoes/{idCartao}/avisos")
    ResponseEntity informaViagem(@PathVariable String idCartao, @RequestBody AvisoViagemRequest avisoViagemRequest);
}
