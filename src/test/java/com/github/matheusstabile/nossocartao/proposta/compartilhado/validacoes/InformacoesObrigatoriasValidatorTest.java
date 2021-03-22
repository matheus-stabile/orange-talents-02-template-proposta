package com.github.matheusstabile.nossocartao.proposta.compartilhado.validacoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class InformacoesObrigatoriasValidatorTest {

    InformacoesObrigatoriasValidator informacoesObrigatoriasValidator;

    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

    @BeforeEach
    void setup() {
        informacoesObrigatoriasValidator = new InformacoesObrigatoriasValidator();
        informacoesObrigatoriasValidator.initialize(null);
    }

    @Test
    @DisplayName("Deve retornar falso se o remoteAddr e User-Agent estiverem em branco")
    void deveRetornarFalsoSeORequestForNulo() {

        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("");
        Mockito.when(httpServletRequest.getHeader("User-Agent")).thenReturn("");

        boolean resposta = informacoesObrigatoriasValidator.isValid(httpServletRequest, null);

        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar falso se o remoteAddr for válido e o User-Agent estiver em branco")
    void deveRetornarFalsoSeRemoteAddrValidoEUserAgentEmBranco() {

        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        Mockito.when(httpServletRequest.getHeader("User-Agent")).thenReturn("");

        boolean resposta = informacoesObrigatoriasValidator.isValid(httpServletRequest, null);

        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar falso se o remoteAddr for estiver em branco e o User-Agent for válido")
    void deveRetornarFalsoSeRemoteAddrEmBrancoEUserAgentForValido() {

        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("");
        Mockito.when(httpServletRequest.getHeader("User-Agent")).thenReturn("chrome");

        boolean resposta = informacoesObrigatoriasValidator.isValid(httpServletRequest, null);

        assertFalse(resposta);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro se o remoteAddr e o User-Agent forem válidos")
    void deveRetornarVerdadeiroSeRemoteAddrEUserAgentValidos() {

        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        Mockito.when(httpServletRequest.getHeader("User-Agent")).thenReturn("chrome");

        boolean resposta = informacoesObrigatoriasValidator.isValid(httpServletRequest, null);

        assertTrue(resposta);
    }

}