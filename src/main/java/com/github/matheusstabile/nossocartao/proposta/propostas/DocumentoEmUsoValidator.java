package com.github.matheusstabile.nossocartao.proposta.propostas;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DocumentoEmUsoValidator {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean documentoEmUso(PropostaRequest propostaRequest) {

        return !entityManager.createQuery("select 1 from Proposta where documento = :documento")
                .setParameter("documento", propostaRequest.getDocumento())
                .getResultList().isEmpty();
    }
}
