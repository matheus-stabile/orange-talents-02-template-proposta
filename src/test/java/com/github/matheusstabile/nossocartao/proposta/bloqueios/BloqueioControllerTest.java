package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BloqueioControllerTest {

    BloqueioController bloqueioController;

    @Mock
    EntityManager entityManager;

    @Mock
    BloqueioService bloqueioService;

    @Mock
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setup() {
        bloqueioController = new BloqueioController(entityManager, bloqueioService);

    }

    @Test
    @DisplayName("Deve retornar 404 se o cartão não existir")
    void deveRetornar404SeOCartaoNaoExistir() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(null);

        ResponseEntity responseEntity = bloqueioController.adicionarBloqueio(1L, httpServletRequest);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }




}