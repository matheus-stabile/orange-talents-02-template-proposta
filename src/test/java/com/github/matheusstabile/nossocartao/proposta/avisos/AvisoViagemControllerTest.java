package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvisoViagemControllerTest {

    AvisoViagemController avisoViagemController;

    @Mock
    EntityManager entityManager;

    @Mock
    AvisoViagemService avisoViagemService;

    @Mock
    AvisoViagemRequest avisoViagemRequest;

    @Mock
    Cartao cartao;

    @Mock
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setup() {
        avisoViagemController = new AvisoViagemController(entityManager, avisoViagemService);
    }

    @Test
    @DisplayName("Deve retornar 404 se o cartão não existir")
    void deveRetornar404SeOCartaoNaoExistir() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(null);

        ResponseEntity responseEntity = avisoViagemController.avisarViagem(1L, avisoViagemRequest, httpServletRequest);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }

    @Test
    @DisplayName("Deve processar aviso de viagem se as precondições forem válidas")
    void deveProcessarAvisoDeViagemSeAsPrecondicoesForemValidas() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(cartao);

        ResponseEntity responseEntity = avisoViagemController.avisarViagem(1L, avisoViagemRequest, httpServletRequest);

        Mockito.verify(avisoViagemService).avisaViagem(cartao, avisoViagemRequest, httpServletRequest);
    }
}