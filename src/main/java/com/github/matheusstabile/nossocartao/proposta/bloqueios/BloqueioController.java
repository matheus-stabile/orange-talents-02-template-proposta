package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes.InformacoesObrigatorias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RestController
public class BloqueioController {

    private final EntityManager entityManager;
    private final BloqueioService bloqueioService;
    private final Logger logger = LoggerFactory.getLogger(BloqueioController.class);

    @Autowired
    public BloqueioController(EntityManager entityManager, BloqueioService bloqueioService) {
        this.entityManager = entityManager;
        this.bloqueioService = bloqueioService;
    }

    @PostMapping("/cartoes/{id}/bloquear")
    @Transactional
    public ResponseEntity<?> adicionarBloqueio(@PathVariable("id") Long id, @InformacoesObrigatorias HttpServletRequest request) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(entityManager.find(Cartao.class, id));

        if (cartaoOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroPadronizado(Map.of("cartao", "não encontrado")));

        Cartao cartao = cartaoOptional.get();

        return bloqueioService.bloqueia(cartao, request);
    }
}
