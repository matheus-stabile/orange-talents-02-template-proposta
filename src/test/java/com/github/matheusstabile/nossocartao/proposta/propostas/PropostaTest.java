package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.propostas.enums.StatusProposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PropostaTest {

    private Proposta proposta;

    @BeforeEach
    void setup() {
        proposta = new Proposta("11111111111", "user@email.com", "nome", "endereco", BigDecimal.ONE);
        proposta.atualizaStatusAnalise(StatusProposta.ELEGIVEL);
    }


    @Test
    @DisplayName("Nao deve atualizar status de propostas que ja sao elegiveis")
    void naoDeveAtualizarStatusDePropostaElegivel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> proposta.atualizaStatusAnalise(StatusProposta.NAO_ELEGIVEL));

    }
}