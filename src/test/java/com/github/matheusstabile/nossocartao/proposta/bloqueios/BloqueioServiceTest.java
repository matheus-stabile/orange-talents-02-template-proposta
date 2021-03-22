package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BloqueioServiceTest {

    @Mock
    CartaoClient cartaoClient;

    @Mock
    EntityManager entityManager;

    BloqueioService bloqueioService;

    @BeforeEach
    void setup() {
        bloqueioService = new BloqueioService(entityManager, cartaoClient);
    }

    @Test
    @DisplayName("Não deve processar bloqueio se o cartão informado for nulo")
    void naoDeveProcessarBloqueioSeCartaoForNulo() {

        assertThrows(IllegalArgumentException.class, () -> bloqueioService.bloqueia(null, null));
    }

}