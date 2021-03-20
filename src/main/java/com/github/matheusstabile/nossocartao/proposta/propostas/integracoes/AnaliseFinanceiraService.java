package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaRepository;
import com.github.matheusstabile.nossocartao.proposta.propostas.enums.StatusProposta;
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
public class AnaliseFinanceiraService {

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraClient analiseFinanceiraClient;
    private final Logger logger = LoggerFactory.getLogger(AnaliseFinanceiraService.class);

    @Autowired
    public AnaliseFinanceiraService(PropostaRepository propostaRepository, AnaliseFinanceiraClient analiseFinanceiraClient) {
        this.propostaRepository = propostaRepository;
        this.analiseFinanceiraClient = analiseFinanceiraClient;
    }

    @Scheduled(fixedDelayString = "${analiseFinanceiraService.analisePeriodica.delay}")
    public void analisePeriodicaDePropostasNaoProcessadas() {

        logger.info("[ANÁLISE FINANCEIRA] Verificando se há propostas não processadas");
        List<Proposta> propostas = propostaRepository.findByStatusPropostaIs(StatusProposta.NAO_PROCESSADO);
        propostas.forEach(this::processa);
    }

    @Transactional
    public void processa(Proposta proposta) {
        Assert.notNull(proposta, "Proposta não pode ser nula");

        try {
            AnaliseFinanceiraResponse analiseFinanceiraResponse = analiseFinanceiraClient.analiza(new AnaliseFinanceiraRequest(proposta));
            proposta.atualizaStatusAnalise(StatusProposta.ELEGIVEL);
            propostaRepository.save(proposta);
            logger.info("[ANÁLISE FINANCEIRA] Atualizado status da proposta {} para {}", proposta.getId(), proposta.getStatusProposta());
        } catch (FeignException e) {
            if (e.status() == 422) {
                proposta.atualizaStatusAnalise(StatusProposta.NAO_ELEGIVEL);
                propostaRepository.save(proposta);
                logger.info("[ANÁLISE FINANCEIRA] Atualizado status da proposta {} para {}", proposta.getId(), proposta.getStatusProposta());

            } else {
                logger.error("[ANÁLISE FINANCEIRA] Não foi possível atualizar o status da proposta, erro: {}", e.getMessage());
            }
        }
    }
}

