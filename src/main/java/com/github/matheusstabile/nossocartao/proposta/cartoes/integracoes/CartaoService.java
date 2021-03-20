package com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaRepository;
import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaStatus;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartaoService {

    private final PropostaRepository propostaRepository;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger(CartaoService.class);

    @Autowired
    public CartaoService(PropostaRepository propostaRepository, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(fixedDelayString = "${analiseCartaoService.analisePeriodica.delay}")
    public void analisePeriodicaDePropostasElegiveisSemCartao() {
        logger.info("[ANÁLISE CARTÃO] Verificando se há propostas elegiveis e sem cartão");
        List<Proposta> propostas = propostaRepository.findByPropostaStatusIsAndCartaoIdIsNull(PropostaStatus.ELEGIVEL);
        propostas.forEach(this::buscarCartao);
    }

    @Transactional
    public void buscarCartao(Proposta proposta) {
        Assert.notNull(proposta, "Proposta não pode ser nula");

        try {
            AnaliseCartaoResponse analiseCartaoResponse = cartaoClient.buscarCartaoPeloIdDaProposta(String.valueOf(proposta.getId()));
            Cartao cartao = analiseCartaoResponse.toCartao();
            proposta.associarCartao(cartao);
            cartao.associarProposta(proposta);
            propostaRepository.save(proposta);
            logger.info("[ANÁLISE CARTÃO] Cartão associado a proposta {}", proposta.getId());
        } catch (FeignException e) {
            logger.error("[ANÁLISE CARTÃO] Não foi possível associar cartão devido ao erro: {}", e.getMessage());
        }
    }
}
