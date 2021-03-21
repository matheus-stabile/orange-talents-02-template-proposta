package com.github.matheusstabile.nossocartao.proposta.cartoes;

import com.github.matheusstabile.nossocartao.proposta.biometrias.Biometria;
import com.github.matheusstabile.nossocartao.proposta.propostas.Proposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaoTest {

    Cartao cartaoValido;

    Proposta propostaValida;

    Biometria biometriaValida;


    @BeforeEach
    void setup() {
        cartaoValido = new Cartao("numero", LocalDateTime.parse("2021-03-20T20:08:43.777489"), "titular", BigDecimal.ONE);
        propostaValida = new Proposta("documento", "email", "nome", "endereco", BigDecimal.ONE);
        biometriaValida = new Biometria("digital");

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

    @Test
    @DisplayName("Não deve adicionar biometria se for nula")
    void naoDeveAdicionarBiometriaNula() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> cartaoValido.adicionaBiometria(null));
    }

    @Test
    @DisplayName("Deve adicionar biometria")
    void deveAdicionarBiometria() {

        cartaoValido.adicionaBiometria(biometriaValida);

        Assertions.assertTrue(cartaoValido.getBiometrias().contains(biometriaValida));
    }

}