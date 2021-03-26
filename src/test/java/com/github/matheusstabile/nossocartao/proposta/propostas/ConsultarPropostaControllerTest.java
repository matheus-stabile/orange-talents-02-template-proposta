package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ConsultarPropostaControllerTest {

    private ConsultarPropostaController controller;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Proposta proposta;

    @Mock
    private Tracer tracer;

    @BeforeEach
    public void setup() {
        initMocks(this);
        controller = new ConsultarPropostaController(entityManager, tracer);
    }

    @Test
    @DisplayName("Não deve retornar a consulta da proposta se não for encontrada")
    public void naoDeveRetornarAConsultaDaProposta() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(null);
        ResponseEntity responseEntity = controller.consultarPropostaPeloId(1L, new String());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Não deve retornar a consulta se a proposta não pertencer ao solicitante")
    public void naoDeveRetornarAConsultaSeAPropostaNaoPertencerAoSolicitante() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(Optional.of(proposta));
        when(proposta.pertenceAoUsuario(anyString())).thenReturn(false);
        ResponseEntity responseEntity = controller.consultarPropostaPeloId(1L, new String());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve retornar a consulta da proposta")
    public void deveRetornarAConsultaDaProposta() {
        when(entityManager.find(any(), any(Long.class))).thenReturn(Optional.of(proposta));
        when(proposta.pertenceAoUsuario(anyString())).thenReturn(true);
        ResponseEntity responseEntity = controller.consultarPropostaPeloId(1L, new String());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof PropostaResponse);
    }

}