package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.propostas.integracoes.AnaliseFinanceiraService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarPropostaControllerTest {

    @Mock
    PropostaRepository propostaRepository;

    @Mock
    AnaliseFinanceiraService analiseFinanceiraService;

    @Mock
    Proposta proposta;

    @Mock
    PropostaRequest propostaRequest;

    CadastrarPropostaController cadastrarPropostaController;

    @BeforeEach
    public void setup() {
        cadastrarPropostaController = new CadastrarPropostaController(propostaRepository, analiseFinanceiraService);
    }

    @Test
    @DisplayName("Não deve cadastrar mais de uma proposta com o mesmo documento")
    public void naoDeveCadastrarPropostaComMesmoDocumento() {

        when(propostaRepository.existsByDocumento(propostaRequest.getDocumento())).thenReturn(true);
        ResponseEntity responseEntity = cadastrarPropostaController.cadastrarProposta(propostaRequest, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Deve cadastrar uma proposta e retornar o location")
    public void deveCadastrarProposta() {

        when(propostaRepository.existsByDocumento(propostaRequest.getDocumento())).thenReturn(false);
        when(propostaRequest.toModel()).thenReturn(proposta);
        when(propostaRepository.save(proposta)).thenReturn(proposta);
        ResponseEntity responseEntity = cadastrarPropostaController.cadastrarProposta(propostaRequest, UriComponentsBuilder.newInstance());

        Assertions.assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertTrue(responseEntity.getHeaders().containsKey("Location"))
        );

    }
}