package com.github.matheusstabile.nossocartao.proposta.carteiras;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CarteiraControllerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CarteiraService carteiraService;

    @Mock
    private Cartao cartao;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    private CarteiraController controller;

    @BeforeEach
    public void setup(){
        initMocks(this);
        controller = new CarteiraController(entityManager, carteiraService, tracer);
    }

    @Test
    @DisplayName("Não deve cadastrar carteira se cartão não foi encontrado")
    public void naoDeveCadastrarCarteiraSamsungPaySeCartaoNaoFoiEncontrado(){
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(null);
        ResponseEntity responseEntity = controller.adicionarCarteiraPaypal(1L, null, new String(), UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve cadastrar carteira se cartão não pertence ao solicitante")
    public void naoDeveCadastrarCarteiraSeCartaoNaoPertenceAoSolicitante(){
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(false);
        ResponseEntity responseEntity = controller.adicionarCarteiraPaypal(1L, null, new String(), UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve cadastrar a carteira SamsungPay se ela estiver cadastrada já estiver cadastradaa")
    public void naoDeveCadastrarCarteiraSamsungPaySeJaEstiverCadastrada(){
        when(tracer.activeSpan()).thenReturn(span);
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(true);
        ResponseEntity responseEntity = controller.adicionarCarteiraSamsungpay(null, null, new String(), UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve cadastrar a carteira PayPal se ela estiver cadastrada já estiver cadastrada")
    public void naoDeveCadastrarCarteiraPayPalSeJaEstiverCadastrada(){
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(true);
        when(cartao.temCarteira(TipoCarteira.PAYPAL)).thenReturn(true);
        ResponseEntity responseEntity = controller.adicionarCarteiraPaypal(null, null, new String(), UriComponentsBuilder.newInstance());        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

}