package com.github.matheusstabile.nossocartao.proposta.propostas.integracoes;

import com.github.matheusstabile.nossocartao.proposta.propostas.PropostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AnaliseFinanceiraServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private AnaliseFinanceiraClient analiseFinanceiraClient;

    private AnaliseFinanceiraService analiseFinanceiraService;

    @BeforeEach
    public void setup() {
        analiseFinanceiraService = new AnaliseFinanceiraService(propostaRepository, analiseFinanceiraClient);
    }

    @Test
    @DisplayName("Não deve processar proposta nula")
    void naoDeveProcessarPropostaNula() {

        assertThrows(IllegalArgumentException.class, () -> analiseFinanceiraService.processa(null));
    }
}