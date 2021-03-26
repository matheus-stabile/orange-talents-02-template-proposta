package com.github.matheusstabile.nossocartao.proposta.avisos;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.cartoes.integracoes.CartaoClient;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvisoViagemControllerTest {

    @Mock
    EntityManager entityManager;

    @Mock
    CartaoClient cartaoClient;

    @Mock
    Tracer tracer;

    @Mock
    Span span;

    AvisoViagemRequest avisoViagemRequest;

    @Mock
    Cartao cartao;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    AvisoViagemController avisoViagemController;

    @BeforeEach
    void setup() {
        avisoViagemController = new AvisoViagemController(entityManager, cartaoClient, tracer);
    }

    @Test
    @DisplayName("Não deve cadastrar aviso se cartão não foi encontrado")
    public void naoDeveCadastrarAvisoSeCartaoNaoFoiEncontrado() {
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(null);
        ResponseEntity responseEntity = avisoViagemController.avisarViagem(1L, avisoViagemRequest, httpServletRequest);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve cadastrar aviso se cartão não pertence ao solicitante")
    public void naoDeveCadastrarAvisoSeCartaoNaoPertenceAoSolicitante() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(false);
        ResponseEntity responseEntity = avisoViagemController.avisarViagem(1L, avisoViagemRequest, httpServletRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve cadastrar aviso de viagem")
    public void deveCadastrarAvisoDeViagem() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(true);
        ResponseEntity responseEntity = avisoViagemController.avisarViagem(1L, avisoViagemRequest, httpServletRequest);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey("Location"));
    }
}