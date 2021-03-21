package com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes;

import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaoServiceTest {

    @Mock
    CartaoClient cartaoClient;

    @Mock
    PropostaRepository propostaRepository;

    CartaoService cartaoService;

    @BeforeEach
    void setup() {
        cartaoService = new CartaoService(propostaRepository, cartaoClient);
    }

    @Test
    @DisplayName("Não deve buscar cartão se a proposta for nula")
    void naoDeveBuscarCartaoSePropostaForNula() {

        assertThrows(IllegalArgumentException.class, () -> cartaoService.buscarCartao(null));
    }
}