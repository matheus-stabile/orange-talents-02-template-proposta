package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class AvisoViagemService {

    private final EntityManager entityManager;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger(AvisoViagemService.class);

    public AvisoViagemService(EntityManager entityManager, CartaoClient cartaoClient) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
    }

    public ResponseEntity<?> avisaViagem(Cartao cartao, AvisoViagemRequest avisoViagemRequest, HttpServletRequest request) {

        try {
            ResponseEntity response = cartaoClient.informaViagem(cartao.getNumero(), avisoViagemRequest);
            AvisoViagem avisoViagem = new AvisoViagem(request.getRemoteAddr(), request.getHeader("user-Agent"), cartao);
            entityManager.persist(avisoViagem);

            cartao.adicionarAviso(avisoViagem);
            entityManager.merge(cartao);

            logger.info("[CADASTRO AVISO DE VIAGEM] Aviso de viagem adicionado ao cartão: {}", cartao.getId());
            return ResponseEntity.ok().build();

        } catch (FeignException e) {
            logger.info("[CADASTRO AVISO DE VIAGEM] Não foi possível adicionar aviso de viagem ao cartão devido ao erro: {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("aviso", "não foi possível processar")));
        }
    }
}
