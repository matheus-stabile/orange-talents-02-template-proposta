package com.github.matheusstabile.nossocartao.proposta.cartoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaoTest {

    Cartao cartaoValido;

    @BeforeEach
    void setup() {
        cartaoValido = new Cartao("numero", LocalDateTime.parse("2021-03-20T20:08:43.777489"), "titular", BigDecimal.ONE);
    }

    @Test
    @DisplayName("Não deve associar proposta ao cartão se a proposta for nula")
    void naoDeveAssociarPropostaNula() {

        assertThrows(IllegalArgumentException.class, () -> cartaoValido.associarProposta(null));
    }

}