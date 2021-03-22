package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class BloqueioService {

    private final EntityManager entityManager;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger(BloqueioService.class);

    @Autowired
    public BloqueioService(EntityManager entityManager, CartaoClient cartaoClient) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
    }

    public ResponseEntity<?> bloqueia(Cartao cartao, HttpServletRequest request) {

        if (cartao.estaBloqueado()) {
            logger.error("[BLOQUEIO DE CARTÃO] O cartão já está bloqueado, id: {}", cartao.getId());
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("cartao", "já está bloqueado")));
        }

        try {
            Map bloqueioRequest = Map.of("sistemaResponsavel", "${spring.application.name}");
            cartaoClient.bloqueiaCartao(cartao.getNumero(), bloqueioRequest);
            Bloqueio bloqueio = new Bloqueio(request.getRemoteAddr(), request.getHeader("User-Agent"));
            entityManager.persist(bloqueio);
            cartao.adicionarBloqueio(bloqueio);
            entityManager.merge(cartao);
            logger.info("[BLOQUEIO DE CARTÃO] Cartao bloqueado, id: {}", cartao.getId());
            return ResponseEntity.ok().build();
        } catch (FeignException e) {
            logger.error("[BLOQUEIO DE CARTÃO] Não foi possível processar o bloqueio, erro: {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("bloqueio", "não foi possível processar")));
        }
    }
}
