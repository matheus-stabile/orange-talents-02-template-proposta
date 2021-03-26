package com.github.matheusstabile.nossocartao.proposta.compartilhado.seguranca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CryptoTest {

    @Test
    @DisplayName("Deve lançar exception se informação para criptografia for nula")
    public void deveLancaoExceptionSeInformacaoParaCriptografiaForNula() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Crypto.encrypt(null));
    }

}