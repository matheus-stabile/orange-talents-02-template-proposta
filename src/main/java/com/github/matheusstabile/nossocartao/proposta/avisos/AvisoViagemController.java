package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.InformacoesObrigatorias;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
public class AvisoViagemController {

    private final EntityManager entityManager;
    private final AvisoViagemService avisoViagemService;

    @Autowired
    public AvisoViagemController(EntityManager entityManager, AvisoViagemService avisoViagemService) {
        this.entityManager = entityManager;
        this.avisoViagemService = avisoViagemService;
    }

    @PostMapping("/cartoes/{idCartao}/aviso-viagem")
    @Transactional
    public ResponseEntity<?> avisarViagem(@PathVariable("idCartao") Long idCartao, @RequestBody @Valid AvisoViagemRequest avisoViagemRequest, @InformacoesObrigatorias HttpServletRequest request) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        if (cartaoOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("cartao", "não encontrado")));

        return avisoViagemService.avisaViagem(cartaoOptional.get(), avisoViagemRequest, request);
    }
}
