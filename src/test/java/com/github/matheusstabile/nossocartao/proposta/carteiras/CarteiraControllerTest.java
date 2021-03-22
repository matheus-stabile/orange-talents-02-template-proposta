package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarteiraControllerTest {

    CarteiraController carteiraController;

    @Mock
    EntityManager entityManager;

    @Mock
    CarteiraService carteiraService;

    @Mock
    CarteiraRequest carteiraRequest;

    @Mock
    Cartao cartao;


    @BeforeEach
    void setup() {
        carteiraController = new CarteiraController(entityManager, carteiraService);
    }

    @Test
    @DisplayName("Deve retornar 404 se o cartão não existir")
    void deveRetornar404SeOCartaoNaoExistir() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(null);

        ResponseEntity responseEntity = carteiraController.adicionarCarteiraPaypal(1L, carteiraRequest, UriComponentsBuilder.newInstance());

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }

    @Test
    @DisplayName("Deve retornar 422 se cartao ja tiver carteira Paypal associada")
    void deveRetornar422SeJaTiverPaypal() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(cartao);
        when(cartao.temCarteira(TipoCarteira.PAYPAL)).thenReturn(true);

        ResponseEntity responseEntity = carteiraController.adicionarCarteiraPaypal(1L, carteiraRequest, UriComponentsBuilder.newInstance());

        Assertions.assertAll(
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }

    @Test
    @DisplayName("Deve retornar 422 se cartao ja tiver carteira Samsung Pay associada")
    void deveRetornar422SeJaTiverSamsungPay() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(cartao);
        when(cartao.temCarteira(TipoCarteira.SAMSUNG_PAY)).thenReturn(true);

        ResponseEntity responseEntity = carteiraController.adicionarCarteiraSamsungpay(1L, carteiraRequest, UriComponentsBuilder.newInstance());

        Assertions.assertAll(
                () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }
}