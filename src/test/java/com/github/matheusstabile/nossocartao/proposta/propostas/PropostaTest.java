package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PropostaTest {

    Proposta propostaValida;

    Cartao cartaoValido;

    @BeforeEach
    void setup() {
        propostaValida = new Proposta("11111111111", "user@email.com", "nome", "endereco", BigDecimal.ONE);
        cartaoValido = new Cartao("numero", LocalDateTime.parse("2021-03-20T20:08:43.777489"), "titular", BigDecimal.ONE);
    }


    @Test
    @DisplayName("Nao deve atualizar status da propostas se já for elegivel")
    void naoDeveAtualizarStatusDePropostaElegivel() {

        propostaValida.atualizaStatusAnalise(PropostaStatus.ELEGIVEL);

        Assertions.assertThrows(IllegalArgumentException.class, () -> propostaValida.atualizaStatusAnalise(PropostaStatus.NAO_ELEGIVEL));
    }

    @Test
    @DisplayName("Deve atualizar status da proposta")
    void deveAtualizarStatusDePropostas() {

        propostaValida.atualizaStatusAnalise(PropostaStatus.NAO_ELEGIVEL);

        Assertions.assertEquals(PropostaStatus.NAO_ELEGIVEL, propostaValida.getStatusProposta());
    }

    @Test
    @DisplayName("Nao deve associar cartão nulo")
    void naoDeveAssociarCartaoNulo() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> propostaValida.associarCartao(null));
    }

    @Test
    @DisplayName("Deve associar cartão valido")
    void deveAssociarUmCartao() {

        propostaValida.associarCartao(cartaoValido);

        Assertions.assertEquals(cartaoValido, propostaValida.getCartao());
    }

    @Test
    @DisplayName("Nao deve associar um novo cartão se a proposta já tiver cartão associado")
    void naoDeveAssociarNovoCartaoSeAPropostaJaTiverCartaoAssociado() {

        propostaValida.associarCartao(cartaoValido);
        Assertions.assertThrows(IllegalArgumentException.class, () -> propostaValida.associarCartao(cartaoValido));
    }

    @Test
    @DisplayName("Deve associar um novo cartão se a proposta não tiver cartão associado")
    void naoDeveAssociarNovoCartaoSeAPropostaNaoTiverCartaoAssociado() {

        propostaValida.associarCartao(cartaoValido);

        Assertions.assertEquals(cartaoValido, propostaValida.getCartao());
    }
}