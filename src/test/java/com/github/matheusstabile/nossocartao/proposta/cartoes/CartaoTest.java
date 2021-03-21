package com.github.matheusstabile.nossocartao.proposta.cartoes;

import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaoTest {

    Cartao cartaoValido;

    Proposta propostaValida;

    @BeforeEach
    void setup() {
        cartaoValido = new Cartao("numero", LocalDateTime.parse("2021-03-20T20:08:43.777489"), "titular", BigDecimal.ONE);
        propostaValida = new Proposta("documento", "email", "nome", "endereco", BigDecimal.ONE);
    }

    @Test
    @DisplayName("Não deve associar proposta ao cartão se a proposta for nula")
    void naoDeveAssociarPropostaNula() {

        assertThrows(IllegalArgumentException.class, () -> cartaoValido.associarProposta(null));
    }

    @Test
    @DisplayName("Deve associar proposta ao cartão se a proposta for valida")
    void deveAssociarPropostaSeForValida() {

        cartaoValido.associarProposta(propostaValida);

        assertEquals(propostaValida, cartaoValido.getProposta());


    }

}