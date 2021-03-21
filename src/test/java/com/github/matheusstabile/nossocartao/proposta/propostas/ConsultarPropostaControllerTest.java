package com.github.matheusstabile.nossocartao.proposta.propostas;

import com.github.matheusstabile.nossocartao.proposta.compartilhado.exceptions.ErroPadronizado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ConsultarPropostaControllerTest {


    ConsultarPropostaController consultarPropostaController;

    @Mock
    PropostaRepository propostaRepository;

    @Mock
    Proposta proposta;

    @BeforeEach
    void setup() {
        consultarPropostaController = new ConsultarPropostaController(propostaRepository);
//        propostaValida = new Proposta("11111111111", "user@email.com", "nome", "endereco", BigDecimal.ONE);
    }

    @Test
    @DisplayName("Deve retornar 404 quando informar id inexistente")
    void deveRetornar404ComIdInexistente() {

        Mockito.when(propostaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity responseEntity = consultarPropostaController.consultarPropostaPeloId(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody() instanceof ErroPadronizado);
    }

    @Test
    @DisplayName("Deve retornar a consulta da proposta")
    void deveRetornarAConsultaDaProposta() {

        Mockito.when(propostaRepository.findById(any(Long.class))).thenReturn(Optional.of(proposta));

        ResponseEntity responseEntity = consultarPropostaController.consultarPropostaPeloId(1L);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody() instanceof PropostaResponse);
    }


}