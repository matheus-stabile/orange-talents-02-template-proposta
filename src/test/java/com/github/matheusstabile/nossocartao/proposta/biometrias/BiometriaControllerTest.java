package com.github.matheusstabile.nossocartao.proposta.biometrias;

import com.github.matheusstabile.nossocartao.proposta.cartoes.Cartao;
import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BiometriaControllerTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Tracer tracer;

    @Mock
    private Cartao cartao;

    @Mock
    private BiometriaRequest request;

    private BiometriaController controller;

    @BeforeEach
    public void setup() {
        initMocks(this);
        controller = new BiometriaController(entityManager, tracer);
    }

    @Test
    @DisplayName("Não deve cadastrar biometria quando cartão não for encontrado")
    public void naoDeveCadastrarBiometriaQuandoCartaoNaoForEncontrado() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(null);
        ResponseEntity responseEntity = controller.adicionaBiometria(1L, request, new String(), UriComponentsBuilder.newInstance());
        ;
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve cadastrar biometria se cartão não pertence ao solicitante")
    public void naoDeveCadastrarBiometriaSeCartaoNaoPertenceAoSolicitante() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(false);
        ResponseEntity responseEntity = controller.adicionaBiometria(1L, request, new String(), UriComponentsBuilder.newInstance());
        ;
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve cadastrar biometria")
    public void deveCadastrarBiometria() {
        when(entityManager.find(any(), any(UUID.class))).thenReturn(cartao);
        when(cartao.pertenceAoUsuario(anyString())).thenReturn(true);
        when(request.toModel(cartao)).thenReturn(new Biometria());
        ResponseEntity responseEntity = controller.adicionaBiometria(1L, request, new String(), UriComponentsBuilder.newInstance());
        ;
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey("Location"));
    }

}