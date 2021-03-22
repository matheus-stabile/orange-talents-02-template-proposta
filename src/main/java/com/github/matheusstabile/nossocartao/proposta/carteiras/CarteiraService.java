package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CarteiraService {

    private final EntityManager entityManager;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger(CarteiraService.class);

    public CarteiraService(EntityManager entityManager, CartaoClient cartaoClient) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
    }

    @Transactional
    public ResponseEntity<?> associaCarteira(Cartao cartao, TipoCarteira tipoCarteira, String email, UriComponentsBuilder uri) {
        try {
            Map carteiraRequest = Map.of("email", email, "carteira", tipoCarteira);
            ResponseEntity response = cartaoClient.associaCarteira(carteiraRequest, cartao.getNumero());
            Carteira carteira = new Carteira(email, tipoCarteira, cartao);
            entityManager.persist(carteira);
            cartao.adicionaCarteira(carteira);
            entityManager.merge(cartao);
            logger.info("[ASSOCIAÇÃO DE CARTEIRA DIGITAL] Carteira tipo {} adicionada ao cartão", carteira.getTipo());
            return ResponseEntity.created(uri.path("/cartoes/carteiras/{id}").buildAndExpand(carteira.getId()).toUri()).build();

        }catch (FeignException e) {
            logger.error("[ASSOCIAÇÃO DE CARTEIRA DIGITAL] Não foi possível associar carteira digital, erro: {}", e.getMessage());
            return ResponseEntity.unprocessableEntity().body(new ErroPadronizado(Map.of("carteira", "não foi possível processar os dados")));
        }
    }
}
