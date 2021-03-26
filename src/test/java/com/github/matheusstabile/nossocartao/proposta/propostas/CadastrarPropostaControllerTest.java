package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import com.github.matheusstabile.nossocartao.proposta.propostas.integracoes.AnaliseFinanceiraClient;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CadastrarPropostaControllerTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private AnaliseFinanceiraClient analiseFinanceiraService;

    @Mock
    private Tracer tracer;

    @Mock
    private Proposta proposta;

    @Mock
    private PropostaRequest request;

    @Mock
    private Span span;

    private CadastrarPropostaController cadastrarPropostaController;

    @BeforeEach
    public void setup() {
        initMocks(this);
        cadastrarPropostaController = new CadastrarPropostaController(propostaRepository, analiseFinanceiraService, tracer);
    }

    @Test
    @DisplayName("Não deve cadastrar mais de uma proposta com o mesmo documento")
    public void naoDeveCadastrarMaisDeUmaPropostaComOMesmoDocumento() {
        when(tracer.activeSpan()).thenReturn(span);
        when(propostaRepository.existsByDocumento(any())).thenReturn(true);
        when(request.toModel()).thenReturn(proposta);
        when(request.getDocumento()).thenReturn(String.valueOf(new Random().nextInt(100)));
        ResponseEntity responseEntity = cadastrarPropostaController.cadastrarProposta(request, UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve cadastrar proposta")
    public void deveCadastrarProposta() {
        when(tracer.activeSpan()).thenReturn(span);
        when(propostaRepository.existsByDocumento(any())).thenReturn(false);
        when(request.toModel()).thenReturn(proposta);
        when(request.getDocumento()).thenReturn(String.valueOf(new Random().nextInt(100)));
        ResponseEntity responseEntity = cadastrarPropostaController.cadastrarProposta(request, UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey("Location"));
    }

}