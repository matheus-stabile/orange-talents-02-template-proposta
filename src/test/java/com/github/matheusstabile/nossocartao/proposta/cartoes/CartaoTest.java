package com.github.matheusstabile.nossocartao.proposta.cartoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartaoTest {

    private Cartao cartao;

    @BeforeEach
    public void setup(){
        cartao = new Cartao();
    }

    @Test
    @DisplayName("Não deve incluir biometria no cartão se for nula")
    public void naoDeveIncluirBiometriaNoCartaoSeForNula() {
        assertThrows(IllegalArgumentException.class,
                () -> cartao.adicionaBiometria(null));
    }

    @Test
    @DisplayName("Não deve incluir bloqueio no cartão se for nulo")
    public void naoDeveIncluirBloqueioNoCartaoSeForNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> cartao.adicionarBloqueio(null));
    }

    @Test
    @DisplayName("Não deve incluir aviso viagem no cartão se for nulo")
    public void naoDeveIncluirAvisoViagemNoCartaoSeForNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> cartao.adicionarAviso(null));
    }

    @Test
    @DisplayName("Não deve incluir carteira no cartão se for nula")
    public void naoDeveIncluirCarteiraNoCartaoSeForNula() {
        assertThrows(IllegalArgumentException.class,
                () -> cartao.adicionaCarteira(null));
    }

    @Test
    @DisplayName("Não deve verificar carteira se for nula")
    public void naoDeveVerificarCarteiraSeForNula() {
        assertThrows(IllegalArgumentException.class,
                () -> cartao.temCarteira(null));
    }

}