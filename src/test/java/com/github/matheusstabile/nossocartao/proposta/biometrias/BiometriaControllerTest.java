package com.github.matheusstabile.nossocartao.proposta.biometrias;

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
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BiometriaControllerTest {

    private BiometriaController biometriaController;

    @Mock
    EntityManager entityManager;

    @Mock
    BiometriaRequest biometriaRequest;

    @Mock
    Cartao cartao;

    @BeforeEach
    void setup() {
        biometriaController = new BiometriaController(entityManager);
    }

    @Test
    @DisplayName("Deve retornar 404 se o cartao nao existir")
    void deveRetornar400SeBiometriaInvalida() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(null);

        ResponseEntity responseEntity = biometriaController.adicionaBiometria(1L, biometriaRequest, UriComponentsBuilder.newInstance());

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getBody() instanceof ErroPadronizado)
        );
    }

    @Test
    @DisplayName("Deve criar biometria se as precondições forem válidas")
    void deveCriarBiometriaSePrecondicoesForemValidas() {

        when(entityManager.find(ArgumentMatchers.any(), ArgumentMatchers.any(Long.class))).thenReturn(cartao);
        when(biometriaRequest.toModel()).thenReturn(new Biometria());

        ResponseEntity responseEntity = biometriaController.adicionaBiometria(1L, biometriaRequest, UriComponentsBuilder.newInstance());

        Mockito.verify(cartao).adicionaBiometria(biometriaRequest.toModel());

    }
}