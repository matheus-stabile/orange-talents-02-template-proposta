package com.github.matheusstabile.nossocartao.proposta.bloqueios;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BloqueioControllerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CartaoClient cartaoClient;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private Cartao cartao;

    @Mock
    private HttpServletRequest request;

    private BloqueioController controller;

    @BeforeEach
    public void setup() {
        initMocks(this);
        controller = new BloqueioController(entityManager, cartaoClient, tracer);
    }

    @Test
    @DisplayName("Não deve bloquear se cartão não foi encontrado")
    public void naoDeveBloquearSeCartaoNaoFoiEncontrado() {
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(null);
        ResponseEntity responseEntity = controller.adicionarBloqueio(1L, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve bloquear se cartão não pertence ao solicitante")
    public void naoDeveBloquearSeCartaoNaoPertenceAoSolicitante() {
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(false);
        ResponseEntity responseEntity = controller.adicionarBloqueio(1L, null);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve bloquear cartão")
    public void deveBloquearCartao() {
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(true);
        ResponseEntity responseEntity = controller.adicionarBloqueio(1L, request);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey("Location"));
    }

}